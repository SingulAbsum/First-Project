package main.java.com.model1.service;

import java.sql.SQLException;

import main.java.com.model1.exception.AuthenticationException;
import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.model.dto.LoginRequest;
import main.java.com.model1.model.dto.UserSession;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.repository.AuthRepository;
import main.java.com.model1.repository.DatabaseManager;
import main.java.com.model1.repository.LoginHistoryRepository;
import main.java.com.model1.util.ValidationUtils;

public class AuthService {
    private final AuthRepository authRepository;
    private final LoginHistoryRepository loginHistoryRepository;

    public AuthService() {
        this(new AuthRepository(), new LoginHistoryRepository());
    }

    public AuthService(AuthRepository authRepository, LoginHistoryRepository loginHistoryRepository) {
        this.authRepository = authRepository;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    public boolean login(LoginRequest request) {
        try {
            LoginRequest normalizedRequest = validateRequest(request);
            boolean authenticated = authRepository.credentialsMatch(normalizedRequest);
            if (authenticated && normalizedRequest.role() == Role.CASHIER) {
                DatabaseManager.inTransaction(connection -> {
                    loginHistoryRepository.save(connection, normalizedRequest.username());
                });
            }
            return authenticated;
        } catch (SQLException ex) {
            throw new DatabaseException("Unable to authenticate user.", ex);
        }
    }

    public UserSession authenticate(LoginRequest request) {
        if (!login(request)) {
            throw new AuthenticationException("Invalid username or password.");
        }
        LoginRequest normalizedRequest = validateRequest(request);
        return new UserSession(normalizedRequest.username(), normalizedRequest.role());
    }

    private LoginRequest validateRequest(LoginRequest request) {
        ValidationUtils.requireNonNull(request, "Login request");
        String username = ValidationUtils.requireNonBlank(request.username(), "Username");
        ValidationUtils.requireNonBlank(request.password(), "Password");
        ValidationUtils.requireNonNull(request.role(), "Role");
        return new LoginRequest(username, request.password(), request.role());
    }
}
