package com.uet.libraryManagement;

import javafx.fxml.FXML;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class RatingComment {
    private String username;
    private int userId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public RatingComment(String username, int userId, int rating, String comment, Timestamp createdAt) {
        this.username = username;
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public RatingComment(int userId, int rating, String comment, Timestamp createdAt) {
        this.userId = userId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timeString = createdAt != null ? createdAt.toLocalDateTime().format(formatter) : "Unknown time";
        return getUsername() + " (" + timeString + "): " + getComment();
    }
}
