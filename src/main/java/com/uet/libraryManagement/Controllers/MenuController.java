package com.uet.libraryManagement.Controllers;

import com.jfoenix.controls.JFXButton;
import com.uet.libraryManagement.SceneManager;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class MenuController {
    public Label Title_page;
    private boolean isLight = false;
    public JFXButton DL_mode;
    public ImageView DL_image;
    @FXML
    private AnchorPane overlayPane;

    private boolean isMenuVisible = false;
    public VBox menuBox;
    @FXML
    private AnchorPane contentPane;

    @FXML
    private void initialize() throws IOException {
        SceneManager.getInstance().setContentPane(contentPane);
        menuBox.setTranslateX(-menuBox.getPrefWidth());
        Title_page.setText("Welcome to UET Library Management");
    }

    @FXML
    private void exit() {
        // create alert window.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setHeaderText("Do you really want to exit?");
        alert.setContentText("Choose OK to exit the application.");

        // show confirmation window and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        // check user response
        if (((Optional<?>) result).isPresent() && result.get() == ButtonType.OK) {
            Platform.exit(); // exit
        }
    }

    public static void close(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit confirmation");
        alert.setHeaderText("Do you really want to exit?");
        alert.setContentText("Choose OK to exit the application.");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Logged out successfully");
            stage.close();
        }
    }

    public void DL_mode(ActionEvent event) {
        if (isLight) {
            SceneManager.getInstance().setDL_Mode(isLight);
            DL_image.setImage(new Image(Objects.requireNonNull(getClass().getResource("/com/uet/libraryManagement/icons/dark.png")).toExternalForm()));
            isLight = false;
        } else {
            SceneManager.getInstance().setDL_Mode(isLight);
            Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/uet/libraryManagement/icons/light.png")).toExternalForm());
            DL_image.setImage(image);
            isLight = true;
        }
    }

    @FXML
    private void Home() {
        SceneManager.getInstance().setSubScene("Home.fxml");
        Title_page.setText("Home");
        hideMenuBox();
    }

    @FXML
    private void Profile() {
        SceneManager.getInstance().setSubScene("Profile.fxml");
        Title_page.setText("Profile");
        hideMenuBox();
    }

    @FXML
    private void Documents() throws IOException {
        SceneManager.getInstance().setSubScene("UserDocuments.fxml");
        Title_page.setText("Documents");
        hideMenuBox();
    }

    @FXML
    private void Issue() {
        SceneManager.getInstance().setSubScene("IssueBook.fxml");
        Title_page.setText("Issue");
        hideMenuBox();
    }

    @FXML
    private void Manage() {
        SceneManager.getInstance().setSubScene("ManageDocuments.fxml");
        Title_page.setText("Manage");
        hideMenuBox();
    }

    @FXML
    private void History() {
        SceneManager.getInstance().setSubScene("History.fxml");
        Title_page.setText("History");
        hideMenuBox();
    }

    @FXML
    private void LogOut() {
        SceneManager.getInstance().setSubScene("Logout.fxml");
        Title_page.setText("Logout");
        hideMenuBox();
    }

    @FXML
    private void menuButton() {
        if (isMenuVisible) {
            hideMenuBox();
        } else {
            showMenuBox();
        }
    }

    private void showMenuBox() {
        // Hiện overlayPane với độ mờ
        overlayPane.setVisible(true);
        overlayPane.toFront();
        menuBox.toFront();

        // Thiết lập hiệu ứng mờ dần cho overlayPane
        FadeTransition fadeInOverlay = new FadeTransition(Duration.millis(300), overlayPane);
        fadeInOverlay.setFromValue(0);     // bắt đầu từ trong suốt
        fadeInOverlay.setToValue(0.5);     // kết thúc với độ mờ 50%
        fadeInOverlay.play();

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), menuBox);
        transition.setFromX(-menuBox.getPrefWidth());  // start from left-most position
        transition.setToX(0);                            // end at x = 0
        transition.setOnFinished(event -> isMenuVisible = true);
        transition.play();
        menuBox.setVisible(true);  // Hiển thị menu box

        // Thêm sự kiện bấm chuột để ẩn menuBox khi bấm vào overlayPane
        overlayPane.setOnMouseClicked(event -> hideMenuBox());
    }

    private void hideMenuBox() {
        // Ẩn overlayPane với hiệu ứng mờ dần
        FadeTransition fadeOutOverlay = new FadeTransition(Duration.millis(200), overlayPane);
        fadeOutOverlay.setFromValue(0.5);   // bắt đầu với độ mờ 50%
        fadeOutOverlay.setToValue(0);       // kết thúc ở trong suốt
        fadeOutOverlay.setOnFinished(event -> overlayPane.setVisible(false));
        fadeOutOverlay.play();

        TranslateTransition transition = new TranslateTransition(Duration.millis(200), menuBox);
        transition.setFromX(0);                          // start from x = 0
        transition.setToX(-menuBox.getPrefWidth());     // end at left-most position
        transition.setOnFinished(event -> {
            menuBox.setVisible(false); // hide menu box
            overlayPane.toBack();
            menuBox.toBack();
            isMenuVisible = false;
        });
        transition.play();
    }
}
