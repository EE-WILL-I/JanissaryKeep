package ru.osmanov.janissarykeep.database;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Connector {
    private MongoClient mongoClient;
    private MongoDatabase database;

    public Connector(String URL, String datasource) {
        mongoClient = MongoClients.create(URL);
        database = mongoClient.getDatabase(datasource);
    }

    public void printDatabases() {
        for(String name : mongoClient.listDatabaseNames()) {
            System.out.println(name);
        }
    }
}
