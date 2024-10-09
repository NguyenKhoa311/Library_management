package com.uet.libraryManagement;

public class Thesis extends Document {
    // attribute
    private String field;

    // constructors
    public Thesis() {

    }

    public Thesis(String id, String title, String author, String publisher, String description, int year, String field) {
        super(id, title, author, publisher, description, year);
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
