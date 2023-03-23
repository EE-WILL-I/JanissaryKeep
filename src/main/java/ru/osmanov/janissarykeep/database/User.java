package ru.osmanov.janissarykeep.database;

public class User {
    private String id;
    public User(String id) {
        this.id = id;
    }
    public static User get(String userId) {
        User user = new User(userId);
        return user;
    }
    public String getId() {
        return id;
    }
}
