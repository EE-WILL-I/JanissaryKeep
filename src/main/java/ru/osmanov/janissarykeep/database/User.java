package ru.osmanov.janissarykeep.database;

import org.bson.Document;
import org.bson.types.ObjectId;
import ru.osmanov.janissarykeep.Application;

/**
 * Класс с данными о пользователе
 * **/
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
