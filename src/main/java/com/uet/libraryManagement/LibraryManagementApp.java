package com.uet.libraryManagement;

import com.uet.libraryManagement.Controllers.MenuController;
import com.uet.libraryManagement.Manager.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;
import java.io.IOException;

public class LibraryManagementApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().setLoginScene("FXML/Login.fxml");

        stage.setOnCloseRequest(event -> {
            event.consume();
            MenuController.close(stage);
        });
    }

    public static void main(String[] args) {
        launch();
    }
}