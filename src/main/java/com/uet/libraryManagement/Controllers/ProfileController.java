package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.SessionManager;
import com.uet.libraryManagement.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ProfileController {
    @FXML private ImageView profileImage;
    @FXML private Label userName;
    @FXML private Label usernameLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;

    private final User currentUser = SessionManager.getInstance().getUser();

    public void initialize() {
        loadUserInfo();
    }

    public void handleEditProfile(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/EditProfile.fxml"));
            Parent editProfileRoot = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Edit Profile");
            stage.setScene(new Scene(editProfileRoot));
            stage.showAndWait(); // Wait for the window to close before continuing

            // Update profile info if the user made changes
            loadUserInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePassword(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/ChangePassword.fxml"));
            Parent changePasswordRoot = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Change Password");
            stage.setScene(new Scene(changePasswordRoot));
            stage.showAndWait(); // Wait for the window to close before continuing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfo() {
        userName.setText(currentUser.getUsername());
        usernameLabel.setText("Full Name: " + ((currentUser.getFullName() == null) ? "N/A" : currentUser.getFullName()));
        birthdayLabel.setText("Date of birth: " + ((currentUser.getBirthday() == null) ? "N/A" : currentUser.getBirthday()));
        phoneLabel.setText("Phone: " + ((currentUser.getPhone() == null) ? "N/A" : currentUser.getPhone()));
        emailLabel.setText("Email: " + ((currentUser.getEmail() == null) ? "N/A" : currentUser.getEmail()));

        if (currentUser.getAvatarUrl() != null && !currentUser.getAvatarUrl().isEmpty()) {
            File file = new File(currentUser.getAvatarUrl());
            Image image = new Image(file.toURI().toString(), true);
            profileImage.setImage(image);
        }
    }
}
