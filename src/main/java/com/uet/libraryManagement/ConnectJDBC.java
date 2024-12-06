package com.uet.libraryManagement;

import java.sql.*;

public class ConnectJDBC {

    private static final String DB_URL =
            "jdbc:mysql://library-db.chyo8scokfws.ap-southeast-1.rds.amazonaws.com:3310/library_db";
    private static final String DB_USERNAME = "admin";
    private static final String DB_PASSWORD = "admindeptrai123";

    private static Connection connection;

    // Phương thức khởi tạo kết nối (chỉ tạo một lần)
    static {
        try {
            // Nạp driver MySQL
//            Class.forName("com.mysql.cj.jdbc.Driver");

            // Tạo kết nối
//            String url = "jdbc:mysql://localhost:3307/library_db";
//            String username = "root";
//            String password = "";
//            connection = DriverManager.getConnection(url, username, password);
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Kết nối thành công tới AWS RDS!");
//        } catch (ClassNotFoundException e) {
//            System.err.println("Không tìm thấy driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối tới cơ sở dữ liệu: " + e.getMessage());
        }
    }

    public static void connect() {
        if (connection == null) {
            try {
//                String url = "jdbc:mysql://localhost:3307/library_db";
//                String username = "root";
//                String password = "";
//                connection = DriverManager.getConnection(url, username, password);
                connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
                System.out.println("Kết nối thành công tới AWS RDS!");
            } catch (SQLException e) {
                System.err.println("Lỗi khi kết nối tới cơ sở dữ liệu: " + e.getMessage());
            }
        }
    }

    public static ResultSet executeQuery(String query) {
        Statement statement;
        ResultSet rs;
        try {
            statement = connection.createStatement();
            rs = statement.executeQuery(query);
            System.out.println("executeQuery: " + query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public static ResultSet executeQueryWithParams(String query, Object... params) {
        PreparedStatement statement;
        ResultSet rs;
        try {
            statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            System.out.println("Executing prepared statement: " + statement.toString());
            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public static void executeUpdate(String query, Object... params) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }
            System.out.println("UPDATE: " + query);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
