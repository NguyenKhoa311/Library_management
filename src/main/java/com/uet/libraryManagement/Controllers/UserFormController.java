package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class UserFormController {
    @FXML private ImageView userAva;
    @FXML private Label userNameLabel;
    @FXML private TextField nameField;
    @FXML private DatePicker birthdayField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private User currentUser;
    private String avatarFile;
    private byte[] tmpAvatar;

    public void setUserInfo(User user) {
        this.currentUser = user;
        userNameLabel.setText("Username:" + user.getUsername());
        nameField.setText((user.getFullName() == null) ? "N/A" : user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText((user.getPhone() == null) ? "N/A" : user.getPhone());

        // Parse and set birthday if available
        if (user.getBirthday() != null && !user.getBirthday().isEmpty()) {
            birthdayField.setValue(LocalDate.parse(user.getBirthday()));
        } else {
            birthdayField.setPromptText("N/A");
        }

        // Load avatar image if available
        if (user.getAvatar() != null) {
            Image avatarImage = new Image(new ByteArrayInputStream(user.getAvatar()));
            userAva.setImage(avatarImage);
        }
    }

    public void insertImg() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Avatar Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(userAva.getScene().getWindow());
        if (selectedFile != null) {
            avatarFile = selectedFile.getAbsolutePath(); // Lưu tạm đường dẫn ảnh

            try {
                // Đọc ảnh thành mảng byte
                tmpAvatar = java.nio.file.Files.readAllBytes(selectedFile.toPath());

                // Hiển thị ảnh trong ImageView
                Image thumbnailImage = new Image(new File(avatarFile).toURI().toString());
                userAva.setImage(thumbnailImage);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error reading image file.");
            }
        }
    }

    public void saveUser() throws IOException {
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        stage.close();
    }
}
