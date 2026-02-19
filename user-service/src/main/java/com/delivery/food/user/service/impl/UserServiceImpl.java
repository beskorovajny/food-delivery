package com.delivery.food.user.service.impl;

import com.delivery.food.user.domain.User;
import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.mapper.UserMapper;
import com.delivery.food.user.repository.UserRepository;
import com.delivery.food.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto create(UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        user.setRole(dto.getRole() != null ? dto.getRole() : User.Role.CUSTOMER);
        user.setActive(true);
        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }


    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
        return userMapper.toResponseDto(user);
    }
}