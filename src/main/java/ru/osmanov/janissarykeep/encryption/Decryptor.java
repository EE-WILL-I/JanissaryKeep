package ru.osmanov.janissarykeep.encryption;

import org.bson.Document;
import org.bson.internal.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decryptor {
    public static File decryptDocument(Document document, String key, File outFile) throws Exception {
        String encodedByteString = document.getString("data");
        byte[] encodedBytes = decryptString(encodedByteString);
        if(!outFile.exists()) {
            outFile.createNewFile();
        }
        byte[] decodedBytes = decryptFile(encodedBytes, key);
        if(outFile.isFile() && outFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(decodedBytes);
            fos.close();
            return outFile;
        }
        throw new IOException("Can't write to:" + outFile.getAbsolutePath());
    }

    private static byte[] decryptFile(byte[] encodedBytes, String key) throws Exception {
        return EncryptionProvider.Decrypt(encodedBytes, key);
    }

    private static byte[] decryptString(String encodedBytesString) {
        return Base64.decode(encodedBytesString);
    }
}
