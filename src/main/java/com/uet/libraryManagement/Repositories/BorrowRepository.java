package com.uet.libraryManagement.Repositories;

import com.uet.libraryManagement.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BorrowRepository {

    private static BorrowRepository instance;

    private BorrowRepository() {}

    public static BorrowRepository getInstance() {
        if (instance == null) {
            instance = new BorrowRepository();
        }
        return instance;
    }

    public int getUserIssuedDocs(int userId) {
        String query = "SELECT COUNT(*) FROM borrow_history WHERE user_id = ?";
        int count = 0;
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getNumberOfDocBorrowed() {
        updateOverdueStatus();
        String query = "SELECT COUNT(*) FROM borrow_history WHERE status = 'borrowed'";
        int count = 0;
        try (ResultSet rs = ConnectJDBC.executeQuery(query)) {
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public List<BorrowHistory> getUserBorrowRecords(int currentUserId) {
        updateOverdueStatus();
        List<BorrowHistory> borrowRecords = new ArrayList<>();
        String query = "SELECT borrow_history.id, borrow_date, due_date, return_date, status, " +
                "CASE WHEN doc_type = 'book' THEN books.title " +
                "WHEN doc_type = 'thesis' THEN theses.title END AS docTitle " +
                "FROM borrow_history " +
                "LEFT JOIN books ON borrow_history.doc_id = books.id AND doc_type = 'book' " +
                "LEFT JOIN theses ON borrow_history.doc_id = theses.id AND doc_type = 'thesis' " +
                "WHERE user_id = ? AND status = 'borrowed'" +
                "ORDER BY borrow_date DESC";

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, currentUserId)) {
            while (rs.next()) {
                borrowRecords.add(getBorrowHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowRecords;
    }

    public List<BorrowHistory> getAllHistoryByUserId(int userId, String docType) {
        updateOverdueStatus();
        List<BorrowHistory> historyList = new ArrayList<>();
        String query;

        // Choose query based on document type
        if ("Books".equalsIgnoreCase(docType)) {
            query = "SELECT borrow_history.id, books.title AS docTitle, borrow_date, due_date, return_date, status "
                    + "FROM borrow_history "
                    + "JOIN books ON borrow_history.doc_id = books.id "
                    + "WHERE borrow_history.user_id = ? "
                    + "AND doc_type = 'book'";
        } else {
            query = "SELECT borrow_history.id, theses.title AS docTitle, borrow_date, due_date, return_date, status "
                    + "FROM borrow_history "
                    + "JOIN theses ON borrow_history.doc_id = theses.id "
                    + "WHERE borrow_history.user_id = ? "
                    + "AND doc_type = 'thesis'";
        }

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String docTitle = rs.getString("docTitle");
                String borrowDate = rs.getString("borrow_date");
                String dueDate = rs.getString("due_date");
                String returnDate = rs.getString("return_date");
                if (returnDate == null) {
                    returnDate = "N/A";
                }
                String status = rs.getString("status");
                historyList.add(new BorrowHistory(id, docTitle, borrowDate, dueDate, returnDate, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }

    public String getDocType(int userId, int borrowId) {
        String typeQuery = "SELECT doc_type FROM borrow_history WHERE user_id = ? AND id = ?";
        String docType = null;
        try (ResultSet rsType = ConnectJDBC.executeQueryWithParams(typeQuery, userId, borrowId)) {
            if (rsType.next()) {
                docType = rsType.getString("doc_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return docType;
    }

    public Document getRecentDocument(int userId, int borrowId) {
        Document document = null;
        // Query to determine document type (book or thesis)
        String typeQuery = "SELECT doc_type FROM borrow_history WHERE user_id = ? AND id = ?";
        String docType = null;

        try (ResultSet rsType = ConnectJDBC.executeQueryWithParams(typeQuery, userId, borrowId)) {
            if (rsType.next()) {
                docType = rsType.getString("doc_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Query to get document details based on the document type
        String query = docType.equalsIgnoreCase("book")
                ? "SELECT b.* FROM borrow_history br JOIN books b ON br.doc_id = b.id WHERE br.user_id = ? AND br.id = ?"
                : "SELECT t.* FROM borrow_history br JOIN theses t ON br.doc_id = t.id WHERE br.user_id = ? AND br.id = ?";

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId, borrowId)) {
            if (rs.next()) {
                int id = rs.getInt("id");
                String titleValue = rs.getString("title");
                String authorValue = rs.getString("author");
                String publisher = rs.getString("publisher");
                String year = rs.getString("publishDate");
                String description = rs.getString("description");
                String genre = rs.getString(docType.equalsIgnoreCase("book") ? "genre" : "field");
                String url = rs.getString("thumbnail");
                String isbn10Value = rs.getString("isbn10");
                String isbn13Value = rs.getString("isbn13");

                document = docType.equalsIgnoreCase("book")
                        ? new Book(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value)
                        : new Thesis(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    public Document getDocument(int userId, int borrowId, String docType) {
        Document document = null;
        String query;
        boolean isBook = "Books".equalsIgnoreCase(docType);

        query = isBook
                ? "SELECT b.* FROM borrow_history br JOIN books b ON br.doc_id = b.id WHERE user_id = ? AND br.id = ?"
                : "SELECT t.* FROM borrow_history br JOIN theses t ON br.doc_id = t.id WHERE user_id = ? AND br.id = ?";

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId, borrowId)) {
            if (rs.next()) {
                int id = rs.getInt("id");
                String titleValue = rs.getString("title");
                String authorValue = rs.getString("author");
                String publisher = rs.getString("publisher");
                String year = rs.getString("publishDate");
                String description = rs.getString("description");
                String genre = rs.getString(isBook ? "genre" : "field");
                String url = rs.getString("thumbnail");
                String isbn10Value = rs.getString("isbn10");
                String isbn13Value = rs.getString("isbn13");
                document = isBook ?
                        new Book(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value) :
                        new Thesis(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return document;
    }

    // Method to get filtered history based on dynamic query and parameters
    public List<BorrowHistory> getFilteredHistory(String query, Object[] params) {
        List<BorrowHistory> historyList = new ArrayList<>();
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, params)) {
            while (rs.next()) {
                historyList.add(getBorrowHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyList;
    }

    // Method to get borrowing history by userId and title for search functionality
    public List<BorrowHistory> getAllHistoryByTitle(int userId, String docType, String title) {
        List<BorrowHistory> historyList = new ArrayList<>();
        String query;
        if ("Books".equalsIgnoreCase(docType)) {
            query = "SELECT borrow_history.id, books.title AS docTitle, borrow_date, due_date, return_date, status "
                    + "FROM borrow_history "
                    + "JOIN books ON borrow_history.doc_id = books.id "
                    + "WHERE borrow_history.user_id = ? AND doc_type = 'book' AND books.title LIKE ?";
        } else {
            query = "SELECT borrow_history.id, theses.title AS docTitle, borrow_date, due_date, return_date, status "
                    + "FROM borrow_history "
                    + "JOIN theses ON borrow_history.doc_id = theses.id "
                    + "WHERE borrow_history.user_id = ? AND doc_type = 'thesis' AND theses.title LIKE ?";
        }

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId, "%" + title + "%")) {
            while (rs.next()) {
                historyList.add(getBorrowHistory(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }


    private BorrowHistory getBorrowHistory(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String docTitle = rs.getString("docTitle");
        String borrowDate = rs.getString("borrow_date");
        String dueDate = rs.getString("due_date");
        String returnDate = rs.getString("return_date");
        String status = rs.getString("status");
        return new BorrowHistory(id, docTitle, borrowDate, dueDate, returnDate, status);
    }

    public boolean hasUserAlreadyBorrowedDocument(int userId, int docId, String docType) {
        String query = "SELECT COUNT(*) FROM borrow_history WHERE user_id = ? AND doc_id = ? AND doc_type = ? AND status = 'borrowed'";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId, docId, docType)) {
            // Check if a result is returned
            if (rs.next()) {
                return rs.getInt(1) > 0;  // If COUNT(*) > 0, it means the document is already borrowed by the user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void borrowDocument(int userId, int docId, String docType) {
        String maxIdQuery = "SELECT MAX(id) FROM borrow_history";
        try (ResultSet maxIdRs = ConnectJDBC.executeQuery(maxIdQuery)) {
            if (maxIdRs.next()) {
                int maxId = maxIdRs.getInt(1);
                String resetAutoIncrementQuery = "ALTER TABLE borrow_history AUTO_INCREMENT = " + (maxId + 1);
                ConnectJDBC.executeUpdate(resetAutoIncrementQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String query = "INSERT INTO borrow_history (user_id, doc_type, doc_id, borrow_date, due_date, status) VALUES (?, ?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 14 DAY), 'borrowed')";
        ConnectJDBC.executeUpdate(query, userId, docType, docId);
    }

    public void updateDocumentQuantity(int docId, String docType, int newQuantity) {
        String query;
        if ("book".equals(docType)) {
            query = "UPDATE books SET quantity = ? WHERE id = ?";
        } else {
            query = "UPDATE theses SET quantity = ? WHERE id = ?";
        }
        ConnectJDBC.executeUpdate(query, newQuantity, docId);
    }

    public void increaseDocumentQuantity(int docId, String docType) {
        String query;
        if ("book".equals(docType)) {
            query = "UPDATE books SET quantity = quantity + 1 WHERE id = ?";
        } else {
            query = "UPDATE theses SET quantity = quantity + 1 WHERE id = ?";
        }
        ConnectJDBC.executeUpdate(query, docId);
    }

    public void updateOverdueStatus() {
        String query = "UPDATE borrow_history SET status = 'overdue' WHERE due_date <= CURRENT_DATE AND status = 'borrowed'";
        ConnectJDBC.executeUpdate(query);
    }
}