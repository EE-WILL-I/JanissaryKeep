package ru.osmanov.janissarykeep.database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import ru.osmanov.janissarykeep.Application;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 Класс для работы с документами в БД (в документе хранятся данные о зашифрованном файле и сам файл)
 **/
public class DocumentManager {
    //коллекция в БД с докуменнтами
    private static MongoCollection<Document> collection;

    //старт
    public static void init() {
        collection = Application.getInstance().getDatabaseConnector().getDocumentsCollection();
    }

    //загрузить в БД
    public static boolean uploadDocumentToDatabase(Document document) {
        try {
            collection.insertOne(document);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //все доки пользователя
    public static ArrayList<Document> getAllDocumentsForUser(String userId) {
        Bson filter = eq("userId", userId);
        return collection.find(filter).into(new ArrayList<>());
    }

    //все доки
    public static ArrayList<Document> getAllDocumentsAdmin() {
        return collection.find().into(new ArrayList<>());
    }

    //документ по имени
    public static Document getDocumentByName(String name) {
        Bson filter = and(eq("userId", User.get().getId()), eq("name", name));
        return collection.find(filter).first();
    }

    //удалить документ
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
