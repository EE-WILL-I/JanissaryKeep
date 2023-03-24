package ru.osmanov.janissarykeep.database;


import org.bson.Document;
import org.bson.types.ObjectId;

public class DocumentBuilder {
    private final Document document;

    public DocumentBuilder() {
        document = new Document("_id", new ObjectId());
    }
    public void addData(String data) {
        document.append("data:", data);
    }
    public Document getDocument() { return document; }
}
