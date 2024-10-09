package com.uet.libraryManagement;

public class Document {
    // attributes
    private String id;
    private String title;
    private String author;
    private String publisher;
    private String description;
    private int publicationYear;

    //constructors
    public Document() {

    }

    public Document(String id, String title, String author, String publisher, String description, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.description = description;
        this.publicationYear = year;
    }

    // getters, setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getYear() {
        return publicationYear;
    }

    public void setYear(int year) {
        this.publicationYear = year;
    }
}
