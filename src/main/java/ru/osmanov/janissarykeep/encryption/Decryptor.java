package ru.osmanov.janissarykeep.encryption;

import org.bson.internal.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decryptor {

    public static File decryptFile(String encodedBytesString, File outFile) throws IOException {
        if(!outFile.exists()) {
            outFile.createNewFile();
        }
        if(outFile.isFile() && outFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(outFile);
            fos.write(Base64.decode(encodedBytesString));
            fos.close();
            return outFile;
        }
        throw new IOException("Can't write to:" + outFile.getAbsolutePath());
    }
}
