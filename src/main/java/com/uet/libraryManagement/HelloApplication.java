package com.uet.libraryManagement;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SceneManager.getInstance().setStage(stage);
        SceneManager.getInstance().setScene("hello-view.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}