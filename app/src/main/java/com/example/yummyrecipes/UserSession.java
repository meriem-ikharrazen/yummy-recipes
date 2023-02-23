package com.example.yummyrecipes;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private static UserSession instance;
    private User user;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
