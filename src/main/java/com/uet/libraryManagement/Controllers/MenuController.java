package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.SceneManager;
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
        SceneManager.getInstance().setSubScene("Documents.fxml");
    }

    @FXML
    void Issue() {
        SceneManager.getInstance().setSubScene("IssueBook.fxml");
    }

    @FXML
    void Manage() {
        SceneManager.getInstance().setSubScene("ManageDocuments.fxml");
    }

}
