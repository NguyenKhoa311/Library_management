package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Thesis;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SearchDocumentDetailController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView thumbnailImageView;
    @FXML
    private Label titleLabel, authorLabel, publisherLabel, publishedDateLabel, genreLabel, isbn10Label, isbn13Label;

    public void setDocumentDetails(Document document) {
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
}