package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.APIService.BookAPI;
import com.uet.libraryManagement.APIService.Volume;
import com.uet.libraryManagement.APIService.VolumeInfo;
import com.uet.libraryManagement.APIService.IndustryIdentifier;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import javafx.collections.FXCollections;
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
import java.util.List;
import java.util.ResourceBundle;

public class SearchDocumentsController implements Initializable {
    private static String savedSearchTerm;
    private static String savedDocType;
    private static ObservableList<Document> savedDocuments;

    @FXML
    protected TableView<Document> docsTable;
    @FXML
    protected TableColumn<Document, String> noCol, titleCol, authorCol, categoryCol, publisherCol, dateCol, isbn10Col, isbn13Col;
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
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("category"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        isbn10Col.setCellValueFactory(new PropertyValueFactory<>("isbn10"));
        isbn13Col.setCellValueFactory(new PropertyValueFactory<>("isbn13"));

        noCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(String.valueOf(getIndex() + 1));
                }
            }
        });

        docTypeBox.getItems().addAll("Books", "Theses");

        // set default to show books
        docTypeBox.getSelectionModel().select("Books");

        if (savedSearchTerm != null) {
            System.out.println("Restoring search term: " + savedSearchTerm);
            searchBar.setText(savedSearchTerm);
            docTypeBox.setValue(savedDocType);
            if (savedDocuments != null) {
                System.out.println("Restoring documents...");
                docsTable.setItems(savedDocuments);
            }
        } else {
            System.out.println("No saved search term found.");
        }

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
        if (searchTerm != null && !searchTerm.isEmpty()) {
            List<Volume> volumes = BookAPI.searchVolumes(searchTerm);
            ObservableList<Document> documents = convertVolumesToDocuments(volumes);

            if (documents.isEmpty()) {
                // Hiển thị thông báo nếu không có kết quả tìm kiếm
                showAlert("No documents found for the search term.");
            } else {
                docsTable.setItems(documents);
            }
        } else {
            // Handle case for empty search term
            showAlert("Please enter a search term.");
        }
        saveData();
    }

    private ObservableList<Document> convertVolumesToDocuments(List<Volume> volumes) {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        for (Volume volume : volumes) {
            VolumeInfo volumeInfo = volume.volumeInfo;
            String title = volumeInfo.title != null ? volumeInfo.title : "N/A";
            String authors = volumeInfo.authors != null ? String.join(", ", volumeInfo.authors) : "N/A";
            String publishedDate = volumeInfo.publishedDate != null ? volumeInfo.publishedDate : "N/A";
            String publisher = volumeInfo.publisher != null ? volumeInfo.publisher : "N/A";
            String description = volumeInfo.description != null ? volumeInfo.description : "N/A";
            String categories = volumeInfo.categories != null ? String.join(", ", volumeInfo.categories) : "N/A";
            String thumbnail = volumeInfo.imageLinks != null ? volumeInfo.imageLinks.thumbnail : "No Thumbnail";
            String isbn10 = "N/A";
            String isbn13 = "N/A";

            if (volumeInfo.industryIdentifiers != null) {
                for (IndustryIdentifier id : volumeInfo.industryIdentifiers) {
                    if ("ISBN_10".equals(id.type)) {
                        isbn10 = id.identifier;
                    } else if ("ISBN_13".equals(id.type)) {
                        isbn13 = id.identifier;
                    }
                }
            }

            Document document;
            if (docTypeBox.getValue().equals("Books")) {
                document = new Book(title, authors, publisher, description, publishedDate, categories, thumbnail, isbn10, isbn13);
            } else {
                document = new Thesis(title, authors, publisher, description, publishedDate, categories, thumbnail, isbn10, isbn13);
            }
            documents.add(document);
        }

        return documents;
    }

    @FXML
    private void clearSearchTerm() {
        searchBar.clear();
        docsTable.getItems().clear();
    }

    // Apply filters based on user inputs
    @FXML
    private void applyFilters() {
        StringBuilder searchTerm = new StringBuilder();

        // Append each filter to the query if it's not empty
        if (!titleFilter.getText().isEmpty()) {
            searchTerm.append("intitle:").append(titleFilter.getText()).append("+");
        }
        if (!authorFilter.getText().isEmpty()) {
            searchTerm.append("inauthor:").append(authorFilter.getText()).append("+");
        }
        if (!categoryFilter.getText().isEmpty()) {
            searchTerm.append("subject:").append(categoryFilter.getText()).append("+");
        }
        if (!isbn10Filter.getText().isEmpty()) {
            searchTerm.append("isbn:").append(isbn10Filter.getText()).append("+");
        }
        if (!isbn13Filter.getText().isEmpty()) {
            searchTerm.append("isbn:").append(isbn13Filter.getText()).append("+");
        }
        if (startDateFilter.getValue() != null) {
            searchTerm.append("after:").append(startDateFilter.getValue().getYear()).append("+");
        }
        if (endDateFilter.getValue() != null) {
            searchTerm.append("before:").append(endDateFilter.getValue().getYear()).append("+");
        }

        if (searchTerm.length() > 0 && searchTerm.charAt(searchTerm.length() - 1) == '+') {
            searchTerm.setLength(searchTerm.length() - 1);
        }

        List<Volume> volumes = BookAPI.searchVolumes(searchTerm.toString());
        ObservableList<Document> documents = convertVolumesToDocuments(volumes);
        filtersPanel.setVisible(false);

        if (documents.isEmpty()) {
            // Hiển thị thông báo nếu không có kết quả tìm kiếm
            showAlert("No documents found for the search term.");
        } else {
            docsTable.setItems(documents);
        }
        saveData();
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentDetail.fxml"));
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

    public void addDoc() {
        Document selectedDocument = docsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a document to add.");
            alert.showAndWait();
            return;
        }

        if (docTypeBox.getValue().equals("Books")) {
            BookRepository.getInstance().create(selectedDocument);
        } else {
            ThesisRepository.getInstance().create(selectedDocument);
        }
    }

    private void saveData() {
        // Save the current state before switching scenes
        System.out.println("Saving scene state...");
        System.out.println("Search term: " + searchBar.getText());
        System.out.println("Doc type: " + docTypeBox.getValue());
        System.out.println("Documents count: " + docsTable.getItems().size());
        savedSearchTerm = searchBar.getText();
        savedDocType = docTypeBox.getValue();
        savedDocuments = FXCollections.observableArrayList(docsTable.getItems());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
