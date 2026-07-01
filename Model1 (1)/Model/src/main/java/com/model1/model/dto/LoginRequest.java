package main.java.com.model1.model.dto;

import main.java.com.model1.model.enums.Role;

public record LoginRequest(
        String username,
        String password,
        Role role) {
}
