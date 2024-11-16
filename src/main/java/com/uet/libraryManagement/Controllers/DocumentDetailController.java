package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Thesis;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

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
        if (document instanceof Book) {
            genreLabel.setText("Genre: " + document.getCategory());
        } else if (document instanceof Thesis) {
            genreLabel.setText("Field: " + document.getCategory());
        }
        descriptionArea.setText(document.getDescription());

        // Load thumbnail image
        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()
                && !document.getThumbnailUrl().equalsIgnoreCase("No Thumbnail")) {
            if (document.getThumbnailUrl().startsWith("http")) {
                Image image = new Image(document.getThumbnailUrl(), true);
                thumbnailImageView.setImage(image);
            } else {
                File file = new File(document.getThumbnailUrl());
                Image image = new Image(file.toURI().toString(), true);
                thumbnailImageView.setImage(image);
            }
        } else {
            Image image = new Image(getClass().getResource("/com/uet/libraryManagement/icons/no_image.png").toExternalForm());
            thumbnailImageView.setFitHeight(150);
            thumbnailImageView.setFitWidth(150);
            thumbnailImageView.setImage(image); // set no thumbnail image
        }
    }
}
