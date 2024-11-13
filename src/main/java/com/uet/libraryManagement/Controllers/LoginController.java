package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField username_textfield;
    @FXML
    private TextField password_textfield;
    @FXML
    public Button Login_button;

    @FXML
    public void initialize() {
        // Gán sự kiện cho nút đăng nhập
        Login_button.setOnAction(event -> handleLoginButtonAction());
    }

    public boolean validateLogin() {
        String username = username_textfield.getText();
        String password = password_textfield.getText();

        // Kiểm tra thông tin đăng nhập (ở đây giả định tài khoản hợp lệ là admin/password)
        //return "admin".equals(username) && "1".equals(password);
        return true;
    }

    // Phương thức xử lý sự kiện nút đăng nhập
    private void handleLoginButtonAction() {
        // Kiểm tra tài khoản đăng nhập
        String username = username_textfield.getText();
        String password = password_textfield.getText();
        // Giả định tài khoản hợp lệ (bạn có thể thêm kiểm tra xác thực tài khoản tại đây)
        if (validateLogin()) {
            try {
                SceneManager.getInstance().setScene("UserMenu.fxml");
                SceneManager.getInstance().setSubScene("Home.fxml");

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
            // Bạn có thể hiển thị thông báo lỗi cho người dùng ở đây
        }
    }
}
