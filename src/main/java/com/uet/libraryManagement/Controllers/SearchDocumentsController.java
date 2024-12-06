package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.APIService.BookAPI;
import com.uet.libraryManagement.APIService.Volume;
import com.uet.libraryManagement.APIService.VolumeInfo;
import com.uet.libraryManagement.APIService.IndustryIdentifier;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
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
        docTypeBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            docsTable.getItems().clear();
        });

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
                if (docsTable.getSelectionModel().getSelectedItem() != null) {
                    showDocumentDetails();
                }
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
        String docType = docTypeBox.getValue();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            Task<ObservableList<Document>> searchTask = new Task<>() {
                @Override
                protected ObservableList<Document> call() throws Exception {
                    List<Volume> volumes = BookAPI.searchVolumes(searchTerm, docType);
                    return convertVolumesToDocuments(volumes);
                }
            };

            TaskManager.runTask(searchTask, () -> {
                ObservableList<Document> documents = searchTask.getValue();
                if (documents.isEmpty()) {
                    showAlert("No documents found for the search term.");
                } else {
                    docsTable.setItems(documents);
                }
                saveData();
            }, () -> {
                showAlert("Failed to load documents.");
            });
        } else {
            // Handle case for empty search term
            showAlert("Please enter a search term.");
        }
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
        filtersPanel.setVisible(false);
        Task<ObservableList<Document>> filterTask = new Task<ObservableList<Document>>() {
            @Override
            protected ObservableList<Document> call() throws Exception {
                StringBuilder searchTerm = new StringBuilder();
                String docType = docTypeBox.getValue();

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

                // Search for volumes based on the filters
                List<Volume> volumes = BookAPI.searchVolumes(searchTerm.toString(), docType);
                // Convert the volumes to document objects
                return convertVolumesToDocuments(volumes);
            }
        };

        TaskManager.runTask(filterTask,
                () -> {
                    ObservableList<Document> documents = filterTask.getValue();
                    if (documents.isEmpty()) {
                        showAlert("No documents found for the search term.");
                    } else {
                        docsTable.setItems(documents);
                    }
                    saveData();
                },
                () -> {
                    showAlert("An error occurred while applying the filters.");
                });
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/SearchDocumentDetail.fxml"));
                Parent detailRoot = loader.load();

                // Get the controller and set the selected book
                SearchDocumentDetailController controller = loader.getController();
                controller.setDocumentDetails(selectedDocument);

                Scene detailScene = new Scene(detailRoot);
                detailScene.getStylesheets().add(SceneManager.getInstance().get_css());

                // Create a new stage for the book detail window
                Stage detailStage = new Stage();
                detailStage.setResizable(false);
                String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
                Image icon = new Image(icon_url);
                detailStage.getIcons().add(icon);
                detailStage.setTitle("Document Details");
                detailStage.setScene(detailScene);
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

        int quantity = 0;
        while (quantity <= 0) {
            TextInputDialog quantityDialog = new TextInputDialog();
            quantityDialog.setTitle("Enter Quantity");
            quantityDialog.setHeaderText("Add Document Quantity");
            quantityDialog.setContentText("Please enter the quantity:");

            // Show the dialog and capture the result
            String quantityInput = quantityDialog.showAndWait().orElse("");

            if (quantityInput.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Quantity cannot be empty.");
                alert.showAndWait();
            } else {
                try {
                    quantity = Integer.parseInt(quantityInput);
                    if (quantity <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Please enter a valid positive quantity.");
                    alert.showAndWait();
                }
            }
        }

        if (docTypeBox.getValue().equals("Books")) {
            BookRepository.getInstance().create(selectedDocument, quantity);
        } else {
            ThesisRepository.getInstance().create(selectedDocument, quantity);
        }
        showAlert("Document Added Successfully!");
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
