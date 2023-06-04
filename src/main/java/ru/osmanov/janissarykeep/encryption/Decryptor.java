package ru.osmanov.janissarykeep.encryption;

import org.bson.Document;
import org.bson.internal.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 Расшифровка файлов здесь
 * **/
public class Decryptor {
    private static final String SYSTEM_KEY = "bezopasnost";

    //расшифровываем файл из документа с ключем
    public static File decryptDocument(Document document, String key, File outFile) throws Exception {
        //берем строку с данными
        String encodedByteString = document.getString("data");
        //переводим в байты
        byte[] encodedBytes = decryptString(encodedByteString);
        if(!outFile.exists()) {
            outFile.createNewFile();
        }
        //расшифровываем байты на основе ключа
        byte[] decodedBytes = decryptFile(encodedBytes, key);
        //записываем итог в новый файл на диске
        if(outFile.isFile() && outFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(decodedBytes);
            fos.close();
            return outFile;
        }
        throw new IOException("Can't write to:" + outFile.getAbsolutePath());
    }

    public static String decryptKey(String encryptedKey) throws Exception {
        byte[] encodedBytes = decryptString(encryptedKey);
        byte[] decodedBytes = decryptFile(encodedBytes, SYSTEM_KEY);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    //расшифровываем байты
    private static byte[] decryptFile(byte[] encodedBytes, String key) throws Exception {
        return EncryptionProvider.Decrypt(encodedBytes, key);
    }

    //из строки, что берем из документа, что лежит в БД, что стоит на сервере, считываем зашифрованные байты нашего файла
    private static byte[] decryptString(String encodedBytesString) {
        return Base64.decode(encodedBytesString);
    }
}
