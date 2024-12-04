package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.BorrowHistory;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class UserDashboardController {
    @FXML
    private ImageView currentBookThumbnail;
    @FXML
    private ImageView currentRecThumbnail;
    @FXML
    private Label borrowedCount;
    @FXML
    private Label dueSoonCount;
    @FXML
    private Label overdueCount;
    @FXML
    private Label historyCount;
    @FXML
    private Circle borrowedCircle;
    @FXML
    private Circle dueSoonCircle;
    @FXML
    private Circle overdueCircle;
    @FXML
    private TableView<BorrowHistory> recentBorrowsTable;
    @FXML
    private TableColumn<BorrowHistory, String> noCol;
    @FXML
    private TableColumn<BorrowHistory, String> docTitleColumn;
    @FXML
    private TableColumn<BorrowHistory, LocalDate> borrowDateColumn;
    @FXML
    private TableColumn<BorrowHistory, LocalDate> dueDateColumn;
    @FXML
    private TableColumn<BorrowHistory, String> statusColumn;

    private List<Document> recentDocs;
    private List<Document> recommendedDocs;
    private int recentDocIndex = 0;      // index of current doc
    private int recommendedDocIndex = 0;
    private static final double CIRCLE_RADIUS = 30.0;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadUserData();
        setupCircleProgress();
        recentBorrowsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetails();
            }
        });
        loadRecentlyAddedDocuments();
        loadRecommendedDocuments();
        HandleOutsideClickListener();

        currentBookThumbnail.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetailsFromImage();
            }
        });

        currentRecThumbnail.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showRecommendedDocumentDetailsFromImage();
            }
        });
    }

    private void setupTableColumns() {
        docTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
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
    }

    private void loadUserData() {
        int currentUserId = getCurrentUserId();
        List<BorrowHistory> borrowRecords = BorrowRepository.getInstance().getUserBorrowRecords(currentUserId);
        updateBorrowStats(borrowRecords);
        updateRecentBorrowTable(borrowRecords);
    }

    private void updateBorrowStats(List<BorrowHistory> borrowRecords) {
        int totalBorrowed = 0, dueSoon = 0, overdue = 0, totalHistory = borrowRecords.size();
        LocalDate now = LocalDate.now();
        LocalDate oneWeekFromNow = now.plusDays(7);

        for (BorrowHistory record : borrowRecords) {
            if (record.getReturnDate() == null) {
                totalBorrowed++;
                LocalDate dueDate = parseDate(record.getDueDate());
                if (dueDate != null) {
                    if (dueDate.isBefore(now)) overdue++;
                    else if (dueDate.isBefore(oneWeekFromNow)) dueSoon++;
                }
            }
        }

        borrowedCount.setText(String.valueOf(totalBorrowed));
        dueSoonCount.setText(String.valueOf(dueSoon));
        overdueCount.setText(String.valueOf(overdue));
        historyCount.setText(String.valueOf(totalHistory));
    }

    private void updateRecentBorrowTable(List<BorrowHistory> borrowRecords) {
        int limit = Math.min(5, borrowRecords.size());
        recentBorrowsTable.setItems(FXCollections.observableArrayList(borrowRecords.subList(0, limit)));
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            System.err.println("Parse date error: " + date);
            return null;
        }
    }

    private void setupCircleProgress() {
        int totalHistory = Integer.parseInt(historyCount.getText());
        if (totalHistory == 0) totalHistory = 1; // Avoid division by zero

        setupCircle(borrowedCircle, (Integer.parseInt(borrowedCount.getText()) * 100.0) / totalHistory);
        setupCircle(dueSoonCircle, (Integer.parseInt(dueSoonCount.getText()) * 100.0) / totalHistory);
        setupCircle(overdueCircle, (Integer.parseInt(overdueCount.getText()) * 100.0) / totalHistory);
    }

    private void setupCircle(Circle circle, double percentage) {
        double circumference = 2 * Math.PI * CIRCLE_RADIUS;
        circle.getStrokeDashArray().setAll((percentage * circumference) / 100, circumference);
        circle.setRotate(-90);
    }

    private void loadRecentlyAddedDocuments() {
        recentDocs = BookRepository.getInstance().getRecentAddedDocuments(); // Adjust repository accordingly
        displayDocument(currentBookThumbnail, recentDocs, recentDocIndex);
    }

    private void loadRecommendedDocuments() {
        recommendedDocs = BookRepository.getInstance().getRecommendedDocuments(getCurrentUserId());
        displayDocument(currentRecThumbnail, recommendedDocs, recommendedDocIndex);
    }

    private void showDocumentDetailsFromImage() {
        if (recentDocs != null && recentDocIndex < recentDocs.size()) {
            openDocumentDetails(recentDocs.get(recentDocIndex));
        }
    }

    private void showRecommendedDocumentDetailsFromImage() {
        if (recommendedDocs != null && recommendedDocIndex < recommendedDocs.size()) {
            openDocumentDetails(recommendedDocs.get(recommendedDocIndex));
        }
    }

    private void showDocumentDetails() {
        BorrowHistory borrowHistory = recentBorrowsTable.getSelectionModel().getSelectedItem();
        int userId = SessionManager.getInstance().getUser().getId();
        int borrow_id = borrowHistory.getId();
        Document document = BorrowRepository.getInstance().getRecentDocument(userId, borrow_id);
        openDocumentDetails(document);
    }

    private void openDocumentDetails(Document document) {
        if (document != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentDetail.fxml"));
                Parent detailRoot = loader.load();

                // Get the controller and set the selected document
                DocumentDetailController controller = loader.getController();
                controller.setDocumentDetails(document);

                // Create a new stage for the document detail window
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

    private int getCurrentUserId() {
        return SessionManager.getInstance().getUser().getId();
    }

    private void HandleOutsideClickListener() {
        recentBorrowsTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                    if (event.getTarget() instanceof Node) {
                        Node target = (Node) event.getTarget();
                        while (target != null) {
                            if (target == recentBorrowsTable) { // clicked on table view
                                return;
                            }
                            target = target.getParent();
                        }
                        recentBorrowsTable.getSelectionModel().clearSelection(); // clicked outside tableview --> cancel selection
                    }
                });
            }
        });
    }

    private void displayDocument(ImageView imageView, List<Document> documents, int index) {
        if (documents == null || documents.isEmpty() || index < 0 || index >= documents.size()) {
            return; // Không làm gì nếu danh sách trống hoặc chỉ số không hợp lệ
        }
        Document doc = documents.get(index);
        Image coverImage = new Image(doc.getThumbnailUrl(), true);
        imageView.setImage(coverImage);
    }

    @FXML
    private void prevDoc() {
        recentDocIndex = calculateIndex(recentDocIndex, recentDocs.size(), false);
        displayDocument(currentBookThumbnail, recentDocs, recentDocIndex);
    }

    @FXML
    private void nextDoc() {
        recentDocIndex = calculateIndex(recentDocIndex, recentDocs.size(), true);
        displayDocument(currentBookThumbnail, recentDocs, recentDocIndex);
    }

    @FXML
    private void prevRec() {
        recommendedDocIndex = calculateIndex(recommendedDocIndex, recommendedDocs.size(), false);
        displayDocument(currentRecThumbnail, recommendedDocs, recommendedDocIndex);
    }

    @FXML
    private void nextRec() {
        recommendedDocIndex = calculateIndex(recommendedDocIndex, recommendedDocs.size(), true);
        displayDocument(currentRecThumbnail, recommendedDocs, recommendedDocIndex);
    }

    /**
     * Tính toán chỉ số tiếp theo hoặc trước đó, đảm bảo chỉ số hợp lệ và xoay vòng (circular index).
     *
     * @param currentIndex Chỉ số hiện tại
     * @param size Tổng số phần tử
     * @param isNext Nếu true, tính chỉ số tiếp theo; nếu false, tính chỉ số trước đó
     * @return Chỉ số mới đã được xoay vòng
     */
    private int calculateIndex(int currentIndex, int size, boolean isNext) {
        if (size == 0) return 0;
        return isNext ? (currentIndex + 1) % size : (currentIndex - 1 + size) % size;
    }
}