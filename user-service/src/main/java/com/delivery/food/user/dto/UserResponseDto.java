package com.delivery.food.user.dto;

import com.delivery.food.user.domain.User.Role;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String email;
    private String fullName;
    private String phone;
    private Role role;
    private boolean active;
}