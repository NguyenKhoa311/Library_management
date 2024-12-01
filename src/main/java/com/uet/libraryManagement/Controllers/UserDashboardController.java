package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.BorrowHistory;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeLineCap;
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
    private Circle historyCircle;
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
    private int currentIndex = 0;      // index of current doc
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
        loadRecentlyAddedBooks();
        HandleOutsideClickListener();

        currentBookThumbnail.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                showDocumentDetailsFromImage();
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

        int totalBorrowed = 0;
        int dueSoon = 0;
        int overdue = 0;
        int totalHistory = BorrowRepository.getInstance().getUserIssuedDocs(currentUserId);

        LocalDate now = LocalDate.now();
        LocalDate oneWeekFromNow = now.plusDays(7);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (BorrowHistory record : borrowRecords) {
            if (record.getReturnDate() == null) {
                totalBorrowed++;

                try {
                    LocalDate dueDate = LocalDate.parse(record.getDueDate(), dateFormatter);
                    if (dueDate.isBefore(now)) {
                        overdue++;
                    } else if (dueDate.isBefore(oneWeekFromNow)) {
                        dueSoon++;
                    }
                } catch (DateTimeParseException e) {
                    System.err.println("Lỗi khi phân tích dueDate: " + record.getDueDate());
                }
            }
        }

        // Cập nhật labels
        borrowedCount.setText(String.valueOf(totalBorrowed));
        dueSoonCount.setText(String.valueOf(dueSoon));
        overdueCount.setText(String.valueOf(overdue));
        historyCount.setText(String.valueOf(totalHistory));

        // Cập nhật bảng với 5 bản ghi gần nhất
        ObservableList<BorrowHistory> recentRecords = FXCollections.observableArrayList(
                borrowRecords.subList(0, Math.min(5, borrowRecords.size()))
        );
        recentBorrowsTable.setItems(recentRecords);
    }

    private void setupCircleProgress() {
        int totalHistory = Integer.parseInt(historyCount.getText());
        int totalBorrowed = Integer.parseInt(borrowedCount.getText());
        int dueSoon = Integer.parseInt(dueSoonCount.getText());
        int overdue = Integer.parseInt(overdueCount.getText());

        // Calculate percentages based on total history
        setupCircle(historyCircle, 100); // Always 100%
        setupCircle(borrowedCircle, totalHistory > 0 ? (totalBorrowed * 100.0) / totalHistory : 0);
        setupCircle(dueSoonCircle, totalHistory > 0 ? (dueSoon * 100.0) / totalHistory : 0);
        setupCircle(overdueCircle, totalHistory > 0 ? (overdue * 100.0) / totalHistory : 0);
    }

    private void setupCircle(Circle circle, double percentage) {
        double circumference = 2 * Math.PI * CIRCLE_RADIUS;
        double dashArray = (percentage * circumference) / 100;
        double gapArray = circumference - dashArray;

        circle.setStrokeLineCap(StrokeLineCap.ROUND);

        ObservableList<Double> strokeDashArray = FXCollections.observableArrayList(
                dashArray, gapArray
        );
        circle.getStrokeDashArray().setAll(strokeDashArray);

        circle.setRotate(-90);
    }

    private void loadRecentlyAddedBooks() {
        // Fetch recently added documents from your data source
        recentDocs = BookRepository.getInstance().getRecentAddedDocuments(); // Adjust repository accordingly
        // Hiển thị cuốn sách đầu tiên
        displayCurrentBook();
    }

    private void displayCurrentBook() {
        if (recentDocs == null || recentDocs.isEmpty() || currentIndex < 0 || currentIndex >= recentDocs.size()) {
            return; // Không làm gì nếu danh sách trống hoặc chỉ số không hợp lệ
        }
        Document doc = recentDocs.get(currentIndex);

        Image coverImage;
        coverImage = new Image(doc.getThumbnailUrl(), true);
        currentBookThumbnail.setImage(coverImage);
    }

    private void showDocumentDetailsFromImage() {
        if (recentDocs == null || recentDocs.isEmpty() || currentIndex >= recentDocs.size()) {
            return; // Không làm gì nếu danh sách trống hoặc chỉ số không hợp lệ
        }

        Document document = recentDocs.get(currentIndex);
        openDocumentDetails(document);
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

    @FXML
    private void prevDoc() {
        if(currentIndex > 0) {
            currentIndex--;
        } else {
            currentIndex = 4;
        }
        displayCurrentBook();
    }

    @FXML
    private void nextDoc() {
        if(currentIndex < 4) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        displayCurrentBook();
    }
}