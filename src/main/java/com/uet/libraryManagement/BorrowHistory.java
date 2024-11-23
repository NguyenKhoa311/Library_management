package com.uet.libraryManagement;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class BorrowHistory {
    private SimpleIntegerProperty id;
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty borrowDate;
    private final SimpleStringProperty dueDate;
    private final SimpleStringProperty returnDate;
    private final SimpleStringProperty status;

    public BorrowHistory() {
        this.id = new SimpleIntegerProperty();
        this.bookTitle = new SimpleStringProperty();
        this.borrowDate = new SimpleStringProperty();
        this.dueDate = new SimpleStringProperty();
        this.returnDate = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
    }

    public BorrowHistory(String bookTitle, String borrowDate, String dueDate, String returnDate, String status) {
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleStringProperty(borrowDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.status = new SimpleStringProperty(status);
    }

    public BorrowHistory(int id, String bookTitle, String borrowDate, String dueDate, String returnDate, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrowDate = new SimpleStringProperty(borrowDate);
        this.dueDate = new SimpleStringProperty(dueDate);
        this.returnDate = new SimpleStringProperty(returnDate);
        this.status = new SimpleStringProperty(status);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getBookTitle() {
        return bookTitle.get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle.set(bookTitle);
    }

    public String getBorrowDate() {
        return borrowDate.get();
    }

    public void setBorrowDate(String borrowDate) {
        this.borrowDate.set(borrowDate);
    }

    public String getDueDate() {
        return dueDate.get();
    }

    public void setDueDate(String dueDate) {
        this.dueDate.set(dueDate);
    }

    public String getReturnDate() {
        return returnDate.get();
    }

    public void setReturnDate(String returnDate) {
        this.returnDate.set(returnDate);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}