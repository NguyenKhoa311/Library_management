package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class AddUserController {
    @FXML private TextField usernameField;
    @FXML private  PasswordField passwordField;
    @FXML private  PasswordField cf_passwordField;
    @FXML private  TextField emailField;
    @FXML private  ComboBox<String> roleBox;

    public void initialize() {
        roleBox.getItems().addAll("admin", "user");
    }

    public void addUser() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = cf_passwordField.getText();
        String email = emailField.getText();
        String role = roleBox.getValue();

        // Basic validation
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        if (role == null || role.isEmpty()) {
            showAlert("Please choose role for user.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Passwords do not match.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Invalid email address.");
            return;
        }

        User user = new User(username, password, email, role);

        // Create Task to add user
        Task<Boolean> addUserTask = new Task<>() {
            @Override
            protected Boolean call() {
                return UserRepository.getInstance().create(user); // Thực hiện thêm người dùng
            }
        };

        // Use TaskManager to handle task
        TaskManager.runTask(
                addUserTask,
                () -> { // On Success
                    if (addUserTask.getValue()) {
                        showAlert("User added successfully.");
                        // Đóng cửa sổ sau khi thêm thành công
                        Stage stage = (Stage) usernameField.getScene().getWindow();
                        stage.close();
                    } else {
                        showAlert("Failed to add user. Please try again.");
                    }
                },
                () -> showAlert("An error occurred while adding the user. Please try again.") // On Failure
        );
    }

    public boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
