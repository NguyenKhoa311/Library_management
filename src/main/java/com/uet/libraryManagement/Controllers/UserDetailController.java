package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class UserDetailController {
    @FXML private Label usernameLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label phoneLabel;
    @FXML private Label emailLabel;
    @FXML private ImageView userAva;
    @FXML private Label userNameLabel;

    protected void setUserDetails(User user) {
        userNameLabel.setText("User: " + user.getUsername());
        usernameLabel.setText("Full Name: " + ((user.getFullName() == null) ? "N/A" : user.getFullName()));
        birthdayLabel.setText("Date of birth: " + ((user.getBirthday() == null) ? "N/A" : user.getBirthday()));
        phoneLabel.setText("Phone: " + ((user.getPhone() == null) ? "N/A" : user.getPhone()));
        emailLabel.setText("Email: " + ((user.getEmail() == null) ? "N/A" : user.getEmail()));

        if (user.getAvatar() != null && !user.getAvatar().isEmpty()) {
            Image image = new Image(user.getAvatar(), true);
            userAva.setImage(image);
        }
    }
}
