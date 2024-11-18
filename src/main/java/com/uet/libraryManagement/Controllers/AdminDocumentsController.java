package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class AdminDocumentsController extends DocumentsController {
    @FXML
    private void deleteDoc(ActionEvent actionEvent) {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this document?", ButtonType.YES, ButtonType.NO);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Document");

            if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                if (selectedDocument instanceof Book) {
                    BookRepository.getInstance().delete(selectedDocument);
                } else if (selectedDocument instanceof Thesis) {
                    ThesisRepository.getInstance().delete(selectedDocument);
                }
                // Refresh the document list after deletion
                loadDocuments(docTypeBox.getValue());
            }
        } else {
            showAlert("Please select a document to delete.");
        }
    }

    @FXML
    private void editDoc(ActionEvent actionEvent) {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentForm.fxml"));
                Parent formRoot = loader.load();

                // Get the controller and set the mode to edit
                DocumentFormController controller = loader.getController();
                controller.setMode("edit");
                controller.setDocument(selectedDocument); // Pass the selected document for editing
                controller.setDocType(docTypeBox.getValue());

                // Create a new stage for the edit window
                Stage formStage = new Stage();
                formStage.setTitle("Edit Document");
                formStage.setScene(new Scene(formRoot));
                formStage.initModality(Modality.APPLICATION_MODAL);
                formStage.showAndWait();

                // Refresh the document list after editing
                loadDocuments(docTypeBox.getValue());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a document to edit.");
        }
    }

    @FXML
    private void addDoc(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentForm.fxml"));
            Parent formRoot = loader.load();

            // Get the controller and set the mode to add
            DocumentFormController controller = loader.getController();
            controller.setMode("add");
            controller.setDocType(docTypeBox.getValue());

            // Create a new stage for the add document window
            Stage formStage = new Stage();
            formStage.setTitle("Add Document");
            formStage.setScene(new Scene(formRoot));
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.showAndWait();

            // Refresh the document list after adding a new document
            loadDocuments(docTypeBox.getValue());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to show alerts
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
