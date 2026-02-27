package com.delivery.food.auth.repository;

import com.delivery.food.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository for managing refresh tokens.
 *
 * Supports:
 * - Lookup by token hash (validation/refresh/logout)
 * - Per-user active tokens (for session list or bulk revocation)
 * - Bulk revocation (logout from all devices)
 * - Cleanup of expired tokens
 * - Existence check for fast revocation validation
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Finds a refresh token by its hash (used for validation, refresh, logout).
     */
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    /**
     * Checks if an active (not revoked + not expired) refresh token exists for given hash.
     * Fast check — used in /refresh and /logout to avoid full entity load if only existence needed.
     */
    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM RefreshToken t " +
            "WHERE t.tokenHash = :tokenHash " +
            "AND t.revoked = false " +
            "AND t.expiresAt > :now")
    boolean existsActiveByTokenHash(@Param("tokenHash") String tokenHash, @Param("now") Instant now);

    /**
     * Finds all active (not revoked + not expired) refresh tokens for a user.
     * Useful for "active sessions" UI or audit.
     */
    @Query("SELECT t FROM RefreshToken t " +
            "WHERE t.userId = :userId " +
            "AND t.revoked = false " +
            "AND t.expiresAt > :now")
    List<RefreshToken> findActiveByUserId(@Param("userId") Long userId, @Param("now") Instant now);

    /**
     * Revokes all refresh tokens for a user (logout from all devices).
     * Sets revoked = true and updates revokedAt timestamp.
     */
    @Modifying
    @Query("UPDATE RefreshToken t " +
            "SET t.revoked = true, t.revokedAt = CURRENT_TIMESTAMP " +
            "WHERE t.userId = :userId")
    void revokeAllByUserId(@Param("userId") Long userId);

    /**
     * Deletes all expired refresh tokens (cleanup job).
     * Run periodically via @Scheduled task.
     */
    @Modifying
    @Query("DELETE FROM RefreshToken t WHERE t.expiresAt < :now")
    void deleteExpired(@Param("now") Instant now);

    /**
     * Optional: count active tokens for a user (for rate limiting or session limits).
     */
    @Query("SELECT COUNT(t) FROM RefreshToken t " +
            "WHERE t.userId = :userId " +
            "AND t.revoked = false " +
            "AND t.expiresAt > :now")
    long countActiveByUserId(@Param("userId") Long userId, @Param("now") Instant now);
}
