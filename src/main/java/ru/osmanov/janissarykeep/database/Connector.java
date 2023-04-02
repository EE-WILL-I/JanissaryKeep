package ru.osmanov.janissarykeep.database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class Connector {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> usersCollection, documentsCollection;

    public Connector(String URL, String datasource) {
        mongoClient = MongoClients.create(URL);
        database = mongoClient.getDatabase(datasource);
        usersCollection = getCollection("users");
        documentsCollection = getCollection("documents");
        //кто блять делает вывод информации в консоль красным цветом, вы хотите чтобы я каждый раз при запуске испытывал стресс?
    }

    public void printDatabases() {
        for(String name : mongoClient.listDatabaseNames()) {
            System.out.println(name);
        }
    }

    public MongoCollection<Document> getCollection(String name) {
        return database.getCollection(name);
    }

    public MongoCollection<Document> getUsersCollection() {
        return usersCollection;
    }
    public MongoCollection<Document> getDocumentsCollection() {
        return documentsCollection;
    }
}
