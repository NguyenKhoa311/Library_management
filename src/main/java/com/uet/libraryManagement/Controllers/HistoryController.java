package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BorrowRepository;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private Integer selectedUserId = null;  // `null` means session user will be used

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
            loadHistory(selectedUserId != null ? selectedUserId : SessionManager.getInstance().getUser().getId(), newValue);
        });

        docTypeBox.getSelectionModel().select("Books");
        historyTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if (historyTable.getSelectionModel().getSelectedItem() != null) {
                    showDocumentDetails();
                }
            }
        });
        HandleOutsideClickListener();
    }

    // Setter method to set the selected user's ID and flag
    public void setSelectedUserId(int userId) {
        this.selectedUserId = userId;
    }

    private void showDocumentDetails() {
        BorrowHistory borrowHistory = historyTable.getSelectionModel().getSelectedItem();
        int userId = (selectedUserId != null) ? selectedUserId : SessionManager.getInstance().getUser().getId();
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
                controller.setDocument(document);
                controller.setDocType(docType);
                controller.loadComments(document.getId(), docType);

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

    // Load borrowing history from the database
    public void loadHistory(int currentUserId, String docType) {
        int userIdToUse = (selectedUserId != null) ? selectedUserId : currentUserId;

        Task<ObservableList<BorrowHistory>> task = new Task<>() {
            @Override
            protected ObservableList<BorrowHistory> call() throws Exception {
                return FXCollections.observableArrayList(
                        BorrowRepository.getInstance().getAllHistoryByUserId(userIdToUse, docType)
                );
            }
        };

        Runnable onSuccess = () -> historyTable.setItems(task.getValue());
        Runnable onFailure = () -> showAlert("Failed to load borrowing history.");

        TaskManager.runTask(task, onSuccess, onFailure);
    }

    @FXML
    private void returnDoc() {
        BorrowHistory borrowHistory = historyTable.getSelectionModel().getSelectedItem();
        if (borrowHistory == null) {
            showAlert("Please select a document from the history to return!");
            return;
        }

        // Check if the document has already been returned
        String status = borrowHistory.getStatus();
        if ("returned".equalsIgnoreCase(status)) {
            showAlert("This document has already been returned!");
            return;
        }

        int borrowId = borrowHistory.getId();
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                String getDocId = "SELECT doc_id, doc_type FROM borrow_history WHERE id = " + borrowId;
                try (ResultSet rs = ConnectJDBC.executeQuery(getDocId)) {
                    if (rs.next()) {
                        int docId = rs.getInt("doc_id");
                        String docType = rs.getString("doc_type");

                        // Update the return date and status in the borrow_history table
                        String updateReturnQuery = "UPDATE borrow_history SET return_date = CURRENT_DATE, status = 'returned' WHERE id = ?";
                        ConnectJDBC.executeUpdate(updateReturnQuery, borrowId);

                        // Increase the document quantity in the respective table
                        BorrowRepository.getInstance().increaseDocumentQuantity(docId, docType);
                    }
                }
                return null;
            }
        };

        Runnable onSuccess = () -> {
            showAlert("The document has been successfully returned!");
            loadHistory(SessionManager.getInstance().getUser().getId(), docTypeBox.getValue());
        };

        Runnable onFailure = () -> showAlert("An error occurred while returning the document.");

        TaskManager.runTask(task, onSuccess, onFailure);
    }

    // Handle search functionality
    public void handleSearchAction() {
        int userIdToUse = (selectedUserId != null) ? selectedUserId : SessionManager.getInstance().getUser().getId();
        String searchTerm = searchBar.getText();
        String docType = docTypeBox.getValue();

        Task<ObservableList<BorrowHistory>> searchTask = new Task<>() {
            @Override
            protected ObservableList<BorrowHistory> call() throws Exception {
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    // Load history with search term
                    return FXCollections.observableArrayList(
                            BorrowRepository.getInstance().getAllHistoryByTitle(userIdToUse, docType, searchTerm)
                    );
                } else {
                    // Reload full history if search term is empty
                    return FXCollections.observableArrayList(
                            BorrowRepository.getInstance().getAllHistoryByUserId(userIdToUse, docType)
                    );
                }
            }
        };

        Runnable onSuccess = () -> {
            historyTable.setItems(searchTask.getValue());
        };

        Runnable onFailure = () -> {
            showAlert("An error occurred while searching. Please try again.");
        };

        // Sử dụng TaskManager để chạy Task
        TaskManager.runTask(searchTask, onSuccess, onFailure);
    }

    public void clearSearchTerm() {
        int userIdToUse = (selectedUserId != null) ? selectedUserId : SessionManager.getInstance().getUser().getId();
        searchBar.clear();
        loadHistory(userIdToUse ,docTypeBox.getValue());
    }

    public void toggleFilters() {
        filtersPanel.setVisible(!filtersPanel.isVisible());
        if (filtersPanel.isVisible()) {
            filtersPanel.toFront();
        }
    }

    public void applyFilters() {
        filtersPanel.setVisible(false);
        String docType = docTypeBox.getValue();
        boolean isBook = "Books".equalsIgnoreCase(docType);
        int userIdToUse = (selectedUserId != null) ? selectedUserId : SessionManager.getInstance().getUser().getId();

        Task<ObservableList<BorrowHistory>> task = new Task<>() {
            @Override
            protected ObservableList<BorrowHistory> call() throws Exception {
                String query = "SELECT borrow_history.id, ";
                query += isBook ? "books.title AS docTitle" : "theses.title AS docTitle";
                query += ", borrow_date, due_date, return_date, status FROM borrow_history ";
                query += "JOIN " + (isBook ? "books" : "theses") + " ON borrow_history.doc_id = "
                        + ("Books".equalsIgnoreCase(docType) ? "books.id" : "theses.id") + " WHERE borrow_history.user_id = ?"
                        + " AND doc_type = " + (isBook ? "'book'" : "'thesis'");

                List<Object> params = new ArrayList<>();
                params.add(userIdToUse);

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

                return FXCollections.observableArrayList(
                        BorrowRepository.getInstance().getFilteredHistory(query, params.toArray())
                );
            }
        };

        Runnable onSuccess = () -> {
            historyTable.setItems(task.getValue());
        };
        Runnable onFailure = () -> showAlert("Failed to apply filters.");

        TaskManager.runTask(task, onSuccess, onFailure);
    }

    public void clearFilters() {
        int userIdToUse = (selectedUserId != null) ? selectedUserId : SessionManager.getInstance().getUser().getId();
        titleFilter.clear();
        statusChoice.getItems().clear();
        borrowStart.setValue(null);
        borrowEnd.setValue(null);
        dueStart.setValue(null);
        dueEnd.setValue(null);
        returnStart.setValue(null);
        returnEnd.setValue(null);
        loadHistory(userIdToUse, docTypeBox.getValue());
    }

    private void HandleOutsideClickListener() {
        historyTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getTarget() instanceof Node) {
                        Node target = (Node) event.getTarget();
                        while (target != null) {
                            if (target == historyTable) { // clicked on table view
                                return;
                            }
                            target = target.getParent();
                        }
                        historyTable.getSelectionModel().clearSelection(); // clicked outside tableview --> cancel selection
                    }
                });
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}