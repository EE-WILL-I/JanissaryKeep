package ru.osmanov.janissarykeep.database;


import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class DocumentBuilder {
    private final Document document;

    public DocumentBuilder(String name) {
        document = new Document("_id", new ObjectId());
        document.append("userId", User.get().getId());
        document.append("name", name);
        document.append("dtm", new Date().getTime());
    }
    public DocumentBuilder addProperty(String key, Object value) {
        document.append(key, value);
        return this;
    }
    public DocumentBuilder addData(String data) {
        document.append("data", data);
        return this;
    }
    public Document getDocument() { return document; }
}
