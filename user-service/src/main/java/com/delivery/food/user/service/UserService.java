package com.delivery.food.user.service;

import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.dto.UserUpdateDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all users with pagination and sorting
     *
     * @return list of users with pagination
     */
    Page<UserResponseDto> findAll(Pageable pageable);

    // Optional: with simple filter example
    Page<UserResponseDto> findAllByActive(boolean active, Pageable pageable);

    /**
     * Delete user if given primary key (ID) found
     *
     * @param id user identifier
     */
    void delete(Long id);

}