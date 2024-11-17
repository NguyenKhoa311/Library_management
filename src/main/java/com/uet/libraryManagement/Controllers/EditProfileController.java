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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;

public class EditProfileController {
    @FXML private ImageView avatarImage;
    @FXML private Label usernameLabel;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField nameField;
    @FXML private DatePicker birthdayField;


    private String avatarPath;
    private final User currentUser = SessionManager.getInstance().getUser();
    private final UserRepository userRepository = new UserRepository();

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

            // Hiển thị ảnh trong ImageView mà không sao chép vào thư mục đích ngay
            Image thumbnailImage = new Image(new File(avatarPath).toURI().toString());
            avatarImage.setImage(thumbnailImage);
        }
    }

    public void saveChanges(ActionEvent actionEvent) {
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

        // copy image to avatars folder if new avatar is inserted
        if (avatarPath != null && !avatarPath.isEmpty() && !avatarPath.equals(currentUser.getAvatarUrl())) {
            try {
                Path targetDir = Paths.get("user_data/avatars");
                Files.createDirectories(targetDir);
                Path targetPath = targetDir.resolve(new File(avatarPath).getName());
                Files.copy(Paths.get(avatarPath), targetPath, StandardCopyOption.REPLACE_EXISTING);

                currentUser.setAvatarUrl("user_data/avatars/" + new File(avatarPath).getName());
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error copying image file.");
                return;
            }
        }

        userRepository.updateProfile(currentUser);
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

        if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
            File file = new File(currentUser.getAvatarUrl());
            Image image = new Image(file.toURI().toString(), true);
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
