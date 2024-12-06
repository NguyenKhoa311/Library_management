package com.uet.libraryManagement.Repositories;

import com.uet.libraryManagement.ConnectJDBC;
import com.uet.libraryManagement.RatingComment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class RatingRepository {
    private static RatingRepository instance;

    public RatingRepository() {}

    public static synchronized RatingRepository getInstance() {
        if (instance == null) {
            instance = new RatingRepository();
        }
        return instance;
    }

    public int getRatingNum(int documentId, String docType) {
        int num = 0;
        String query = "SELECT COUNT(*) AS num FROM rating_comment WHERE doc_id=? AND doc_type=?";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, documentId, docType)) {
            if (rs.next()) {
                num = rs.getInt("num");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num;
    }

    public int getRating(int documentId, String docType) {
        int rating = 0;
        String query = "SELECT AVG(rating) AS rating FROM rating_comment WHERE doc_id=? AND doc_type=?";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, documentId, docType)) {
            if (rs.next()) {
                rating = rs.getInt("rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rating;
    }

    public void addComment(int documentId, String docType, RatingComment newComment) {
        String insertQuery = "INSERT INTO rating_comment (doc_id, doc_type, user_id, rating, comment, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        ConnectJDBC.executeUpdate(insertQuery, documentId, docType.equals("Books") ? "book" : "thesis", newComment.getUserId(),
                newComment.getRating(), newComment.getComment(), newComment.getCreatedAt());
    }

    public ObservableList<RatingComment> getCommentsByDocumentId(int documentId, String docType) {
        ObservableList<RatingComment> comments = FXCollections.observableArrayList();
        String normalizedDocType;
        if (docType.equalsIgnoreCase("book") || docType.equalsIgnoreCase("books")) {
            normalizedDocType = "book";
        } else if (docType.equalsIgnoreCase("thesis") || docType.equalsIgnoreCase("theses")) {
            normalizedDocType = "thesis";
        } else {
            throw new IllegalArgumentException("Invalid document type: " + docType);
        }
        String query = "SELECT r.user_id, r.rating, r.comment, r.created_at, u.user_name " +
                "FROM rating_comment r " +
                "JOIN users u ON r.user_id = u.id " +
                "WHERE r.doc_id = ? AND r.doc_type = ?";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, documentId, normalizedDocType)) {
            while (rs.next()) {
                String userName = rs.getString("user_name");
                int userId = rs.getInt("user_id");
                int rating = rs.getInt("rating");
                String commentText = rs.getString("comment");
                Timestamp createdAt = rs.getTimestamp("created_at");
                comments.add(new RatingComment(userName, userId, rating, commentText, createdAt));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comments;
    }
}
