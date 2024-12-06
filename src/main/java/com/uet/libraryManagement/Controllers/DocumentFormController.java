package com.uet.libraryManagement.Controllers;

import com.uet.libraryManagement.*;
import com.uet.libraryManagement.APIService.ImgurUpload;
import com.uet.libraryManagement.Managers.TaskManager;
import com.uet.libraryManagement.Repositories.BookRepository;
import com.uet.libraryManagement.Repositories.ThesisRepository;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class DocumentFormController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ImageView thumbnailImage;
    @FXML
    private TextField titleField, authorField, publisherField, dateField, categoryField, isbn10Field, isbn13Field, quantityField;

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
        quantityField.setText(String.valueOf(document.getQuantity()));

        if (document.getThumbnailUrl() != null && !document.getThumbnailUrl().isEmpty()
                && !document.getThumbnailUrl().equalsIgnoreCase("No Thumbnail")) {
            Image image = new Image(document.getThumbnailUrl(), true);
            thumbnailImage.setImage(image);
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

            // show temporary image
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
        String quantityInput = quantityField.getText();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty() || category.isEmpty() || quantityInput.isEmpty()) {
            showAlert("Please fill in all required fields.");
            return;
        }

        if (!isbn10.isEmpty() && isbn10.length() != 10) {
            showAlert("ISBN-10 must be exactly 10 characters.");
            return;
        }

        if (!isbn13.isEmpty() && isbn13.length() != 13) {
            showAlert("ISBN-13 must be exactly 13 characters.");
            return;
        }

        // Validate and parse quantity
        int quantity;
        try {
            quantity = Integer.parseInt(quantityInput);
            if (quantity <= 0) {
                showAlert("Quantity must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Please enter a valid integer for quantity.");
            return;
        }

        Task<Void> saveTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // This block will run in a separate thread
                String imgurUrl = null;

                if (thumbnailFilePath != null && !thumbnailFilePath.isEmpty()) {
                    try {
                        imgurUrl = ImgurUpload.uploadImage(new File(thumbnailFilePath)); // Tải ảnh lên Imgur và nhận URL
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Error uploading thumbnail image to Imgur.");
                        return null; // Dừng lại nếu không thể tải ảnh lên Imgur
                    }
                }

                if ("add".equals(mode)) {
                    if ("Books".equals(docType)) {
                        document = new Book(title, author, publisher, description, publishDate, category, imgurUrl, isbn10, isbn13, quantity);
                        BookRepository.getInstance().create(document, quantity);
                        System.out.println("Book added");
                    } else if ("Theses".equals(docType)) {
                        document = new Thesis(title, author, publisher, description, publishDate, category, imgurUrl, isbn10, isbn13, quantity);
                        ThesisRepository.getInstance().create(document, quantity);
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
                    document.setQuantity(quantity);

                    // Upload image to imgur if necessary
                    if (thumbnailFilePath != null && !thumbnailFilePath.isEmpty() && !thumbnailFilePath.equals(document.getThumbnailUrl())) {
                        try {
                            imgurUrl = ImgurUpload.uploadImage(new File(thumbnailFilePath)); // Upload image to Imgur
                            document.setThumbnailUrl(imgurUrl);  // Set the new Imgur URL
                        } catch (IOException e) {
                            e.printStackTrace();
                            showAlert("Error uploading thumbnail image to Imgur.");
                            return null;
                        }
                    }

                    if (document instanceof Book) {
                        BookRepository.getInstance().update(document);
                    } else if (document instanceof Thesis) {
                        ThesisRepository.getInstance().update(document);
                    }
                }

                return null;
            }
        };

        Runnable onSuccess = () -> {
            showAlert("Document saved successfully.");
            closeForm();
        };

        Runnable onFailure = () -> {
            Throwable ex = saveTask.getException();
            if (ex != null) ex.printStackTrace(); // Log lỗi để debug
            showAlert("Failed to save the document.");
        };

        TaskManager.runTask(saveTask, onSuccess, onFailure);
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

