package com.uet.libraryManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

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

    public ObservableList<Document> getAllBooks() {
        ObservableList<Document> books = FXCollections.observableArrayList();
        String query = "SELECT * FROM " + getDbTable();

        try (ResultSet rs = connectJDBC.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String year = rs.getString("publishDate");
                String description = rs.getString("description");
                String genre = rs.getString("genre");
                String url = rs.getString("thumbnail");
                String isbn10 = rs.getString("isbn10");
                String isbn13 = rs.getString("isbn13");

                Book book = new Book(id, title, author, publisher, description, year, genre, url, isbn10, isbn13);
                books.add(book);
            }
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
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String publisher = rs.getString("publisher");
                String year = rs.getString("publishDate");
                String description = rs.getString("description");
                String genre = rs.getString("field");
                String url = rs.getString("thumbnail");
                String isbn10 = rs.getString("isbn10");
                String isbn13 = rs.getString("isbn13");

                Thesis thesis = new Thesis(id, title, author, publisher, description, year, genre, url, isbn10, isbn13);
                theses.add(thesis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return theses;
    }

    public void create(Document document) {
        String checkQuery = "SELECT COUNT(*) FROM " + getDbTable() + " WHERE isbn10 = ? OR isbn13 = ?";
        String insertQuery = "INSERT INTO " + getDbTable() + " (title, author, publisher, publishDate, description, "
                + (getDbTable().equals("books") ? "genre" : "field") + ", thumbnail, isbn10, isbn13) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (ResultSet rs = connectJDBC.executeQueryWithParams(checkQuery, document.getIsbn10(), document.getIsbn13())) {
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Document already exists in the database.");
                return;
            }

            connectJDBC.executeUpdate(insertQuery, document.getTitle(), document.getAuthor(), document.getPublisher(),
                    document.getYear(), document.getDescription(), document.getCategory(),
                    document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13());
            System.out.println("Document inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Document document) {
        String query = "UPDATE " + getDbTable() + " SET title = ?, author = ?, publisher = ?, publishDate = ?, description = ?, "
                + (getDbTable().equals("books") ? "genre" : "field") + " = ?, thumbnail = ?, isbn10 = ?, isbn13 = ? WHERE id = ?";
        connectJDBC.executeUpdate(query, document.getTitle(), document.getAuthor(), document.getPublisher(),
                document.getYear(), document.getDescription(), document.getCategory(),
                document.getThumbnailUrl(), document.getIsbn10(), document.getIsbn13(), document.getId());
    }

    public void delete(Document document) {
        String query = "DELETE FROM " + getDbTable() + " WHERE id = ?";
        connectJDBC.executeUpdate(query, document.getId());
    }

    public void deleteAll() {
        String query = "DELETE FROM " + getDbTable();
        connectJDBC.executeUpdate(query);
    }
}