package ru.osmanov.janissarykeep.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import ru.osmanov.janissarykeep.Application;
import ru.osmanov.janissarykeep.encryption.EncryptionProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Authenticator {
    //https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/
    private static MongoCollection<Document> collection;

    public static void init() {
        collection = Application.getInstance().getDatabaseConnector().getUsersCollection();
    }

    public static User validate(String userId, String password) {
        password = EncryptionProvider.getHashPBKDF2(password);
        Bson userFilter = and(eq("userId", userId), eq("passwd", password));
        Document userDocument = collection.find(userFilter).first();
        if(userDocument != null) {
            System.out.println("User found: " + userDocument.toJson());
            try {
                boolean isUserAdmin = userDocument.getBoolean("admin");
                return new User(userId, isUserAdmin);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return new User(userId);
            }
        } else {
            System.out.println("User " + userId + " not found");
            return null;
        }
    }

    public static boolean Find(String userId) {
        Bson filter = eq("userId", userId);
        return collection.find(filter).first() != null;
    }

    public static boolean create(String userId, String password, boolean isAdmin) {
        if (!validatePassword(password))
            return false;
        Document userDocument = new Document();
        password = EncryptionProvider.getHashPBKDF2(password);
        userDocument.append("userId", userId).append("passwd", password).append("admin", isAdmin);
        collection.insertOne(userDocument);
        System.out.println("User " + userId + " is created");
        return true;
    }

    public static boolean update(String userId, String newPassword) {
        Bson filter = eq("userId", userId);
        Document userDocument = collection.find(filter).first();
        if(userDocument == null)
            return false;
        newPassword = EncryptionProvider.getHashPBKDF2(newPassword);
        return collection.updateOne(filter, Updates.set("passwd", newPassword)).wasAcknowledged();
    }

    public static boolean delete(String userId) {
        Bson filter = eq("userId", userId);
        List<Document> userDocuments = DocumentManager.getAllDocumentsForUser(userId);
        for(Document doc : userDocuments) {
            DocumentManager.deleteDocument(doc);
        }
        return collection.deleteOne(filter).wasAcknowledged();
    }

    public static ArrayList<User> getAllUsers() {
        ArrayList<Document> userDocs = collection.find().into(new ArrayList<>());
        ArrayList<User> users = new ArrayList<>(userDocs.size());
        for(Document user : userDocs) {
            try {
                users.add(new User(user.get("userId").toString(), user.getBoolean("admin")));
            } catch (Exception ignored) {}
        }
        return users;
    }

    public static boolean validatePassword(String password) {
        return password.matches("\\A(?=\\S*?[0-9])(?=\\S*?[a-z])(?=\\S*?[A-Z])(?=\\S*?[@#$%^&+=])\\S{8,}\\z");
    }
}
