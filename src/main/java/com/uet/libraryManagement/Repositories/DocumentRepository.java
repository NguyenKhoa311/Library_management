package com.uet.libraryManagement.Repositories;

import com.uet.libraryManagement.Book;
import com.uet.libraryManagement.ConnectJDBC;
import com.uet.libraryManagement.Document;
import com.uet.libraryManagement.Thesis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class DocumentRepository {
    public DocumentRepository() {
        loadDatabase();
    }

    public abstract String getDbTable();

    // override in child class
    protected void loadDatabase() {

    }

    public int getNumberOfDocuments() {
        String query = "SELECT SUM(quantity) FROM " + getDbTable();
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

    public ObservableList<Document> getRecentAddedDocuments() {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        String query = "SELECT * FROM books "
                        + "UNION ALL "
                        + "SELECT * FROM theses "
                        + "ORDER BY createdDate DESC "
                        + "LIMIT 5";
        try (ResultSet rs = ConnectJDBC.executeQuery(query)) {
            getDocuments(rs, documents);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return documents;
    }

    // get all books from database
    public ObservableList<Document> getAllBooks() {
        ObservableList<Document> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + getDbTable();

        try (ResultSet rs = ConnectJDBC.executeQuery(query)) {
            getDocuments(rs, books);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // get all books from database
    public ObservableList<Document> getAllTheses() {
        ObservableList<Document> theses = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + getDbTable();

        try (ResultSet rs = ConnectJDBC.executeQuery(query)) {
            getDocuments(rs, theses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theses;
    }

    // find document by filter
    public ObservableList<Document> getFilteredDocuments(String title, String author, String category, String startYear, String endYear, String isbn10, String isbn13) {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        // Start the base query
        String query = "SELECT * FROM " + getDbTable() + " WHERE 1=1"; // 1=1 is a placeholder for adding conditions

        // Create a list to store parameters for the prepared statement
        List<Object> parameters = new ArrayList<>();

        // Add filters if they are provided
        if (title != null && !title.isEmpty()) {
            query += " AND title LIKE ?";
            parameters.add("%" + title + "%"); // Add wildcards for partial matching
        }
        if (author != null && !author.isEmpty()) {
            query += " AND author LIKE ?";
            parameters.add("%" + author + "%");
        }
        if (category != null && !category.isEmpty()) {
            query += " AND " + (getDbTable().equals("books") ? "genre" : "field") + " LIKE ?";
            parameters.add("%" + category + "%");
        }
        if (startYear != null && !startYear.isEmpty()) {
            query += " AND publishDate >= ?";
            parameters.add(startYear);
        }
        if (endYear != null && !endYear.isEmpty()) {
            query += " AND publishDate <= ?";
            parameters.add(endYear);
        }
        if (isbn10 != null && !isbn10.isEmpty()) {
            query += " AND isbn = ?";
            parameters.add(isbn10);
        }
        if (isbn13 != null && !isbn13.isEmpty()) {
            query += " AND isbn13 = ?";
            parameters.add(isbn13);
        }

        // Execute the query with the parameters
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, parameters.toArray())) {
            getDocuments(rs, documents);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public ObservableList<Document> searchDocument(String search) {
        ObservableList<Document> documents = FXCollections.observableArrayList();
        search = "%" + search + "%";
        // Start the base query
        String query = "SELECT * FROM " + getDbTable()
                + " WHERE title LIKE ?"
                + " OR author LIKE ?"
                + " OR publisher LIKE ?"
                + " OR " + (getDbTable().equals("books") ? "genre" : "field") + " LIKE ?"
                + " OR isbn10 LIKE ?"
                + " OR isbn13 LIKE ?";

        // Execute the query with the parameters
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, search, search, search, search, search, search)) {
            getDocuments(rs, documents);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    // create new document
    public void create(Document document, int quantity) {
        Object[] params;
        String checkQuery;
        String insertQuery = "INSERT INTO " + getDbTable() + " (title, author, publisher, publishDate, description, "
                + (getDbTable().equals("books") ? "genre" : "field") + ", thumbnail, isbn10, isbn13, quantity, createdDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // check and reset AUTO_INCREMENT if needed
        String maxIdQuery = "SELECT MAX(id) FROM " + getDbTable();
        try (ResultSet maxIdRs = ConnectJDBC.executeQuery(maxIdQuery)) {
            if (maxIdRs.next()) {
                int maxId = maxIdRs.getInt(1);
                String resetAutoIncrementQuery = "ALTER TABLE " + getDbTable() + " AUTO_INCREMENT = " + (maxId + 1);
                ConnectJDBC.executeUpdate(resetAutoIncrementQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Special handling for "N/A" ISBN
        if ("N/A".equals(document.getIsbn10()) && "N/A".equals(document.getIsbn13())) {
            // If both ISBNs are N/A, use a different uniqueness check
            checkQuery = "SELECT COUNT(*) FROM " + getDbTable() +
                    " WHERE title = ? AND author = ? AND publishDate = ?";
            params = new Object[]{
                    document.getTitle(),
                    document.getAuthor(),
                    document.getYear()
            };
        } else {
            // Original ISBN-based check
            checkQuery = "SELECT COUNT(*) FROM " + getDbTable() +
                    " WHERE (isbn10 = ? OR isbn13 = ?) AND isbn10 != 'N/A' AND isbn13 != 'N/A'";
            params = new Object[]{
                    document.getIsbn10(),
                    document.getIsbn13()
            };
        }

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(checkQuery, params)) {
            // check if document already exists
            if (rs.next() && rs.getInt(1) > 0) {
                showAlert("Document already exists in the database !");
                System.out.println("Document already exists in the database.");
                return;
            }

            // add new document
            ConnectJDBC.executeUpdate(insertQuery, document.getTitle(), document.getAuthor(), document.getPublisher(),
                    document.getYear(), document.getDescription(), document.getCategory(),
                    document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13(),
                    quantity, Timestamp.valueOf(LocalDateTime.now()));
            showAlert("Document inserted successfully !");
            System.out.println("Document inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // edit document
    public void update(Document document) {
        String query = "UPDATE " + getDbTable() + " SET title = ?, author = ?, publisher = ?, publishDate = ?, description = ?, "
                + (getDbTable().equals("books") ? "genre" : "field") + " = ?, thumbnail = ?, isbn10 = ?, isbn13 = ?, quantity = ? WHERE id = ?";
        ConnectJDBC.executeUpdate(query, document.getTitle(), document.getAuthor(), document.getPublisher(),
                document.getYear(), document.getDescription(), document.getCategory(),
                document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13(), document.getQuantity(), document.getId());
    }

    // delete document
    public void delete(Document document) {
        String query = "DELETE FROM " + getDbTable() + " WHERE id = ?";
        ConnectJDBC.executeUpdate(query, document.getId());
    }

    public void deleteAll() {
        String query = "DELETE FROM " + getDbTable();
        ConnectJDBC.executeUpdate(query);
    }

    public void getDocuments(ResultSet rs, ObservableList<Document> documents) throws SQLException {
        while (rs.next()) {
            int id = rs.getInt("id");
            String titleValue = rs.getString("title");
            String authorValue = rs.getString("author");
            String publisher = rs.getString("publisher");
            String year = rs.getString("publishDate");
            String description = rs.getString("description");
            String genre = rs.getString(getDbTable().equals("books") ? "genre" : "field");
            String url = rs.getString("thumbnail");
            String isbn10Value = rs.getString("isbn10");
            String isbn13Value = rs.getString("isbn13");
            int quantityValue = rs.getInt("quantity");
            Document document = getDbTable().equals("books") ?
                    new Book(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value, quantityValue) :
                    new Thesis(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value, quantityValue);
            documents.add(document);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}