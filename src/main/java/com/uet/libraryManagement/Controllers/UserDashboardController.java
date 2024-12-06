package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.BorrowHistory;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Managers.SceneManager;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.BorrowRepository;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
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
import java.util.Objects;

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
        loadUserDataInBackground();
        recentBorrowsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                if (recentBorrowsTable.getSelectionModel().getSelectedItem() != null) {
                    showDocumentDetails();
                }
            }
        });
        loadRecentlyAddedDocumentsInBackground();
        loadRecommendedDocumentsInBackground();
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

    private void loadUserDataInBackground() {
        Task<List<BorrowHistory>> loadDataTask = new Task<>() {
            @Override
            protected List<BorrowHistory> call() {
                int currentUserId = getCurrentUserId();
                return BorrowRepository.getInstance().getUserBorrowRecords(currentUserId);
            }
        };

        TaskManager.runTask(loadDataTask,
                () -> {
                    List<BorrowHistory> borrowRecords = loadDataTask.getValue();
                    updateBorrowStats(borrowRecords);
                    updateRecentBorrowTable(borrowRecords);
                    setupTableColumns();
                    setupCircleProgress();
                },
                () -> {
                    Throwable exception = loadDataTask.getException();
                    exception.printStackTrace(); // Xử lý lỗi tải dữ liệu
                });
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

    private void loadRecentlyAddedDocumentsInBackground() {
        Task<List<Document>> loadRecentDocTask = new Task<>() {
            @Override
            protected List<Document> call() {
            return BookRepository.getInstance().getRecentAddedDocuments();
            }
        };

        TaskManager.runTask(loadRecentDocTask,
                () -> {
                    recentDocs = loadRecentDocTask.getValue();
                    displayDocument(currentBookThumbnail, recentDocs, recentDocIndex);
                },
                () -> {
                    Throwable exception = loadRecentDocTask.getException();
                    exception.printStackTrace(); // Xử lý lỗi tải dữ liệu
                });
    }

    private void loadRecommendedDocumentsInBackground() {
        Task<List<Document>> loadRecommendedTask = new Task<>() {
        @Override
        protected List<Document> call() {
            return BookRepository.getInstance().getRecommendedDocuments(getCurrentUserId());
        }
        };

        TaskManager.runTask(loadRecommendedTask,
                () -> {
                    recommendedDocs = loadRecommendedTask.getValue();
                    displayDocument(currentRecThumbnail, recommendedDocs, recommendedDocIndex);
                },
                () -> {
                    Throwable exception = loadRecommendedTask.getException();
                    exception.printStackTrace(); // Xử lý lỗi tải dữ liệu
                });
    }

    private void showDocumentDetailsFromImage() {
        if (recentDocs != null && recentDocIndex < recentDocs.size()) {
            Document recent = recentDocs.get(recentDocIndex);
            String docType;
            if (recent instanceof Book) {
                docType = "book";
            } else {
                docType = "thesis";
            }
            openDocumentDetails(recentDocs.get(recentDocIndex), docType);
        }
    }

    private void showRecommendedDocumentDetailsFromImage() {
        if (recommendedDocs != null && recommendedDocIndex < recommendedDocs.size()) {
            Document document = recommendedDocs.get(recommendedDocIndex);
            String docType;
            if (document instanceof Book) {
                docType = "book";
            } else {
                docType = "thesis";
            }
            openDocumentDetails(recommendedDocs.get(recommendedDocIndex), docType);
        }
    }

    private void showDocumentDetails() {
        BorrowHistory borrowHistory = recentBorrowsTable.getSelectionModel().getSelectedItem();
        int userId = SessionManager.getInstance().getUser().getId();
        int borrow_id = borrowHistory.getId();
        Document document = BorrowRepository.getInstance().getRecentDocument(userId, borrow_id);
        String docType = BorrowRepository.getInstance().getDocType(userId, borrow_id);
        openDocumentDetails(document, docType);
    }

    private void openDocumentDetails(Document document, String docType) {
            if (document != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uet/libraryManagement/FXML/DocumentDetail.fxml"));
                    Parent detailRoot = loader.load();

                    // Lấy controller và thiết lập dữ liệu tài liệu
                    DocumentDetailController controller = loader.getController();
                    controller.setDocumentDetails(document);
                    controller.setDocument(document);
                    controller.setDocType(docType);
                    controller.loadComments(document.getId(), docType); // Load comments trực tiếp

                    // Tạo Scene và Stage cho cửa sổ chi tiết tài liệu
                    Scene detailScene = new Scene(detailRoot);
                    detailScene.getStylesheets().add(SceneManager.getInstance().get_css());

                    Stage detailStage = new Stage();
                    detailStage.setResizable(false);
                    String icon_url = Objects.requireNonNull(this.getClass().getResource("/com/uet/libraryManagement/ICONS/logo.png")).toExternalForm();
                    Image icon = new Image(icon_url);
                    detailStage.getIcons().add(icon);
                    detailStage.setTitle("Document Details");
                    detailStage.setScene(detailScene);
                    detailStage.initModality(Modality.APPLICATION_MODAL); // Đặt chế độ modal
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
        Document document = documents.get(index);
        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()
                && !document.getThumbnailUrl().equalsIgnoreCase("No Thumbnail")) {
            Image image = new Image(document.getThumbnailUrl(), true);
            imageView.setImage(image);

        } else {
            Image image = new Image(getClass().getResource("/com/uet/libraryManagement/ICONS/no_image.png").toExternalForm());
            imageView.setFitHeight(200);
            imageView.setFitWidth(150);
            imageView.setImage(image); // set no thumbnail image
        }
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