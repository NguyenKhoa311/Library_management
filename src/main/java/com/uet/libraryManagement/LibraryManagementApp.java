package com.uet.libraryManagement;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LibraryManagementApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().setScene("Login.fxml");
        //SceneManager.getInstance().setScene("UserMenu.fxml");

        stage.setOnCloseRequest(event -> {
            event.consume();
            logout(stage);
        });
    }

    public void logout(Stage stage) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You're about to logout!");
        alert.setContentText("Are you sure you want to logout?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("Logged out successfully");
            stage.close();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}