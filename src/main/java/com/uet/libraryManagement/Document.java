package com.uet.libraryManagement;

public class Document {
    // attributes
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private String publishedDate;
    private String thumbnailUrl;
    private String isbn10;
    private String isbn13;

    //constructors
    public Document() {

    }

    public Document(String title, String author, String publisher,
                    String description, String year, String thumbnailUrl, String isbn10, String isbn13) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publishedDate = year;
        this.thumbnailUrl = thumbnailUrl;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
    }

    public Document(int id, String title, String author, String publisher,
                    String description, String year, String thumbnailUrl, String isbn10, String isbn13) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publishedDate = year;
        this.thumbnailUrl = thumbnailUrl;
        this.isbn10 = isbn10;
        this.isbn13 = isbn13;
    }

    // getters, setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYear() {
        return publishedDate;
    }

    public void setYear(String year) {
        this.publishedDate = year;
    }

    public String getThumbnailUrl() { return thumbnailUrl; }

    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getIsbn10() { return isbn10; }

    public void setIsbn10(String isbn10) { this.isbn10 = isbn10; }

    public String getIsbn13() { return isbn13; }

    public void setIsbn13(String isbn13) { this.isbn13 = isbn13; }
}
