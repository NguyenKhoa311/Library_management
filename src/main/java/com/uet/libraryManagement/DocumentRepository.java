package com.uet.libraryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class DocumentRepository {
    protected String dbTable;
    protected final ConnectJDBC connectJDBC;

    public DocumentRepository() {
        this.connectJDBC = new ConnectJDBC();
        loadDatabase();
    }

    public abstract String getDbTable();

    // override in child class
    protected void loadDatabase() {

    }

    // get all books from database
    public ObservableList<Document> getAllBooks() {
        ObservableList<Document> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + getDbTable();

        try (ResultSet rs = connectJDBC.executeQuery(query)) {
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

        try (ResultSet rs = connectJDBC.executeQuery(query)) {
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
            query += " AND isbn LIKE ?";
            parameters.add(isbn10);
        }
        if (isbn13 != null && !isbn13.isEmpty()) {
            query += " AND isbn13 LIKE ?";
            parameters.add(isbn13);
        }

        // Execute the query with the parameters
        try (ResultSet rs = connectJDBC.executeQueryWithParams(query, parameters.toArray())) {
            getDocuments(rs, documents);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    public ObservableList<Document> searchDocument(String search) {
        ObservableList<Document> documents = FXCollections.observableArrayList();

        // Start the base query
        String query = "SELECT * FROM " + getDbTable()
                + " WHERE title LIKE ?"
                + " OR author LIKE ?"
                + " OR publisher LIKE ?"
                + " OR " + (getDbTable().equals("books") ? "genre" : "field") + " LIKE ?"
                + " OR isbn10 LIKE ?"
                + " OR isbn13 LIKE ?";

        // Execute the query with the parameters
        try (ResultSet rs = connectJDBC.executeQueryWithParams(query, search, search, search, search, search, search)) {
            getDocuments(rs, documents);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return documents;
    }

    // create new document
    public void create(Document document) {
        String checkQuery = "SELECT COUNT(*) FROM " + getDbTable() + " WHERE isbn10 = ? OR isbn13 = ?";
        String insertQuery = "INSERT INTO " + getDbTable() + " (title, author, publisher, publishDate, description, "
                + (getDbTable().equals("books") ? "genre" : "field") + ", thumbnail, isbn10, isbn13) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // check and reset AUTO_INCREMENT if needed
        String maxIdQuery = "SELECT MAX(id) FROM " + getDbTable();
        try (ResultSet maxIdRs = connectJDBC.executeQuery(maxIdQuery)) {
            if (maxIdRs.next()) {
                int maxId = maxIdRs.getInt(1);
                String resetAutoIncrementQuery = "ALTER TABLE " + getDbTable() + " AUTO_INCREMENT = " + (maxId + 1);
                connectJDBC.executeUpdate(resetAutoIncrementQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (ResultSet rs = connectJDBC.executeQueryWithParams(checkQuery, document.getIsbn10(), document.getIsbn13())) {
            // check if document already exists
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Document already exists in the database.");
                return;
            }

            // add new document
            connectJDBC.executeUpdate(insertQuery, document.getTitle(), document.getAuthor(), document.getPublisher(),
                    document.getYear(), document.getDescription(), document.getCategory(),
                    document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13());
            System.out.println("Document inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // edit document
    public void update(Document document) {
        String query = "UPDATE " + getDbTable() + " SET title = ?, author = ?, publisher = ?, publishDate = ?, description = ?, "
                + (getDbTable().equals("books") ? "genre" : "field") + " = ?, thumbnail = ?, isbn10 = ?, isbn13 = ? WHERE id = ?";
        connectJDBC.executeUpdate(query, document.getTitle(), document.getAuthor(), document.getPublisher(),
                document.getYear(), document.getDescription(), document.getCategory(),
                document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13(), document.getId());
    }

    // delete document
    public void delete(Document document) {
        String query = "DELETE FROM " + getDbTable() + " WHERE id = ?";
        connectJDBC.executeUpdate(query, document.getId());
    }

    public void deleteAll() {
        String query = "DELETE FROM " + getDbTable();
        connectJDBC.executeUpdate(query);
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
            Document document = getDbTable().equals("books") ?
                    new Book(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value) :
                    new Thesis(id, titleValue, authorValue, publisher, description, year, genre, url, isbn10Value, isbn13Value);
            documents.add(document);
        }
    }
}