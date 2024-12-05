package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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

    public void handleEditProfile() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/EditProfile.fxml"));
            Parent editProfileRoot = loader.load();

            SceneManager.getInstance().setDL_Mode(SceneManager.getInstance().get_isLight());

            Scene detailScene = new Scene(editProfileRoot);

            detailScene.getStylesheets().add(SceneManager.getInstance().get_css());

            Stage stage = new Stage();
            stage.setResizable(false);
            String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
            Image icon = new Image(icon_url);
            stage.getIcons().add(icon);
            stage.setTitle("Edit Profile");
            stage.setScene(detailScene);
            stage.showAndWait(); // Wait for the window to close before continuing

            // Update profile info if the user made changes
            loadUserInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/ChangePassword.fxml"));
            Parent changePasswordRoot = loader.load();

            Scene detailScene = new Scene(changePasswordRoot);

            detailScene.getStylesheets().add(SceneManager.getInstance().get_css());

            Stage stage = new Stage();
            stage.setResizable(false);
            String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
            Image icon = new Image(icon_url);
            stage.getIcons().add(icon);
            stage.setTitle("Change Password");
            stage.setScene(detailScene);
            stage.showAndWait(); // Wait for the window to close before continuing
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfo() {
        userName.setText(currentUser.getUsername());
        usernameLabel.setText(((currentUser.getFullName() == null) ? "N/A" : currentUser.getFullName()));
        birthdayLabel.setText(((currentUser.getBirthday() == null) ? "N/A" : currentUser.getBirthday()));
        phoneLabel.setText(((currentUser.getPhone() == null) ? "N/A" : currentUser.getPhone()));
        emailLabel.setText(((currentUser.getEmail() == null) ? "N/A" : currentUser.getEmail()));

        if (currentUser.getAvatar() != null && !currentUser.getAvatar().isEmpty()) {
            Image image = new Image(currentUser.getAvatar(), true);
            profileImage.setImage(image);
        }
    }
}
