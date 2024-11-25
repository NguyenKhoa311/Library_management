package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.APIService.ImgurUpload;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
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

public class UserFormController {
    @FXML private ImageView userAva;
    @FXML private Label userNameLabel;
    @FXML private TextField nameField;
    @FXML private DatePicker birthdayField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private User currentUser;
    private String avatarPath;

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
        if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            Image image = new Image(currentUser.getAvatar(), true);
            userAva.setImage(image);
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
            avatarPath = selectedFile.getAbsolutePath(); // Lưu tạm đường dẫn ảnh

            // show temporary image
            Image img = new Image(new File(avatarPath).toURI().toString());
            userAva.setImage(img);
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

        // Nếu thumbnail đã được chọn, tải lên Imgur và lấy URL trả về
        String imgurUrl = null;
        if (avatarPath != null && !avatarPath.isEmpty()) {
            try {
                imgurUrl = ImgurUpload.uploadImage(new File(avatarPath)); // Tải ảnh lên Imgur và nhận URL
                currentUser.setAvatar(imgurUrl);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error uploading thumbnail image to Imgur.");
                return; // Dừng lại nếu không thể tải ảnh lên Imgur
            }
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
