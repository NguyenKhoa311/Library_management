package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.SceneManager;
import com.uet.libraryManagement.SessionManager;
import com.uet.libraryManagement.User;
import com.uet.libraryManagement.UserRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username_textfield;
    @FXML
    private TextField password_textfield;
    @FXML
    private Label messageLabel;

    @FXML
    public void initialize() {

    }

    // handle login
    @FXML
    private void handleLogin() {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        // Check if username and password fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please enter both username and password.");
            return;
        }

        // Authenticate the user
        User user = UserRepository.getInstance().validateUser(username, password);

        if (user != null && user.getRole().equals("user")) {
            // Successful login, proceed to the main application scene
            SessionManager.getInstance().setUser(user);
            try {
                SceneManager.getInstance().setScene("FXML/UserMenu.fxml");
                SceneManager.getInstance().setSubScene("FXML/Home.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Failed to load the application.");
            }
        } else if (user != null && user.getRole().equals("admin"))  {
            SessionManager.getInstance().setUser(user);
            try {
                SceneManager.getInstance().setScene("FXML/AdminMenu.fxml");
                SceneManager.getInstance().setSubScene("FXML/Home.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Failed to load the application.");
            }
        }
        else {
            // Failed login
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        SceneManager.getInstance().setLoginScene("FXML/Register.fxml");
    }
}
