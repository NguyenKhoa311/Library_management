package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.BookRepository;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.ThesisRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DocumentsController implements Initializable {

    @FXML
    private TableView<Document> docsTable;
    @FXML
    private TableColumn<Document, Integer> idCol;
    @FXML
    private TableColumn<Document, String> titleCol;
    @FXML
    private TableColumn<Document, String> authorCol;
    @FXML
    private TableColumn<Document, String> genreCol;
    @FXML
    private TableColumn<Document, String> publisherCol;
    @FXML
    private TableColumn<Document, String> dateCol;

    @FXML
    private ComboBox<String> docTypeBox;

    private final BookRepository bookRepository = new BookRepository();
    private final ThesisRepository thesisRepository = new ThesisRepository();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        docTypeBox.getItems().addAll("Books", "Theses");
        docTypeBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            loadDocuments(newValue);
        });

        // set default to show books
        docTypeBox.getSelectionModel().select("Books");
        loadDocuments("Books");

        // Set up a double-click event to show book details
        docsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetails();
            }
        });
    }

    // load documents base on combobox chosen item
    private void loadDocuments(String docType) {
        ObservableList<Document> documents;
        if ("Books".equals(docType)) {
            documents = bookRepository.getAllBooks();
        } else {
            documents = thesisRepository.getAllTheses();
        }
        docsTable.setItems(documents);
    }

    @FXML
    private void showDocumentDetails() {
        Document selectedBook = docsTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/DocumentDetail.fxml"));
                Parent detailRoot = loader.load();

                // Get the controller and set the selected book
                DocumentDetailController controller = loader.getController();
                controller.setDocumentDetails(selectedBook);

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
