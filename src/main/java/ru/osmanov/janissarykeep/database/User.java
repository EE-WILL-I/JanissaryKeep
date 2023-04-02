package ru.osmanov.janissarykeep.database;

import ru.osmanov.janissarykeep.Application;

public class User {
    private String id;
    public User(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public static User get() {
        return Application.getInstance().getLoggedUser();
    }
}
