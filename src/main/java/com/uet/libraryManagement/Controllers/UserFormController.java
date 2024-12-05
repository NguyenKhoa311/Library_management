package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.APIService.ImgurUpload;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.UserRepository;
import com.uet.libraryManagement.User;
import javafx.concurrent.Task;
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

        Task<Void> saveUserTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Nếu thumbnail đã được chọn, tải lên Imgur và lấy URL trả về
                if (avatarPath != null && !avatarPath.isEmpty()) {
                    try {
                        String imgurUrl = ImgurUpload.uploadImage(new File(avatarPath));
                        currentUser.setAvatar(imgurUrl); // Set avatar URL sau khi tải lên thành công
                    } catch (IOException e) {
                        throw new RuntimeException("Error uploading thumbnail image to Imgur.", e);
                    }
                }
                // Cập nhật thông tin hồ sơ người dùng
                UserRepository.getInstance().updateProfile(currentUser);
                return null;
            }
        };

        // Sử dụng TaskManager để chạy task
        TaskManager.runTask(
                saveUserTask,
                () -> { // On Success
                    showAlert("Profile changed successfully.");
                    closeForm(); // Đóng form sau khi lưu thành công
                },
                () -> { // On Failure
                    showAlert("An error occurred while saving the profile: " + saveUserTask.getException().getMessage());
                }
        );
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
