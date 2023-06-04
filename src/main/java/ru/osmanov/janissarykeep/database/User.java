package ru.osmanov.janissarykeep.database;

import org.bson.Document;
import org.bson.types.ObjectId;
import ru.osmanov.janissarykeep.Application;

/**
 * Класс с данными о пользователе
 * **/
public class User {
    private String id;
    private boolean isAdmin;

    public User(String id, boolean isAdmin) {
        this.id = id;
        this.isAdmin = isAdmin;
    }

    public User(String id) {
        this(id, false);
    }

    public String getId() {
        return id;
    }

    public boolean isAdmin() { return isAdmin; }

    public static User get() {
        return Application.getInstance().getLoggedUser();
    }
}
