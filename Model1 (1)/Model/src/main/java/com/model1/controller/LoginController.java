package main.java.com.model1.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import main.java.com.model1.app.AppContext;
import main.java.com.model1.app.SceneRouter;
import main.java.com.model1.exception.AuthenticationException;
import main.java.com.model1.exception.DatabaseException;
import main.java.com.model1.exception.ValidationException;
import main.java.com.model1.model.dto.LoginRequest;
import main.java.com.model1.model.dto.UserSession;
import main.java.com.model1.model.enums.Role;
import main.java.com.model1.service.AuthService;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ToggleGroup roleGroup;
    @FXML
    private ToggleButton cashierToggle;
    @FXML
    private ToggleButton managerToggle;
    @FXML
    private ToggleButton administratorToggle;
    @FXML
    private Label statusLabel;
    @FXML
    private Button signInButton;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        cashierToggle.setUserData(Role.CASHIER);
        managerToggle.setUserData(Role.MANAGER);
        administratorToggle.setUserData(Role.ADMINISTRATOR);
        roleGroup.selectToggle(cashierToggle);
        roleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                roleGroup.selectToggle(oldToggle == null ? cashierToggle : oldToggle);
            }
            clearStatus();
        });

        Platform.runLater(() -> usernameField.requestFocus());
    }

    @FXML
    private void handleLogin() {
        Role role = selectedRole();
        signInButton.setDisable(true);
        setStatus("Checking credentials...", false);

        try {
            UserSession session = authService.authenticate(new LoginRequest(usernameField.getText(), passwordField.getText(), role));
            AppContext.getInstance().setActiveSession(session);
            router().showDashboard(session.role());
        } catch (AuthenticationException | ValidationException ex) {
            setStatus(ex.getMessage(), true);
        } catch (DatabaseException ex) {
            setStatus("Authentication is unavailable because the database could not be reached.", true);
        } catch (RuntimeException ex) {
            setStatus("Unable to open the dashboard.", true);
        } finally {
            signInButton.setDisable(false);
        }
    }

    private Role selectedRole() {
        Toggle selectedToggle = roleGroup.getSelectedToggle();
        if (selectedToggle == null) {
            return Role.CASHIER;
        }
        Object value = selectedToggle.getUserData();
        return value instanceof Role role ? role : Role.CASHIER;
    }

    private SceneRouter router() {
        return AppContext.getInstance()
                .getSceneRouter()
                .orElseThrow(() -> new IllegalStateException("SceneRouter has not been initialized."));
    }

    private void setStatus(String message, boolean error) {
        statusLabel.setText(message);
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
        statusLabel.getStyleClass().add(error ? "status-error" : "status-success");
    }

    @FXML
    private void clearStatus() {
        statusLabel.setText("");
        statusLabel.getStyleClass().removeAll("status-error", "status-success");
    }
}
