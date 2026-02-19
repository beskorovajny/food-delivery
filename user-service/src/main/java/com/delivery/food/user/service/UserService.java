package com.delivery.food.user.service;

import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import jakarta.persistence.EntityNotFoundException;

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

}