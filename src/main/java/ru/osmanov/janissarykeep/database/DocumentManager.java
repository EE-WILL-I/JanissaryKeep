package ru.osmanov.janissarykeep.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import ru.osmanov.janissarykeep.Application;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class DocumentManager {
    public static String DOWNLOAD_PATH = "C:/Users/Bogdan/IdeaProjects/JanissaryKeep/temp";
    private static MongoCollection<Document> collection;

    public static void init() {
        collection = Application.getInstance().getDatabaseConnector().getDocumentsCollection();
    }

    public static boolean uploadDocumentToDatabase(Document document) {
        try {
            collection.insertOne(document);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static ArrayList<Document> getAllDocumentsForCurrentUser() {
        Bson filter = eq("userId", User.get().getId());
        return collection.find(filter).into(new ArrayList<>());
    }

    public static Document getDocumentByName(String name) {
        Bson filter = and(eq("userId", User.get().getId()), eq("name", name));
        return collection.find(filter).first();
    }

    public static boolean deleteDocument(String name) {
        Bson filter = and(eq("userId", User.get().getId()), eq("name", name));
        return collection.deleteOne(filter).wasAcknowledged();
    }

    public static boolean deleteDocument(Document document) {
        String name = document.get("name").toString();
        Bson filter = and(eq("userId", User.get().getId()), eq("name", name));
        return collection.deleteOne(filter).wasAcknowledged();
    }
}
