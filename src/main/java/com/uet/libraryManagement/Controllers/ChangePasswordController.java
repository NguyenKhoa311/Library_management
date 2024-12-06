package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.concurrent.Task;
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

        // check if new password is the same to the old one
        if (currentUser.getPassword().equals(newPassword)) {
            messageLabel.setText("New password is the same to the old password");
            return;
        }

        // Check if new password and confirmation match
        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        Task<Void> updatePasswordTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Update password in the user and database
                currentUser.setPassword(newPassword);
                UserRepository.getInstance().updatePassword(currentUser);
                return null;
            }
        };

        Runnable onSuccess = () -> {
            showAlert("Password changed successfully.");
            closeForm();
        };

        Runnable onFailure = () -> {
            Throwable ex = updatePasswordTask.getException();
            if (ex != null) ex.printStackTrace(); // Log the error for debugging
            showAlert("Failed to change the password.");
        };

        TaskManager.runTask(updatePasswordTask, onSuccess, onFailure);
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
