package com.uet.libraryManagement;

import com.uet.libraryManagement.Controllers.MenuController;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.TaskManager;
import javafx.application.Application;
import javafx.scene.image.Image;
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

    @Override
    public void stop() throws Exception {
        // Tắt ExecutorService trước khi thoát ứng dụng
        TaskManager.shutdown();
        super.stop(); // Gọi phương thức stop() của Application
    }

    public static void main(String[] args) {
        launch();
    }
}