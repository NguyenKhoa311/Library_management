package com.uet.libraryManagement.Controllers;

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
