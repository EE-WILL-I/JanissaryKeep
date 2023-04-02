package ru.osmanov.janissarykeep.encryption;

import org.bson.Document;
import org.bson.internal.Base64;
import ru.osmanov.janissarykeep.database.DocumentBuilder;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Encryptor {
//https://stackoverflow.com/questions/1536054/how-to-convert-byte-array-to-string-and-vice-versa

    public static String fileToByteArray(File file) throws IOException {
        byte[] byteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArray);
        return Base64.encode(byteArray);
    }

    public static Document encryptDocument(String encryptedByteData, String name) throws IllegalArgumentException {
        if(encryptedByteData == null || encryptedByteData.isEmpty())
            throw new IllegalArgumentException("Encrypted string data is empty");
        DocumentBuilder builder = new DocumentBuilder(name);
        builder.addData(encryptedByteData);
        return builder.getDocument();
    }
}
