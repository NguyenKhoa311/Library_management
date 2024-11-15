package com.uet.libraryManagement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Document {
    // attributes
    private SimpleIntegerProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty description;
    private SimpleStringProperty publishedDate;
    private SimpleStringProperty thumbnailUrl;
    private SimpleStringProperty isbn10;
    private SimpleStringProperty isbn13;

    //constructors
    public Document() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.author = new SimpleStringProperty();
        this.publisher = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.publishedDate = new SimpleStringProperty();
        this.thumbnailUrl = new SimpleStringProperty();
        this.isbn10 = new SimpleStringProperty();
        this.isbn13 = new SimpleStringProperty();
    }

    public Document(String title, String author, String publisher,
                    String description, String year, String thumbnailUrl, String isbn10, String isbn13) {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.description = new SimpleStringProperty(description);
        this.publishedDate = new SimpleStringProperty(year);
        this.thumbnailUrl = new SimpleStringProperty(thumbnailUrl);
        this.isbn10 = new SimpleStringProperty(isbn10);
        this.isbn13 = new SimpleStringProperty(isbn13);
    }

    public Document(int id, String title, String author, String publisher,
                    String description, String year, String thumbnailUrl, String isbn10, String isbn13) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.description = new SimpleStringProperty(description);
        this.publishedDate = new SimpleStringProperty(year);
        this.thumbnailUrl = new SimpleStringProperty(thumbnailUrl);
        this.isbn10 = new SimpleStringProperty(isbn10);
        this.isbn13 = new SimpleStringProperty(isbn13);
    }

    // getters, setters
    public int getId() { return id.get(); }

    public void setId(int id) { this.id.set(id); }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getPublisher() {
        return publisher.get();
    }

    public void setPublisher(String publisher) {
        this.publisher.set(publisher);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getYear() {
        return publishedDate.get();
    }

    public void setYear(String year) {
        this.publishedDate.set(year);
    }

    public String getCategory() {
        return "";
    }

    public String getThumbnailUrl() { return thumbnailUrl.get(); }

    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl.set(thumbnailUrl); }

    public String getIsbn10() { return isbn10.get(); }

    public void setIsbn10(String isbn10) { this.isbn10.set(isbn10); }

    public String getIsbn13() { return isbn13.get(); }

    public void setIsbn13(String isbn13) { this.isbn13.set(isbn13); }
}
