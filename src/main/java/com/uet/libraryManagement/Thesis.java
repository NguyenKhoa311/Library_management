package com.uet.libraryManagement;

public class Thesis extends Document {
    // attribute
    private String field;

    // constructors
    public Thesis() {

    }

    public Thesis(String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13) {
        super(title, author, publisher, description, year, url, isbn10, isbn13);
        this.field = field;
    }

    public Thesis(int id, String title, String author, String publisher, String description, String year, String field, String url, String isbn10, String isbn13) {
        super(id, title, author, publisher, description, year, url, isbn10, isbn13);
        this.field = field;
    }

    // setter, getter
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
