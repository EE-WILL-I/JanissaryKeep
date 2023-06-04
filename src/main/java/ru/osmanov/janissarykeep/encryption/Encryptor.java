package ru.osmanov.janissarykeep.encryption;

import org.bson.Document;
import org.bson.internal.Base64;
import ru.osmanov.janissarykeep.database.DocumentBuilder;

import javax.crypto.Cipher;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
Шифрование проходит тута
Алгоритм: AES
https://coderlessons.com/articles/java/shifrovanie-i-deshifrovanie-aes-v-java-rezhim-cbc
Режим алгоритма: CBC — Cipher Block Chaining (Режим сцепления блоков шифротекста)
https://ru.wikipedia.org/wiki/Режим_сцепления_блоков_шифротекста
**/
public class Encryptor {
    // ключ для шифрования паролей (ключей)
    private static final String SYSTEM_KEY = "bezopasnost";

    //файл в массив байтов
    private static byte[] fileToByteArray(File file) throws IOException {
        byte[] byteArray = new byte[(int) file.length()];
        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(byteArray);
        return byteArray;
    }
    //создать документ с зашифрованным файлом
    public static Document encryptDocument(File file, String name, String key) throws Exception {
        byte[] byteArray = fileToByteArray(file);
        byte[] ketBytes = key.getBytes(StandardCharsets.UTF_8);
        if(byteArray.length == 0)
            throw new IllegalArgumentException("Encrypted string data is empty");
        //само шифрование
        byteArray = EncryptionProvider.Encrypt(byteArray, key);
        ketBytes = EncryptionProvider.Encrypt(ketBytes, SYSTEM_KEY);
        //закодированные байты в строку кодировки Base64
        String encryptedByteData = Base64.encode(byteArray);
        String encodedKey = Base64.encode(ketBytes);
        if (encryptedByteData.isEmpty())
            throw new IllegalArgumentException("Encrypted string data is empty");
        //создаем документ
        DocumentBuilder builder = new DocumentBuilder(name);
        builder.addProperty("key", encodedKey);
        builder.addData(encryptedByteData);
        return builder.getDocument();
    }
}
