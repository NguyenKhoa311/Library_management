package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.Manager.SessionManager;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {
    @FXML
    private TextField searchBar;
    @FXML
    private VBox filtersPanel;
    @FXML
    private TextField titleFilter;
    @FXML
    private ChoiceBox<String> statusChoice;
    @FXML
    private DatePicker borrowStart, borrowEnd, dueStart, dueEnd, returnStart, returnEnd;
    @FXML
    private TableView<BorrowHistory> historyTable;
    @FXML
    private TableColumn<BorrowHistory, String> noCol, titleCol, borrowDateCol, dueDateCol, returnDateCol, statusCol;
    @FXML
    private ComboBox<String> docTypeBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

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

        statusChoice.getItems().addAll("Borrowed", "Overdue", "Returned");
        docTypeBox.getItems().addAll("Books", "Theses");
        docTypeBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            loadHistory(SessionManager.getInstance().getUser().getId(), newValue);
        });

        docTypeBox.getSelectionModel().select("Books");
        historyTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetails();
            }
        });
    }

    private void showDocumentDetails() {
        BorrowHistory borrowHistory = historyTable.getSelectionModel().getSelectedItem();
        int userId = SessionManager.getInstance().getUser().getId();
        int borrow_id = borrowHistory.getId();
        String docType = docTypeBox.getValue();
        Document document = BorrowRepository.getInstance().getDocument(userId, borrow_id, docType);
        if (document != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentDetail.fxml"));
                Parent detailRoot = loader.load();

                // Get the controller and set the selected book
                DocumentDetailController controller = loader.getController();
                controller.setDocumentDetails(document);

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

    // Load borrowing history from the database
    public void loadHistory(int currentUserId, String docType) {
        ObservableList<BorrowHistory> historyList = FXCollections.observableArrayList(
                BorrowRepository.getInstance().getAllHistoryByUserId(currentUserId, docType)
        );
        historyTable.setItems(historyList);
    }

    @FXML
    private void returnDoc() {
        BorrowHistory borrowHistory = historyTable.getSelectionModel().getSelectedItem();
        if (borrowHistory != null) {
            // check if already returned
            String status = borrowHistory.getStatus();
            if ("returned".equalsIgnoreCase(status)) {
                showAlert("This document has already been returned!");
                return;
            }

            int borrowId = borrowHistory.getId();
            // Update the return date and status in the borrow_history table
            String query = "UPDATE borrow_history SET return_date = CURRENT_DATE, status = 'returned' WHERE id = ?";
            // Execute the update
            ConnectJDBC.executeUpdate(query, borrowId);
            showAlert("The document has been successfully returned!");
            loadHistory(SessionManager.getInstance().getUser().getId(), docTypeBox.getValue());
        } else {
            // Show a warning if no item is selected
            showAlert("Please select a document from the history to return!");
        }
    }

    // Handle search functionality

    public void handleSearchAction() {
        String searchTerm = searchBar.getText();
        if (searchTerm != null && !searchTerm.isEmpty()) {
            int currentUserId = SessionManager.getInstance().getUser().getId();
            String docType = docTypeBox.getValue();

            // Load history with search term
            ObservableList<BorrowHistory> historyList = FXCollections.observableArrayList(
                    BorrowRepository.getInstance().getAllHistoryByTitle(currentUserId, docType, searchTerm)
            );
            historyTable.setItems(historyList);
        } else {
            // If search is cleared, reload full history
            loadHistory(SessionManager.getInstance().getUser().getId(), docTypeBox.getValue());
        }
    }

    public void clearSearchTerm() {
        searchBar.clear();
        loadHistory(SessionManager.getInstance().getUser().getId() ,docTypeBox.getValue());
    }

    public void toggleFilters() {
        filtersPanel.setVisible(!filtersPanel.isVisible());
        if (filtersPanel.isVisible()) {
            filtersPanel.toFront();
        }
    }

    public void applyFilters() {
        int currentUserId = SessionManager.getInstance().getUser().getId();
        String docType = docTypeBox.getValue();
        boolean isBook = "Books".equalsIgnoreCase(docType);

        // Start building the query with the initial structure
        String query = "SELECT borrow_history.id, ";
        query += isBook ? "books.title AS docTitle" : "theses.title AS docTitle";
        query += ", borrow_date, due_date, return_date, status FROM borrow_history ";
        query += "JOIN " + (isBook ? "books" : "theses") + " ON borrow_history.doc_id = "
                + ("Books".equalsIgnoreCase(docType) ? "books.id" : "theses.id") + " WHERE borrow_history.user_id = ?"
                + " AND doc_type = " + (isBook ? "'book'" : "'thesis'");

        // List of parameters to pass to the query
        List<Object> params = new ArrayList<>();
        params.add(currentUserId);

        // Add filters to the query if they are set
        if (titleFilter.getText() != null && !titleFilter.getText().isEmpty()) {
            query += " AND " + (isBook ? "books.title" : "theses.title") + " LIKE ?";
            params.add("%" + titleFilter.getText() + "%");
        }
        if (statusChoice.getValue() != null) {
            query += " AND status = ?";
            params.add(statusChoice.getValue());
        }
        if (borrowStart.getValue() != null) {
            query += " AND borrow_date >= ?";
            params.add(borrowStart.getValue());
        }
        if (borrowEnd.getValue() != null) {
            query += " AND borrow_date <= ?";
            params.add(borrowEnd.getValue());
        }
        if (dueStart.getValue() != null) {
            query += " AND due_date >= ?";
            params.add(dueStart.getValue());
        }
        if (dueEnd.getValue() != null) {
            query += " AND due_date <= ?";
            params.add(dueEnd.getValue());
        }
        if (returnStart.getValue() != null) {
            query += " AND return_date >= ?";
            params.add(returnStart.getValue());
        }
        if (returnEnd.getValue() != null) {
            query += " AND return_date <= ?";
            params.add(returnEnd.getValue());
        }

        // Execute the query with parameters and update the table
        ObservableList<BorrowHistory> historyList = FXCollections.observableArrayList(
                BorrowRepository.getInstance().getFilteredHistory(query, params.toArray())
        );
        historyTable.setItems(historyList);
        filtersPanel.setVisible(false);
    }

    public void clearFilters() {
        titleFilter.clear();
        statusChoice.getItems().clear();
        borrowStart.setValue(null);
        borrowEnd.setValue(null);
        dueStart.setValue(null);
        dueEnd.setValue(null);
        returnStart.setValue(null);
        returnEnd.setValue(null);
        loadHistory(SessionManager.getInstance().getUser().getId(), docTypeBox.getValue());
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}