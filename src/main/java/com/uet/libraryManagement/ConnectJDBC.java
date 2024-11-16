package com.uet.libraryManagement;

import java.sql.*;

public class ConnectJDBC {

    public Connection connect() {
        Connection conn;
        try {
            String url = "jdbc:mysql://localhost:3307/library_db";
            String username = "root";
            String password = "";
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    public ResultSet executeQuery(String query) {
        Statement statement;
        ResultSet rs;
        try {
            statement = connect().createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public ResultSet executeQueryWithParams(String query, Object... params) {
        PreparedStatement statement;
        ResultSet rs;
        try {
            statement = connect().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                if(query.contains("LIKE ?"))
                {
                    statement.setObject(i + 1, "%" + params[i] + "%");
                }
                else statement.setObject(i + 1, params[i]);
            }

            rs = statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return rs;
    }

    public void executeUpdate(String query, Object... params) {
        PreparedStatement statement;
        try {
            statement = connect().prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                statement.setObject(i + 1, params[i]);
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
