package com.delivery.food.user.service;

import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for user management.
 *
 * <p>This interface defines the contract for user operations.
 * Implementations should handle business logic, validation, and orchestration.
 * In production, add caching (e.g. @Cacheable for findById) if reads are frequent.
 * </p>
 */
public interface UserService {

    UserResponseDto create(UserCreateDto dto);

    /**
     * Find user by primary key (ID)
     *
     * @param id user identifier
     * @return UserResponseDto if found
     * @throws EntityNotFoundException if not found
     */
    UserResponseDto findById(Long id);

    /**
     * Find user by unique email
     *
     * @param email user's email address
     * @return UserResponseDto if found
     * @throws EntityNotFoundException if not found
     */
    UserResponseDto findByEmail(String email);

    /**
     * Update user with given primary key (ID) found
     *
     * @param id  user identifier
     * @param dto UserResponseDto
     * @return UserResponseDto if updated
     */
    UserResponseDto update(Long id, UserUpdateDto dto);

    /**
     * Lists all users with pagination and sorting.
     *
     * <p>This method supports pagination to avoid loading all records at once, which is critical for scalability.
     * Sorting is handled by Pageable (e.g., sort by fullName asc).
     * In production, consider adding filters (e.g. by role) via QueryDSL or Specification for flexibility.
     * </p>
     *
     * @param pageable pagination and sorting parameters (page, size, sort)
     * @return Page of UserResponseDto
     */
    Page<UserResponseDto> findAll(Pageable pageable);

    /**
     * Lists active users with pagination and sorting.
     *
     * <p>Similar to findAll but filtered by active=true.
     * This is an example of a simple filter; for more complex ones, use Specification<User> in the repository.
     * </p>
     *
     * @param active the active status (true/false)
     * @param pageable pagination and sorting
     * @return Page of UserResponseDto
     */
    Page<UserResponseDto> findAllByActive(boolean active, Pageable pageable);

    /**
     * Delete user if given primary key (ID) found
     *
     * @param id user identifier
     */
    void delete(Long id);

}