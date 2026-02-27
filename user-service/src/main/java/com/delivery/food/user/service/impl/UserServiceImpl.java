package com.delivery.food.user.service.impl;

import com.delivery.food.common.exception.DuplicateEntityException;
import com.delivery.food.common.exception.EntityNotFoundException;
import com.delivery.food.user.domain.User;
import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.dto.UserUpdateDto;
import com.delivery.food.user.mapper.UserMapper;
import com.delivery.food.user.repository.UserRepository;
import com.delivery.food.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public UserResponseDto create(UserCreateDto dto) {
        log.debug("Creating user with email: {}", dto.getEmail());

        userRepository.findByEmail(dto.getEmail()).ifPresent(existing -> {
            throw new DuplicateEntityException("User with email " + dto.getEmail() + " already exists");
        });
        User user = userMapper.toEntity(dto);
        user.setRole(User.Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setActive(true);
        User saved = userRepository.save(user);

        log.info("User created: id={}, email={}", user.getId(), user.getEmail());
        return userMapper.toResponseDto(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findById(Long id) {
        log.debug("Fetching user by id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findByEmail(String email) {
        log.debug("Fetching user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found by email: " + email));
        return userMapper.toResponseDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> findAll(Pageable pageable) {
        log.debug("Fetching all users: page={}, size={}", pageable.getPageNumber(), pageable.getPageSize());

        return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserResponseDto> findAllByActive(boolean active, Pageable pageable) {
        log.debug("Fetching {} users: page={}, size={}",
                active ? "active" : "inactive", pageable.getPageNumber(), pageable.getPageSize());

        return userRepository.findByActive(active, pageable)
                .map(userMapper::toResponseDto);
    }

    @Transactional
    @Override
    public UserResponseDto update(Long id, UserUpdateDto dto) {
        log.debug("Updating user id: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        userMapper.updateFromDto(dto, user);
        User updated = userRepository.save(user);

        log.info("User updated: id={}", user.getId());
        return userMapper.toResponseDto(updated);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        log.debug("Deleting user id: {}", id);

        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }

        // Soft delete (recommended for most apps)
        User user = userRepository.getReferenceById(id);
        user.setActive(false);
        userRepository.save(user);

        log.info("User deactivated: id={}", id);

        // Hard delete (if you really want to remove record)
        // userRepository.deleteById(id);
    }
}