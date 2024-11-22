package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Manager.SessionManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChangePasswordController {
    @FXML private TextField currentPassField;
    @FXML private TextField newPassField;
    @FXML private TextField confirmPassField;
    @FXML private Label messageLabel;

    private final User currentUser = SessionManager.getInstance().getUser();

    @FXML
    private void savePassword() {
        String currentPassword = currentPassField.getText();
        String newPassword = newPassField.getText();
        String confirmPassword = confirmPassField.getText();

        // Validate current password
        if (!currentUser.getPassword().equals(currentPassword)) {
            messageLabel.setText("Incorrect Password");
            return;
        }

        // Check if new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        // Update password in the user and database
        currentUser.setPassword(newPassword);
        UserRepository.getInstance().updatePassword(currentUser);

        showAlert("Password changed successfully.");
        closeForm();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}
