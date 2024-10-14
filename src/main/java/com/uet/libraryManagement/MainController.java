package com.uet.libraryManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    void Documents(ActionEvent event) throws IOException {
        SceneManager.getInstance().setScene("hello-view.fxml");
    }

    @FXML
    void Issue(ActionEvent event) {

    }

    @FXML
    void Manage(ActionEvent event) {

    }

}
