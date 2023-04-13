package ru.osmanov.janissarykeep.encryption;

import org.bson.internal.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Decryptor {

    public static File decryptFile(String encodedBytesString, String outputPath) throws IOException {
        File outFile = new File(outputPath);
        if(!outFile.exists()) {
            outFile.createNewFile();
        }
        if(outFile.isFile() && outFile.canWrite()) {
            FileOutputStream fos = new FileOutputStream(outputPath);
            fos.write(Base64.decode(encodedBytesString));
            fos.close();
            return outFile;
        }
        throw new IOException("Can't write to:" + outputPath);
    }
}
