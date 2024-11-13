package com.uet.libraryManagement;

import java.sql.*;

public class BookRepository {
    protected String db_table;
    protected final ConnectJDBC connectJDBC;

    public BookRepository() {
        this.connectJDBC = new ConnectJDBC();
        loadDatabase();
    }

    protected void loadDatabase() {
        db_table = "books";
    }

    public void create(Book book) {
        String checkQuery = "SELECT COUNT(*) FROM " + db_table + " WHERE isbn10 = ? OR isbn13 = ?";
        String insertQuery = "INSERT INTO " + db_table + " (title, author, publisher, publishDate, description, genre, thumbnail, isbn10, isbn13) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Kiểm tra sách đã tồn tại chưa
        ResultSet rs = connectJDBC.executeQueryWithParams(checkQuery, book.getIsbn10(), book.getIsbn13());
        try {
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Book already exists in the database.");
                return; // Không chèn sách nữa nếu đã tồn tại
            }

            // Chèn sách mới nếu không tồn tại
            connectJDBC.executeUpdate(insertQuery, book.getTitle(), book.getAuthor(), book.getPublisher(),
                    book.getYear(), book.getDescription(), book.getGenre(),
                    book.getThumbnailUrl(), book.getIsbn10(), book.getIsbn13());
            System.out.println("Book inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Book book) {
        String query = "UPDATE " + db_table + " SET title = ?, author = ?, publisher = ?, publishDate = ?, description = ?, genre = ?, thumbnail = ?, isbn10 = ?, isbn13 = ?, WHERE id = ?";
        connectJDBC.executeUpdate(query, book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear(),
                book.getDescription(), book.getGenre(), book.getThumbnailUrl(), book.getIsbn10(), book.getIsbn13(), book.getId());
    }

    public void delete(Book book) {
        String query = "DELETE FROM " + db_table + " WHERE id = ?";
        connectJDBC.executeUpdate(query, book.getId());
    }

    public void deleteAll() {
        String query = "DELETE FROM " + db_table;
        connectJDBC.executeUpdate(query);
    }
}
