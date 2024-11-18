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
    private ImageView thumbnailImage;
    @FXML
    private TextField titleField, authorField, publisherField, dateField, categoryField, isbn10Field, isbn13Field;

    private String mode;
    private Document document;
    private String docType;

    @FXML
    private String thumbnailFilePath;

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
                thumbnailImage.setImage(image);
            } else {
                File file = new File(document.getThumbnailUrl());
                Image image = new Image(file.toURI().toString(), true);
                thumbnailImage.setImage(image);
            }
        } else {
            Image image = new Image(getClass().getResource("/com/uet/libraryManagement/ICONS/no_image.png").toExternalForm());
            thumbnailImage.setFitHeight(150);
            thumbnailImage.setFitWidth(150);
            thumbnailImage.setImage(image); // set no thumbnail image
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
        thumbnailImage.setImage(null);
        thumbnailFilePath = null;
    }

    public void insertImg(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Thumbnail Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog(thumbnailImage.getScene().getWindow());
        if (selectedFile != null) {
            thumbnailFilePath = selectedFile.getAbsolutePath(); // save temporary image path

            // show temporary image, not yet save to local folder
            Image img = new Image(new File(thumbnailFilePath).toURI().toString());
            thumbnailImage.setImage(img);
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
                BookRepository.getInstance().create(document);
                System.out.println("Book added");
            } else if ("Theses".equals(docType)) {
                document = new Thesis(title, author, publisher, description, publishDate, category, thumbnailFilePath, isbn10, isbn13);
                ThesisRepository.getInstance().create(document);
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

            // copy image to thumbnails folder if new thumbnail is inserted
            if (thumbnailFilePath != null && !thumbnailFilePath.isEmpty() && !thumbnailFilePath.equals(document.getThumbnailUrl())) {
                try {
                    Path targetDir = Paths.get("user_data/thumbnails");
                    Files.createDirectories(targetDir);
                    Path targetPath = targetDir.resolve(new File(thumbnailFilePath).getName());
                    Files.copy(Paths.get(thumbnailFilePath), targetPath, StandardCopyOption.REPLACE_EXISTING);

                    document.setThumbnailUrl("user_data/thumbnails/" + new File(thumbnailFilePath).getName());
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error copying image file.");
                    return;
                }
            }

            if (document instanceof Book) {
                BookRepository.getInstance().update(document);
            } else if (document instanceof Thesis) {
                ThesisRepository.getInstance().update(document);
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

