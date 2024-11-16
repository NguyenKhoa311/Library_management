package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class DocumentFormController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView thumbnailImageView;
    @FXML
    private TextField titleField, authorField, publisherField, dateField, categoryField, isbn10Field, isbn13Field;

    private String mode;
    private Document document;
    private String docType;

    @FXML
    private String thumbnailFilePath;

    private final BookRepository bookRepository = new BookRepository();
    private final ThesisRepository thesisRepository = new ThesisRepository();

    public void setMode(String mode) {
        this.mode = mode;
        if ("add".equals(mode)) {
            clearFields();
        }
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    // Set document details if editing an existing document
    public void setDocument(Document document) {
        this.document = document;
        titleField.setText(document.getTitle());
        authorField.setText(document.getAuthor());
        publisherField.setText(document.getPublisher());
        dateField.setText(document.getYear());
        categoryField.setText(document.getCategory());
        descriptionArea.setText(document.getDescription());
        isbn10Field.setText(document.getIsbn10());
        isbn13Field.setText(document.getIsbn13());

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

    // Clear input fields (for adding a new document)
    private void clearFields() {
        titleField.clear();
        authorField.clear();
        publisherField.clear();
        dateField.clear();
        categoryField.clear();
        descriptionArea.clear();
        isbn10Field.clear();
        isbn13Field.clear();
        thumbnailImageView.setImage(null);
        thumbnailFilePath = null;
    }

    public void insertImg(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Thumbnail Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(thumbnailImageView.getScene().getWindow());
        if (selectedFile != null) {
            try {
                // Copy file to application directory
                Path targetDir = Paths.get("user_data/thumbnails"); // Ensure this directory exists in your project
                Files.createDirectories(targetDir);
                Path targetPath = targetDir.resolve(selectedFile.getName());
                Files.copy(selectedFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

                thumbnailFilePath = "user_data/thumbnails/" + selectedFile.getName(); // Save relative path
                Image thumbnailImage = new Image(targetPath.toUri().toString());
                thumbnailImageView.setImage(thumbnailImage); // Display the image in the ImageView
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error copying image file.");
            }
        }
    }

    public void saveDoc() {
        String title = titleField.getText();
        String author = authorField.getText();
        String publisher = publisherField.getText();
        String publishDate = dateField.getText();
        String category = categoryField.getText();
        String description = descriptionArea.getText();
        String isbn10 = isbn10Field.getText();
        String isbn13 = isbn13Field.getText();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty() || category.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        if ("add".equals(mode)) {
            if ("Books".equals(docType)) {
                document = new Book(title, author, publisher, description, publishDate, category, thumbnailFilePath, isbn10, isbn13);
                bookRepository.create(document);
                System.out.println("Book added");
            } else if ("Theses".equals(docType)) {
                document = new Thesis(title, author, publisher, description, publishDate, category, thumbnailFilePath, isbn10, isbn13);
                thesisRepository.create(document);
                System.out.println("Thesis added");
            }
        } else if ("edit".equals(mode) && document != null) {
            // Update document details and save changes
            document.setTitle(title);
            document.setAuthor(author);
            document.setPublisher(publisher);
            document.setYear(publishDate);
            document.setCategory(category);
            document.setDescription(description);
            document.setIsbn10(isbn10);
            document.setIsbn13(isbn13);

            if (thumbnailFilePath == null || thumbnailFilePath.isEmpty()) {
                // Giữ nguyên thumbnail cũ nếu không có thay đổi
                thumbnailFilePath = document.getThumbnailUrl();  // Lấy giá trị thumbnail cũ
            }
            document.setThumbnailUrl(thumbnailFilePath);

            if (document instanceof Book) {
                bookRepository.update(document);
            } else if (document instanceof Thesis) {
                thesisRepository.update(document);
            }
        }

        showAlert("Document saved successfully.");
        closeForm();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }
}

