package ru.osmanov.janissarykeep.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import ru.osmanov.janissarykeep.Application;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class Authenticator {
    //https://www.mongodb.com/developer/languages/java/java-setup-crud-operations/
    private static MongoCollection<Document> collection;
    public static void init() {
        collection = Application.getInstance().getDatabaseConnector().getUsersCollection();
    }
    public static User validate(String userId, String password) {
        Bson userFilter = and(eq("userId", userId), eq("passwd", password));
        Document userDocument = collection.find(userFilter).first();
        if(userDocument != null) {
            System.out.println("User found: " + userDocument.toJson());
            return new User(userId);
        } else {
            System.out.println("User " + userId + " not found");
            return null;
        }
    }
    public static boolean create(String userId, String password) {
        Document userDocument = new Document();
        userDocument.append("userId", userId).append("passwd", password);
        try {
            collection.insertOne(userDocument);
            System.out.println("User " + userId + " is created");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean delete(String userId) {
        Bson filter = eq("userId", User.get().getId());
        return collection.deleteOne(filter).wasAcknowledged();
    }
}
