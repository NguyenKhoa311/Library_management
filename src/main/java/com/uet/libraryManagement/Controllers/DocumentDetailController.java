package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.Document;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DocumentDetailController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView thumbnailImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label authorLabel;
    @FXML
    private Label publisherLabel;
    @FXML
    private Label publishedDateLabel;
    @FXML
    private Label genreLabel;


    public void setDocumentDetails(Document document) {
        titleLabel.setText(document.getTitle());
        authorLabel.setText("Author: " + document.getAuthor());
        publisherLabel.setText("Publisher: " + document.getPublisher());
        publishedDateLabel.setText("Published date: " + document.getYear());
        genreLabel.setText("Genre: " + document.getCategory());
        descriptionArea.setText(document.getDescription());

        // Load thumbnail image
        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()
                && !document.getThumbnailUrl().equalsIgnoreCase("No Thumbnail")) {
            Image image = new Image(document.getThumbnailUrl(), true);
            thumbnailImageView.setImage(image);
        } else {
            Image image = new Image(getClass().getResource("/com/uet/libraryManagement/icons/no_image.png").toExternalForm());
            thumbnailImageView.setFitHeight(150);
            thumbnailImageView.setFitWidth(150);
            thumbnailImageView.setImage(image); // set no thumbnail image
        }
    }
}

