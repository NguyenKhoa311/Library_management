package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.BookRepository;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.ThesisRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public abstract class DocumentsController implements Initializable {
    @FXML
    protected TableView<Document> docsTable;
    @FXML
    protected TableColumn<Document, Integer> idCol;
    @FXML
    protected TableColumn<Document, String> titleCol, authorCol, categoryCol, publisherCol, dateCol;
    @FXML
    protected ComboBox<String> docTypeBox;

    protected final BookRepository bookRepository = new BookRepository();
    protected final ThesisRepository thesisRepository = new ThesisRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        docTypeBox.getItems().addAll("Books", "Theses");
        docTypeBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            loadDocuments(newValue);
        });

        // set default to show books
        docTypeBox.getSelectionModel().select("Books");
        loadDocuments("Books");

        // handle double-clicked to show document details
        docsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetails();
            }
        });

        HandleOutsideClickListener();
    }

    // load documents base on type chosen from comboBox
    protected void loadDocuments(String docType) {
        ObservableList<Document> documents;
        if ("Books".equals(docType)) {
            documents = bookRepository.getAllBooks();
        } else {
            documents = thesisRepository.getAllTheses();
        }
        docsTable.setItems(documents);
    }

    // add listener to handle clicked outside table
    private void HandleOutsideClickListener() {
        docsTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getTarget() instanceof Node) {
                        Node target = (Node) event.getTarget();
                        while (target != null) {
                            if (target == docsTable) { // clicked on table view
                                return;
                            }
                            target = target.getParent();
                        }
                        docsTable.getSelectionModel().clearSelection(); // clicked outside tableview --> cancel selection
                    }
                });
            }
        });
    }

    protected void showDocumentDetails() {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/DocumentDetail.fxml"));
                Parent detailRoot = loader.load();

                // Get the controller and set the selected book
                DocumentDetailController controller = loader.getController();
                controller.setDocumentDetails(selectedDocument);

                // Create a new stage for the book detail window
                Stage detailStage = new Stage();
                detailStage.setTitle("Document Details");
                detailStage.setScene(new Scene(detailRoot));
                detailStage.initModality(Modality.APPLICATION_MODAL); // Make it a modal window
                detailStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
