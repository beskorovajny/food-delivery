package com.delivery.food.auth.service;

import com.delivery.food.auth.SimpleUserDetails;
import com.delivery.food.auth.dto.AuthResponse;
import com.delivery.food.auth.dto.LoginRequest;
import com.delivery.food.auth.dto.UserValidationResponse;
import com.delivery.food.auth.domain.RefreshToken;
import com.delivery.food.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final WebClient userServiceWebClient; // configured to point to user-service

    private static final String USER_SERVICE_VALIDATE_URI = "/internal/validate-credentials";

    /**
     * Authenticates user credentials by calling user-service.
     * If valid → issues access token + refresh token, stores refresh, sets cookie.
     */
    @Transactional
    public AuthResponse login(LoginRequest request, HttpServletResponse response) {
        // 1. Call user-service to validate email/password
        UserValidationResponse validation = userServiceWebClient.post()
                .uri(USER_SERVICE_VALIDATE_URI)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, res ->
                        Mono.error(new BadCredentialsException("Invalid credentials")))
                .bodyToMono(UserValidationResponse.class)
                .block();

        if (validation == null || !validation.isValid()) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Long userId = validation.getUserId();
        String username = validation.getUsername();

        // 2. Generate tokens
        String accessToken = jwtService.generateToken(Map.of("userId", userId), new SimpleUserDetails(username));
        String refreshToken = UUID.randomUUID().toString(); // opaque refresh token

        // 3. Store refresh token (hashed)
        RefreshToken storedRefresh = RefreshToken.builder()
                .tokenHash(hashToken(refreshToken))
                .userId(userId)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .revoked(false)
                .build();

        refreshTokenRepository.save(storedRefresh);

        // 4. Set HttpOnly cookie for refresh token
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true) // false in local dev, true in prod
                .path("/")
                .sameSite("Strict")
                .maxAge(jwtService.getRefreshTokenExpirationMs() / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        // 5. Return access token + basic info
        return new AuthResponse(accessToken, "Bearer", validation.getUsername(), validation.getRoles());
    }

    /**
     * Refreshes access token using refresh cookie.
     * Rotates refresh token (new token, revoke old).
     */
    @Transactional
    public AuthResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No refresh token");
        }

        String tokenHash = hashToken(refreshToken);

        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (stored.isRevoked() || stored.isExpired()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh token expired or revoked");
        }

        // Get user details from user-service (or cache if you want)
        UserValidationResponse user = getUserFromUserService(stored.getUserId());

        // Generate new tokens
        String newAccessToken = jwtService.generateToken(Map.of("userId", stored.getUserId()),
                new SimpleUserDetails(user.getUsername()));

        String newRefreshToken = UUID.randomUUID().toString();
        String newTokenHash = hashToken(newRefreshToken);

        // Rotate: revoke old, save new
        stored.revoke();
        refreshTokenRepository.save(stored);

        RefreshToken newStored = RefreshToken.builder()
                .tokenHash(newTokenHash)
                .userId(stored.getUserId())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusMillis(jwtService.getRefreshTokenExpirationMs()))
                .revoked(false)
                .deviceInfo(stored.getDeviceInfo())
                .build();

        refreshTokenRepository.save(newStored);

        // Set new refresh cookie
        ResponseCookie newCookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite("Strict")
                .maxAge(jwtService.getRefreshTokenExpirationMs() / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newCookie.toString());

        return new AuthResponse(newAccessToken, "Bearer", user.getUsername(), user.getRoles());
    }

    /**
     * Logs out user by revoking refresh token and clearing cookie.
     */
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            return; // already logged out
        }

        String tokenHash = hashToken(refreshToken);

        refreshTokenRepository.findByTokenHash(tokenHash)
                .ifPresent(token -> {
                    token.revoke();
                    refreshTokenRepository.save(token);
                });

        // Clear cookie
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, deleteCookie.toString());
    }

    // Helpers (implement as needed)

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refresh_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private String hashToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        return new BCryptPasswordEncoder(12).encode(token);
    }

    private UserValidationResponse getUserFromUserService(Long userId) {
        // WebClient call to user-service /internal/users/{id}
        // implement or mock
        return new UserValidationResponse(userId, "user@example.com", List.of("ROLE_USER"), true);
    }
}