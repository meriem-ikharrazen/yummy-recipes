package com.example.yummyrecipes;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    private static UserSession instance;
    // Pour pour stocker une liste d'utilisateurs connectés au lieu d'un singleton
    private List<User> users;

    private UserSession() {
        users = new ArrayList<>();
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getUsers() {
        return users;
    }
}
