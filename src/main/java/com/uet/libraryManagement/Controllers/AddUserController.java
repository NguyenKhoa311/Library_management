package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
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

        User user = new User(username, password, email, role);

        if (UserRepository.getInstance().create(user)) {
            showAlert("User added successfully.");
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
