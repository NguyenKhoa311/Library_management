package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.DocumentRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;


public class AdminDocumentsController extends DocumentsController {
    @FXML
    private void deleteDoc() {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        String docType = docTypeBox.getValue();

        if (selectedDocument != null) {
            if (BookRepository.getInstance().isBorrowed(selectedDocument.getId(), docType)) {
                showAlert("This document cannot be deleted because it is currently borrowed!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this document?", ButtonType.YES, ButtonType.NO);
            confirmAlert.setTitle("Confirm Deletion");
            confirmAlert.setHeaderText("Delete Document");

            if (confirmAlert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
                Task<Void> deleteTask = new Task<>() {
                    @Override
                    protected Void call() throws Exception {
                        if (selectedDocument instanceof Book) {
                            BookRepository.getInstance().delete(selectedDocument);
                        } else if (selectedDocument instanceof Thesis) {
                            ThesisRepository.getInstance().delete(selectedDocument);
                        }
                        return null;
                    }
                };

                Runnable onSuccess = () -> {
                    loadDocuments(docType);
//                    showAlert("Document deleted successfully.");
                };

                Runnable onFailure = () -> {
                    Throwable ex = deleteTask.getException();
                    if (ex != null) ex.printStackTrace(); // Log lỗi để debug
                    showAlert("Failed to delete the document.");
                };

                TaskManager.runTask(deleteTask, onSuccess, onFailure);
            }
        } else {
            showAlert("Please select a document to delete.");
        }
    }

    @FXML
    private void editDoc() {
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

                Scene detailScene = new Scene(formRoot);


                detailScene.getStylesheets().add(SceneManager.getInstance().get_css());

                // Create a new stage for the edit window
                Stage formStage = new Stage();
                formStage.setResizable(false);
                String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
                Image icon = new Image(icon_url);
                formStage.getIcons().add(icon);
                formStage.setTitle("Edit Document");
                formStage.setScene(detailScene);
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
    private void addDoc() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentForm.fxml"));
            Parent detailRoot = loader.load();

            // Get the controller and set the mode to add
            DocumentFormController controller = loader.getController();
            controller.setMode("add");
            controller.setDocType(docTypeBox.getValue());

            Scene detailScene = new Scene(detailRoot);

            detailScene.getStylesheets().add(SceneManager.getInstance().get_css());
            
            // Create a new stage for the add document window
            Stage formStage = new Stage();
            formStage.setResizable(false);
            String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
            Image icon = new Image(icon_url);
            formStage.getIcons().add(icon);
            formStage.setTitle("Add Document");
            formStage.setScene(detailScene);
            formStage.initModality(Modality.APPLICATION_MODAL);
            formStage.showAndWait();

            // Refresh the document list after adding a new document
            loadDocuments(docTypeBox.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
