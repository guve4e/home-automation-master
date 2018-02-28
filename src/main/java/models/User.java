package models;

import user.UserDAO;

public class User {

    private int userId;
    private String username;
    private String password;
    private String hash;
    private boolean isAuthenticated;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public User authenticate(String username, String password) {
        UserDAO userDAO = new UserDAO();
        User user = null;
        user = userDAO.authenticateUser(username, password);

        if (user != null) {
            this.isAuthenticated = true;
            return user;
        } else {
            return null;
        }
    }

    public String getHash() {
        return hash;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
