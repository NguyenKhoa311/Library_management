package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML public TextField usernameField;
    @FXML public PasswordField passwordField;
    @FXML public PasswordField cf_passwordField;
    @FXML public TextField emailField;
    @FXML public Label messageLabel;

    @FXML
    public void submitRegister() {
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
        if (!isValidEmail(email)) {
            messageLabel.setText("Invalid email address.");
            return;
        }

        User user = new User(username, password, email);

        if (UserRepository.getInstance().create(user)) {
            messageLabel.setText("Registration successful!");
        }
    }

    @FXML
    public void cancelRegister(ActionEvent actionEvent) throws IOException {
        SceneManager.getInstance().setLoginScene("FXML/Login.fxml");
    }

    /**
     * Validate email using a regex pattern.
     *
     * @param email the email string to validate
     * @return true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailRegex);
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getCf_passwordField() {
        return cf_passwordField;
    }

    public TextField getEmailField() {
        return emailField;
    }

    public Label getMessageLabel() {
        return messageLabel;
    }
}
