package controllers;

import models.User;

public class LoginController {
    private String loginView;
    private User userModel;

    /**
     *
     * @param loginView
     * @param userModel
     */
    public LoginController(String loginView, User userModel) {

        this.loginView = loginView;
        this.userModel = userModel;

    }
}
