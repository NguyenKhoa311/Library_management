package com.uet.libraryManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    @FXML
    private AnchorPane contentPane;

    private static SceneManager instance;
    private static Stage rootStage;
    private boolean isLight = true;

    private Scene scene;

    private SceneManager() {}

    public void setStage(Stage stage) {
        SceneManager.rootStage = stage;
    }

    public void setLoginScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));

        scene = new Scene(fxmlLoader.load(), 300, 400);

        setDL_Mode(isLight);

        rootStage.setTitle("UET Library");
        rootStage.setScene(scene);
        rootStage.show();
    }

    public void setScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));

        scene = new Scene(fxmlLoader.load(), 700, 500);

        setDL_Mode(isLight);

        rootStage.setTitle("UET Library");
        rootStage.setScene(scene);
        rootStage.show();
    }

    public void setDL_Mode(boolean state) {
        isLight = state;
        String css;
        if (isLight) {
            css = Objects.requireNonNull(this.getClass().getResource("CSS/Light-mode.css")).toExternalForm();
            scene.getStylesheets().clear();
        } else {
            css = Objects.requireNonNull(this.getClass().getResource("CSS/Dark-mode.css")).toExternalForm();
            scene.getStylesheets().clear();
        }

        scene.getStylesheets().add(css);
    }

    public static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return instance;
    }

    public void setContentPane(AnchorPane contentPane) {
        this.contentPane = contentPane;
    }

    public void setSubScene(String sceneName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
            AnchorPane pageContent = loader.load();
            contentPane.getChildren().setAll(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
