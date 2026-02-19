package com.delivery.food.user.controller;

import com.delivery.food.user.dto.UserCreateDto;
import com.delivery.food.user.dto.UserResponseDto;
import com.delivery.food.user.dto.UserUpdateDto;
import com.delivery.food.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 20;
    private static final String DEFAULT_SORT = "id,asc";

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponseDto> create(@RequestBody UserCreateDto dto) {
        return ok(userService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDto> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> partialUpdate(@PathVariable Long id, @Valid @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    // PUT (full replace)
    // @PutMapping("/{id}")
    // public ResponseEntity<UserResponseDto> fullUpdate(
    //         @PathVariable Long id,
    //         @Valid @RequestBody UserUpdateDto dto) {
    //     return ResponseEntity.ok(userService.update(id, dto));
    // }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAll(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<UserResponseDto> users = userService.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<UserResponseDto>> getActive(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {

        Page<UserResponseDto> users = userService.findAllByActive(true, pageable);
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
