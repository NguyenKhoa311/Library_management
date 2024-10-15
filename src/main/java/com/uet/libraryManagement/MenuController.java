package com.uet.libraryManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MenuController {
    @FXML
    private AnchorPane contentPane;

    @FXML
    private void initialize() throws IOException {
        SceneManager.getInstance().setContentPane(contentPane);
    }

    @FXML
    void Documents() throws IOException {
        SceneManager.getInstance().setSubScene("hello-view.fxml");
    }

    @FXML
    void Issue() {

    }

    @FXML
    void Manage() {

    }

}
