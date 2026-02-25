package com.delivery.food.user.repository;

import com.delivery.food.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for User entity operations.
 *
 * <p>This interface extends JpaRepository to provide standard CRUD and pagination.
 * Custom methods are added for specific queries like findByEmail.
 * In production, ensure indexes on frequently queried fields (email, active) via schema.sql or migration.
 * For complex filters, consider JpaSpecificationExecutor or @Query.
 * </p>
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email the user's email
     * @return Optional<User> if found, empty otherwise
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by primary key (ID)
     *
     * @param id the user primary key (ID) to check
     * @return Optional<User> if found, empty otherwise
     *//*
    Optional<User> findById(Long id);*/

    /**
     * Finds users by active status with pagination.
     *
     * @param active the active status
     * @param pageable pagination and sorting info
     * @return Page of User entities
     */
    Page<User> findByActive(boolean active, Pageable pageable);
}
