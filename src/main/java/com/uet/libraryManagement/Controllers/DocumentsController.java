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
import javafx.scene.layout.VBox;
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
    protected TableColumn<Document, String> titleCol, authorCol, categoryCol, publisherCol, dateCol, isbn10Col, isbn13Col;
    @FXML
    protected ComboBox<String> docTypeBox;
    @FXML
    protected TextField titleFilter, authorFilter, categoryFilter, isbn10Filter, isbn13Filter, searchBar;
    @FXML
    protected DatePicker startDateFilter, endDateFilter;
    @FXML
    protected VBox filtersPanel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        isbn10Col.setCellValueFactory(new PropertyValueFactory<>("isbn10"));
        isbn13Col.setCellValueFactory(new PropertyValueFactory<>("isbn13"));

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

        filtersPanel.setVisible(false);
        HandleOutsideClickListener();
    }

    // Toggle the visibility of the advanced filter panel
    @FXML
    private void toggleFilters() {
        filtersPanel.setVisible(!filtersPanel.isVisible());
        if (filtersPanel.isVisible()) {
            filtersPanel.toFront();
        }
    }

    // Handle search action with the text from search bar
    @FXML
    private void handleSearchAction() {
        String searchTerm = searchBar.getText();
        loadSearchedDocuments(searchTerm);
    }

    @FXML
    private void clearSearchTerm() {
        searchBar.clear();
        loadDocuments(docTypeBox.getValue());
    }

    // Apply filters based on user inputs
    @FXML
    private void applyFilters() {
        String title = titleFilter.getText();
        String author = authorFilter.getText();
        String category = categoryFilter.getText();
        String isbn10 = isbn10Filter.getText();
        String isbn13 = isbn13Filter.getText();
        String startYear = (startDateFilter.getValue() != null) ? String.valueOf(startDateFilter.getValue().getYear()) : null;
        String endYear = (endDateFilter.getValue() != null) ? String.valueOf(endDateFilter.getValue().getYear()) : null;
        filtersPanel.setVisible(false);
        loadDocuments(title, author, category, startYear, endYear, isbn10, isbn13);
    }

    // Clear all filters
    @FXML
    private void clearFilters() {
        titleFilter.clear();
        authorFilter.clear();
        categoryFilter.clear();
        isbn10Filter.clear();
        isbn13Filter.clear();
        startDateFilter.setValue(null);
        endDateFilter.setValue(null);
        loadDocuments(docTypeBox.getValue());
    }

    // load documents base on type chosen from comboBox
    protected void loadDocuments(String docType) {
        ObservableList<Document> documents;
        if ("Books".equals(docType)) {
            documents = BookRepository.getInstance().getAllBooks();
        } else {
            documents = ThesisRepository.getInstance().getAllTheses();
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

    private void loadDocuments(String title, String author, String category, String startYear, String endYear, String isbn10, String isbn13) {
        ObservableList<Document> documents;
        if (docTypeBox.getValue().equals("Books")) {
            documents = BookRepository.getInstance().getFilteredDocuments(title, author, category, startYear, endYear, isbn10, isbn13);
            docsTable.setItems(documents);
        } else {
            documents = ThesisRepository.getInstance().getFilteredDocuments(title, author, category, startYear, endYear, isbn10, isbn13);
        }
        docsTable.setItems(documents);
    }

    private void loadSearchedDocuments(String searchTerm) {
        ObservableList<Document> documents;
        if (docTypeBox.getValue().equals("Books")) {
            documents = BookRepository.getInstance().searchDocument(searchTerm);
            docsTable.setItems(documents);
        } else {
            documents = ThesisRepository.getInstance().searchDocument(searchTerm);
        }
        docsTable.setItems(documents);
    }
}
