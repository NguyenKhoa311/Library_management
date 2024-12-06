package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.concurrent.Task;
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

        Task<Boolean> registerTask = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return UserRepository.getInstance().create(user);
            }
        };

        Runnable onSuccess = () -> {
            if (registerTask.getValue()) {
                messageLabel.setText("Registration successful!");
            } else {
                messageLabel.setText("Registration failed. Username already exist.");
            }
        };

        Runnable onFailure = () -> {
            messageLabel.setText("An error occurred. Please try again.");
        };

        TaskManager.runTask(registerTask, onSuccess, onFailure);
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
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
