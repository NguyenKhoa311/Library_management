package com.uet.libraryManagement.Managers;

import com.uet.libraryManagement.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    /**
     * only use for JUnit test.
     * @param sessionManagerMock instance to set.
     */
    public static void setInstance(SessionManager sessionManagerMock) {
        instance = sessionManagerMock;
    }

    public void setUser(User user) {
        this.currentUser = user;
    }

    public User getUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public void logout() {
        currentUser = null;
    }
}
