package com.uet.libraryManagement;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    private static UserRepository instance;

    public UserRepository() {}

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    // check existed user
    public User validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE user_name = ? AND password = ?";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, username, password)) {
            if (rs.next()) {
                // Retrieve user data if login is successful
                int id = rs.getInt("id");
                String userName = rs.getString("user_name");
                String pass = rs.getString("password");
                String fullName = rs.getString("fullName"); // can be null
                String email = rs.getString("email");
                String birthday = rs.getString("birthday"); // Can be null
                String image = rs.getString("image"); // Can be null
                String phone = rs.getString("phone"); // Can be null
                String role = rs.getString("role");
                return new User(id, userName, pass, fullName, birthday, email, image, phone, role); // Adjust the constructor if necessary
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }

    // create new document
    public boolean create(User user) {
        String insertQuery = "INSERT INTO users (user_name, password, email, role) VALUES (?, ?, ?, ?)";
        ConnectJDBC.executeUpdate(insertQuery, user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
        System.out.println("User registered successfully.");
        return true;
    }

    // edit document
    public void updateProfile(User user) {
        String query = "UPDATE users SET fullName = ?, birthday = ?, email = ?, image = ?, phone = ? WHERE id = ?";
        ConnectJDBC.executeUpdate(query, user.getFullName(), user.getBirthday(), user.getEmail(), user.getAvatarUrl(), user.getPhone(), user.getId());
    }

    public void updatePassword(User user) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        ConnectJDBC.executeUpdate(query, user.getPassword(), user.getId());
    }

    // delete document
    public void delete(User user) {
        String query = "DELETE FROM users WHERE id = ?";
        ConnectJDBC.executeUpdate(query, user.getId());
    }
}
