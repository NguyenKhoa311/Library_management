package com.uet.libraryManagement.Managers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManager {

    @FXML
    private AnchorPane contentPane;

    private static SceneManager instance;
    private static Stage rootStage;
    private boolean isLight = true;
    private String css;
    private Scene scene;

    private SceneManager() {}

    /**
     * only use for JUnit Test.
     * @param sceneManagerMock instance to set.
     */
    public static void setInstance(SceneManager sceneManagerMock) {
        instance = sceneManagerMock;
    }

    public void setStage(Stage stage) {
        stage.setResizable(false);
        String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
        Image icon = new Image(icon_url);
        stage.getIcons().add(icon);
        SceneManager.rootStage = stage;
    }

    public void setLoginScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/" + sceneName));

        scene = new Scene(fxmlLoader.load(), 300, 400);

        setDL_Mode(isLight);

        rootStage.setTitle("UET Library");
        rootStage.setScene(scene);
        rootStage.show();
    }

    public void setScene(String sceneName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/" + sceneName));

        scene = new Scene(fxmlLoader.load(), 700, 500);

        setDL_Mode(isLight);

        rootStage.setTitle("UET Library");
        rootStage.setScene(scene);
        rootStage.show();
    }

    public void setDL_Mode(boolean state) {
        isLight = state;
        if (isLight) {
            css = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/CSS/Light-mode.css")).toExternalForm();
            scene.getStylesheets().clear();
        } else {
            css = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/CSS/Dark-mode.css")).toExternalForm();
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/" + sceneName));
            AnchorPane pageContent = loader.load();
            contentPane.getChildren().setAll(pageContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean get_isLight() {
        return isLight;
    }

    public String get_css() {
        return css;
    }
}
