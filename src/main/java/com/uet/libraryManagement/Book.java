package com.uet.libraryManagement;

import javafx.beans.property.SimpleStringProperty;

public class Book extends Document {
    // attribute
    private final SimpleStringProperty genre;

    // constructors
    public Book() {
        super();
        this.genre = new SimpleStringProperty();
    }

    public Book(String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13) {
        super(title, author, publisher, description, year, url, isbn10, isbn13);
        this.genre = new SimpleStringProperty(genre);
    }

    public Book(int id, String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13);
        this.genre = new SimpleStringProperty(genre);
    }

    public Book(String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13, int quantity) {
        super(title, author, publisher, description, year, url, isbn10, isbn13, quantity);
        this.genre = new SimpleStringProperty(genre);
    }

   public Book(int id, String title, String author, String publisher, String description, String year, String genre, String url, String isbn10, String isbn13, int quantity) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13, quantity);
       this.genre = new SimpleStringProperty(genre);
   }

    // getters, setters
    public String getGenre() {
        return genre.get();
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    @Override
    public String getCategory() {
        return genre.get();
    }

    @Override
    public void setCategory(String category) {
        setGenre(category);
    }
}
