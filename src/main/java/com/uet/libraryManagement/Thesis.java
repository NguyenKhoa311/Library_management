package com.uet.libraryManagement;

import javafx.beans.property.SimpleStringProperty;

public class Thesis extends Document {
    // attribute
    private final SimpleStringProperty field;

    // constructors
    public Thesis() {
        super();
        field = new SimpleStringProperty();
    }

    public Thesis(int id, String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13);
        this.field = new SimpleStringProperty(field);
    }

    public Thesis(String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13) {
        super(title, author, publisher, description, year, url, isbn10, isbn13);
        this.field = new SimpleStringProperty(field);
    }

    public Thesis(String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13, int quantity) {
        super(title, author, publisher, description, year, url, isbn10, isbn13, quantity);
        this.field = new SimpleStringProperty(field);
    }

    public Thesis(int id, String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13, int quantity) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13, quantity);
        this.field = new SimpleStringProperty(field);
    }

    // setter, getter
    public String getField() {
        return field.get();
    }

    public void setField(String field) {
        this.field.set(field);
    }

    @Override
    public String getCategory() {
        return field.get();
    }

    @Override
    public void setCategory(String category) {
        setField(category);
    }
}
