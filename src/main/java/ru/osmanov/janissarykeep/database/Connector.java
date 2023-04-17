package ru.osmanov.janissarykeep.database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**Класс для подключения к БД**/
public class Connector {
    private final MongoClient mongoClient;
    private final MongoDatabase database;
    private final MongoCollection<Document> usersCollection, documentsCollection;

    //конструктор, т.е. задаем URL и название БД
    public Connector(String URL, String datasource) {
        mongoClient = MongoClients.create(URL);
        database = mongoClient.getDatabase(datasource);
        usersCollection = getCollection("users");
        documentsCollection = getCollection("documents");
        //кто блять делает вывод служебной информации в консоль красным цветом, вы хотите чтобы я каждый раз при запуске паниковал?
    }

    //выводим в консоль коллекции в БД
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
