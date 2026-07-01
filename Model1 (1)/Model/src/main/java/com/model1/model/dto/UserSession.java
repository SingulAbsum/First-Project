package main.java.com.model1.model.dto;

import java.util.Objects;

import main.java.com.model1.model.enums.Role;

public record UserSession(String username, Role role) {
    public UserSession {
        username = Objects.requireNonNull(username, "username").trim();
        role = Objects.requireNonNull(role, "role");
    }
}
