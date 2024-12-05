package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.APIService.ImgurUpload;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class EditProfileController {
    @FXML
    public ImageView avatarImage;
    @FXML
    public Label usernameLabel;
    @FXML
    public TextField phoneField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField nameField;
    @FXML
    public DatePicker birthdayField;

    public String avatarPath;
    private final User currentUser = SessionManager.getInstance().getUser();

    public void initialize() {
        loadUserInfo();
    }

    public void insertImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());
        if (selectedFile != null) {
            avatarPath = selectedFile.getAbsolutePath(); // Lưu tạm đường dẫn ảnh

            // show temporary image
            Image img = new Image(new File(avatarPath).toURI().toString());
            avatarImage.setImage(img);
        }
    }

    public void saveChanges(ActionEvent actionEvent) throws IOException {
        String phone = phoneField.getText();
        String email = emailField.getText();
        String name = nameField.getText();
        String birthday = (birthdayField.getValue() != null) ? birthdayField.getValue().toString() : currentUser.getBirthday();

        if (phone.isEmpty() || email.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }
        currentUser.setFullName(name);
        currentUser.setPhone(phone);
        currentUser.setEmail(email);
        currentUser.setBirthday(birthday);

        // Nếu có avatar mới, tải ảnh lên Imgur
        Task<Void> saveProfileTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                if (avatarPath != null && !avatarPath.isEmpty()) {
                    String imgurUrl = ImgurUpload.uploadImage(new File(avatarPath)); // Tải ảnh lên Imgur
                    currentUser.setAvatar(imgurUrl); // Cập nhật URL avatar
                }
                // Cập nhật thông tin user trong database
                UserRepository.getInstance().updateProfile(currentUser);
                return null;
            }
        };

        // Xử lý thành công hoặc thất bại
        TaskManager.runTask(saveProfileTask, () -> {
            // Thành công: hiển thị thông báo và đóng form
            Platform.runLater(() -> {
                showAlert("Profile changed successfully.");
                closeForm();
            });
        }, () -> {
            // Thất bại: thông báo lỗi
            Platform.runLater(() -> showAlert("Failed to update profile. Please try again."));
        });
    }

    public void loadUserInfo() {
        usernameLabel.setText(currentUser.getUsername());
        nameField.setText(((currentUser.getFullName() == null) ? "N/A" : currentUser.getFullName()));
        phoneField.setText(((currentUser.getPhone() == null) ? "N/A" : currentUser.getPhone()));
        emailField.setText(((currentUser.getEmail() == null) ? "N/A" : currentUser.getEmail()));

        // Set birthdayField if birthday exists
        if (currentUser.getBirthday() != null && !currentUser.getBirthday().isEmpty()) {
            LocalDate birthday = LocalDate.parse(currentUser.getBirthday());
            birthdayField.setValue(birthday);
        } else {
            birthdayField.setPromptText("N/A");
        }

        if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            Image image = new Image(currentUser.getAvatar(), true);
            avatarImage.setImage(image);
        }
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void closeForm() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }
}
