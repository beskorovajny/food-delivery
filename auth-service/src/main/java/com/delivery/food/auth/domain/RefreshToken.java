package com.delivery.food.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Entity for storing refresh tokens (and optionally access tokens if blacklisted).
 * <p>
 * Key design principles:
 * - Only refresh tokens are stored here (access tokens are stateless).
 * - Tokens are hashed before storage (never plain text in DB).
 * - Full support for revocation, rotation, and logout.
 * - Indexed for fast lookups (by user and token hash).
 * - Cleanup job can delete expired tokens periodically.
 */
@Entity
@Table(name = "refresh_tokens",
        indexes = {
                @Index(name = "idx_refresh_token_user_id", columnList = "user_id"),
                @Index(name = "idx_refresh_token_expires_at", columnList = "expires_at")
        })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Hashed value of the refresh token.
     * NEVER store plain token — always hash (e.g. SHA-256 or BCrypt).
     * Unique index ensures no duplicates.
     */
    @Column(name = "token_hash", unique = true, nullable = false, length = 255)
    private String tokenHash;

    /**
     * Foreign key to user (from user-service).
     * Indexed for fast "revoke all for user" queries.
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * When this token was issued.
     * Useful for audit, rotation detection, and debugging.
     */
    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    /**
     * When this token expires (absolute time).
     * Better than boolean expired flag — allows precise checks.
     * Indexed for cleanup of expired tokens.
     */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /**
     * Whether this token has been revoked (logout, password change, etc.).
     * Default: false.
     * Indexed for fast "is revoked?" checks.
     */
    @Column(name = "revoked", nullable = false)
    private boolean revoked = false;

    /**
     * When this token was revoked (null if not revoked).
     * For audit and debugging.
     */
    @Column(name = "revoked_at")
    private Instant revokedAt;

    /**
     * Optional: user-agent or device fingerprint.
     * Helps implement "active sessions" UI or per-device logout.
     */
    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    // Helper methods (add these as needed)

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }

    public boolean isValid() {
        return !revoked && !isExpired();
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = Instant.now();
    }
}