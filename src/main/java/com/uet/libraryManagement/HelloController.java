package com.uet.libraryManagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label documentText;

    @FXML
    void OnDocumentsButton() {
        documentText.setText("Hello World!");
    }

}
