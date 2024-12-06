package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Managers.SessionManager;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.RatingComment;
import com.uet.libraryManagement.Repositories.RatingRepository;
import com.uet.libraryManagement.Thesis;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class DocumentDetailController {
    @FXML private Label ratingNum;
    @FXML private ListView<String> commentsListView;
    @FXML private TextArea newCommentArea;
    @FXML private FontAwesomeIconView userRatingStar1, userRatingStar2, userRatingStar3, userRatingStar4, userRatingStar5;
    @FXML private FontAwesomeIconView star1, star2, star3, star4, star5;
    @FXML private TextArea descriptionArea;
    @FXML private ImageView thumbnailImageView;
    @FXML private Label titleLabel, authorLabel, publisherLabel, publishedDateLabel, genreLabel, isbn10Label, isbn13Label;

    private Document document;
    private String docType;
    private Integer userRating = null;  // Store the selected rating

    public void setDocumentDetails(Document document) {
        setupRatingEvents();
        titleLabel.setText(document.getTitle());
        authorLabel.setText("Author: " + document.getAuthor());
        publisherLabel.setText("Publisher: " + document.getPublisher());
        publishedDateLabel.setText("Published date: " + document.getYear());
        if (document instanceof Book) {
            genreLabel.setText("Genre: " + document.getCategory());
        } else if (document instanceof Thesis) {
            genreLabel.setText("Field: " + document.getCategory());
        }
        isbn10Label.setText("Isbn 10: " + document.getIsbn10());
        isbn13Label.setText("Isbn 13: " + document.getIsbn13());
        descriptionArea.setText(document.getDescription());

        // Load thumbnail image
        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()
                && !document.getThumbnailUrl().equalsIgnoreCase("No Thumbnail")) {
            Image image = new Image(document.getThumbnailUrl(), true);
            thumbnailImageView.setImage(image);

        } else {
            Image image = new Image(getClass().getResource("/com/uet/libraryManagement/ICONS/no_image.png").toExternalForm());
            thumbnailImageView.setFitHeight(150);
            thumbnailImageView.setFitWidth(150);
            thumbnailImageView.setImage(image); // set no thumbnail image
        }
    }

    @FXML
    private void comment() {
        // Kiểm tra người dùng có nhập bình luận hay không
        String commentText = newCommentArea.getText().trim();
        if (commentText.isEmpty()) {
            showAlert("Please enter a comment.");
            return;
        }

        if (userRating == null) {
            showAlert("Please rating by choosing stars above.");
            return;
        }

        // Lấy thông tin người dùng
        int userId = SessionManager.getInstance().getUser().getId();
        RatingComment newComment = new RatingComment(userId, userRating, commentText, new Timestamp(System.currentTimeMillis()));

        Task<Void> saveCommentTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                RatingRepository.getInstance().addComment(document.getId(), getDocType(), newComment);
                return null;
            }
        };

        TaskManager.runTask(saveCommentTask,
                () -> {
                    showAlert("Comment sent.");
                    loadComments(document.getId(), getDocType());
                    newCommentArea.clear();
                },
                () -> showAlert("Failed to save comment.")
        );
    }

    public void loadComments(int documentId, String docType) {
        Task<Void> loadCommentsTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Lấy số lượng đánh giá và điểm đánh giá từ cơ sở dữ liệu
                String normalizedDocType;
                if (docType.equalsIgnoreCase("book") || docType.equalsIgnoreCase("books")) {
                    normalizedDocType = "book";
                } else if (docType.equalsIgnoreCase("thesis") || docType.equalsIgnoreCase("theses")) {
                    normalizedDocType = "thesis";
                } else {
                    throw new IllegalArgumentException("Invalid document type: " + docType);
                }
                int numRating = RatingRepository.getInstance().getRatingNum(documentId, normalizedDocType);
                int docRating = RatingRepository.getInstance().getRating(documentId, normalizedDocType);

                // Lấy danh sách bình luận
                ObservableList<RatingComment> comments = RatingRepository.getInstance().getCommentsByDocumentId(documentId, docType);
                if (comments == null || comments.isEmpty()) {
                    comments = FXCollections.observableArrayList();
                    comments.add(new RatingComment("System", 0, 0, "There aren't any comments yet.", new Timestamp(System.currentTimeMillis())));
                }

                // Chuyển đổi bình luận thành chuỗi
                ObservableList<String> commentStrings = FXCollections.observableArrayList();
                for (RatingComment comment : comments) {
                    commentStrings.add(comment.toString());
                }

                // Cập nhật giao diện trên JavaFX Application Thread
                Platform.runLater(() -> {
                    ratingNum.setText("(" + numRating + ")");
                    updateRating(docRating);
                    commentsListView.setItems(commentStrings);
                });

                return null;
            }
        };

        TaskManager.runTask(loadCommentsTask,
                () -> System.out.println("Load comments successfully."), // Callback thành công
                () -> showAlert("Failed to load comments.")              // Callback thất bại
        );
    }

    private void setupRatingEvents() {
        // Danh sách các star
        List<FontAwesomeIconView> stars = Arrays.asList(userRatingStar1, userRatingStar2, userRatingStar3, userRatingStar4, userRatingStar5);

        // Xử lý hover và click cho từng star
        for (int i = 0; i < stars.size(); i++) {
            final int starIndex = i;
            FontAwesomeIconView star = stars.get(i);

            // Sự kiện di chuột vào
            star.setOnMouseEntered(event -> {
                // Highlight tất cả các star từ 0 đến starIndex
                for (int j = 0; j <= starIndex; j++) {
                    stars.get(j).getStyleClass().add("star-icon-hover");
                    stars.get(j).getStyleClass().remove("star-icon");
                    stars.get(j).getStyleClass().remove("star-icon-selected");
                }
                // Unhighlight các star còn lại
                for (int j = starIndex + 1; j < stars.size(); j++) {
                    stars.get(j).getStyleClass().add("star-icon");
                    stars.get(j).getStyleClass().remove("star-icon-hover");
                    stars.get(j).getStyleClass().remove("star-icon-selected");
                }
            });

            // Sự kiện di chuột ra
            star.setOnMouseExited(event -> {
                // Nếu chưa chọn userRating, trả tất cả các star về trạng thái ban đầu
                if (userRating == null) {
                    for (FontAwesomeIconView s : stars) {
                        s.getStyleClass().clear();
                        s.getStyleClass().add("star-icon");
                    }
                } else {
                    // Nếu đã chọn rating, giữ highlight cho các star được chọn
                    for (int j = 0; j < stars.size(); j++) {
                        if (j < userRating) {
                            stars.get(j).getStyleClass().clear();
                            stars.get(j).getStyleClass().add("star-icon-selected");
                        } else {
                            stars.get(j).getStyleClass().clear();
                            stars.get(j).getStyleClass().add("star-icon");
                        }
                    }
                }
            });

            // Sự kiện click
            star.setOnMouseClicked(event -> {
                int rating = starIndex + 1;
                userRating = rating;

                // Highlight các star được chọn
                for (int j = 0; j < stars.size(); j++) {
                    if (j < rating) {
                        stars.get(j).getStyleClass().clear();
                        stars.get(j).getStyleClass().add("star-icon-selected");
                    } else {
                        stars.get(j).getStyleClass().clear();
                        stars.get(j).getStyleClass().add("star-icon");
                    }
                }
            });
        }
    }

    private void updateRating(int rating) {
        // Danh sách các sao
        List<FontAwesomeIconView> stars = Arrays.asList(star1, star2, star3, star4, star5);

        // Làm nổi bật các sao dựa trên giá trị rating
        for (int i = 0; i < stars.size(); i++) {
            if (i < rating) {
                stars.get(i).getStyleClass().clear();
                stars.get(i).getStyleClass().add("star-icon-selected");
            } else {
                stars.get(i).getStyleClass().clear();
                stars.get(i).getStyleClass().add("star-icon");
            }
        }
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

