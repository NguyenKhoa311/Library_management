package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
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
    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField nameField;
    @FXML private DatePicker birthdayField;

    private byte[] tmpAvatar;
    private String avatarPath;
    private final User currentUser = SessionManager.getInstance().getUser();

    public void initialize() {
        loadUserInfo();
    }

    public void insertImg(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(avatarImage.getScene().getWindow());
        if (selectedFile != null) {
            avatarPath = selectedFile.getAbsolutePath(); // Lưu tạm đường dẫn ảnh

            try {
                // Đọc ảnh thành mảng byte
                tmpAvatar = java.nio.file.Files.readAllBytes(selectedFile.toPath());

                // Hiển thị ảnh trong ImageView
                Image thumbnailImage = new Image(new File(avatarPath).toURI().toString());
                avatarImage.setImage(thumbnailImage);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error reading image file.");
            }
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
        if (tmpAvatar != null) {
            currentUser.setAvatar(tmpAvatar);
        }

        UserRepository.getInstance().updateProfile(currentUser);
        showAlert("Profile changed successfully.");
        closeForm();
    }

    private void loadUserInfo() {
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

        if (currentUser.getAvatar() != null) {
            // Hiển thị avatar từ byte[]
            Image image = new Image(new java.io.ByteArrayInputStream(currentUser.getAvatar()));
            avatarImage.setImage(image);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }
}
