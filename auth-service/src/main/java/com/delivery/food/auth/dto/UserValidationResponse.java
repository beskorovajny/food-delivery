package com.delivery.food.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserValidationResponse {
    private Long userId;
    private String email;
    private List<String> roles;
    private boolean valid;

    public String getUsername() {
        return email;
    }
}
