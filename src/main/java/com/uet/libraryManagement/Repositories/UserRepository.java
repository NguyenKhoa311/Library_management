package com.uet.libraryManagement.Repositories;

import com.uet.libraryManagement.ConnectJDBC;
import com.uet.libraryManagement.User;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;

    public UserRepository() {}

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    /**
     * only use for JUnit test.
     * @param userRepositoryMock instance to set.
     */
    public static void setInstance(UserRepository userRepositoryMock) {
        instance = userRepositoryMock;
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

    public int getNumberOfUsers() {
        String query = "SELECT COUNT(*) FROM users WHERE role = 'user'";
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

    public int getNumberOfUsersIssuing() {
        String query = "SELECT COUNT(DISTINCT user_id) FROM borrow_history WHERE status = 'borrowed'";
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

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (ResultSet rs = ConnectJDBC.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String userName = rs.getString("user_name");
                String pass = rs.getString("password");
                String fullName = rs.getString("fullName"); // can be null
                String email = rs.getString("email");
                String birthday = rs.getString("birthday"); // Can be null
                String image = rs.getString("image"); // Can be null
                String phone = rs.getString("phone"); // Can be null
                String role = rs.getString("role");
                users.add(new User(id, userName, pass, fullName, birthday, email, image, phone, role)); // Adjust the constructor if necessary
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public List<User> searchUsers(String searchTerm) {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE user_name LIKE ? OR id LIKE ? OR phone LIKE ?";

        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, "%" + searchTerm + "%", "%" + searchTerm + "%", "%" + searchTerm + "%")) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("id"),
                        rs.getString("user_name"),
                        rs.getString("password"),
                        rs.getString("fullName"),
                        rs.getString("birthday"),
                        rs.getString("email"),
                        rs.getString("image"),
                        rs.getString("phone"),
                        rs.getString("role")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean isUsernameTaken(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE user_name = ?";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, username)) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if username exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // create new document
    public boolean create(User user) {
        if (isUsernameTaken(user.getUsername())) {
            return false; // Return false if the username is already taken
        }

        String maxIdQuery = "SELECT MAX(id) FROM users";
        try (ResultSet maxIdRs = ConnectJDBC.executeQuery(maxIdQuery)) {
            if (maxIdRs.next()) {
                int maxId = maxIdRs.getInt(1);
                String resetAutoIncrementQuery = "ALTER TABLE users AUTO_INCREMENT = " + (maxId + 1);
                ConnectJDBC.executeUpdate(resetAutoIncrementQuery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String insertQuery = "INSERT INTO users (user_name, password, email, role) VALUES (?, ?, ?, ?)";
        ConnectJDBC.executeUpdate(insertQuery, user.getUsername(), user.getPassword(), user.getEmail(), user.getRole());
        System.out.println("User registered successfully.");
        return true;
    }

    // checking if user is borrowing documents
    public boolean isUserBorrowingDocuments(int userId) {
        String query = "SELECT COUNT(*) FROM borrow_history WHERE user_id = ? AND return_date IS NULL";
        try (ResultSet rs = ConnectJDBC.executeQueryWithParams(query, userId)) {
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu có ít nhất 1 tài liệu chưa trả
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // edit document
    public void updateProfile(User user) throws IOException {
        String query = "UPDATE users SET fullName = ?, birthday = ?, email = ?, image = ?, phone = ? WHERE id = ?";
        ConnectJDBC.executeUpdate(query, user.getFullName(), user.getBirthday(), user.getEmail(), user.getAvatar(), user.getPhone(), user.getId());
    }

    public void updatePassword(User user) {
        String query = "UPDATE users SET password = ? WHERE id = ?";
        ConnectJDBC.executeUpdate(query, user.getPassword(), user.getId());
    }

    // delete document
    public void deleteUser(int userID) {
        String query = "DELETE FROM users WHERE id = ?";
        ConnectJDBC.executeUpdate(query, userID);
    }
}
