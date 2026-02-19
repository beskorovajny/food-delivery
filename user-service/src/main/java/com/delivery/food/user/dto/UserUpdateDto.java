package com.delivery.food.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserUpdateDto {
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 2, max = 100, message = "Full name length must be 2–100")
    private String fullName;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone format")
    private String phone;

    // add later
    // private Role role;
}
