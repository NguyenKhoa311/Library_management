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

    private final UserRepository userRepository = new UserRepository();


    @FXML
    public void initialize() {

    }

    public boolean validateLogin() {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        // Kiểm tra thông tin đăng nhập (ở đây giả định tài khoản hợp lệ là admin/password)
        //return "admin".equals(username) && "1".equals(password);
        return true;
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
        User user = userRepository.validateUser(username, password);

        if (user != null && user.getRole().equals("user")) {
            // Successful login, proceed to the main application scene
            SessionManager.getInstance().setUser(user);
            try {
                SceneManager.getInstance().setScene("UserMenu.fxml");
                SceneManager.getInstance().setSubScene("Home.fxml");
            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Failed to load the application.");
            }
        } else {
            // Failed login
            messageLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void handleRegister() throws IOException {
        SceneManager.getInstance().setLoginScene("Register.fxml");
    }
}
