package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.SceneManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField cf_passwordField;
    @FXML private TextField emailField;
    @FXML private Label messageLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    private void submitRegister(ActionEvent actionEvent) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = cf_passwordField.getText();
        String email = emailField.getText();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            messageLabel.setText("Please fill all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        User user = new User(username, password, email);

        if (userRepository.create(user)) {
            messageLabel.setText("Registration successful! Please login.");
            // Optional: Redirect to login scene
        }
    }

    @FXML
    private void cancelRegister(ActionEvent actionEvent) throws IOException {
        SceneManager.getInstance().setLoginScene("Login.fxml");
    }
}
