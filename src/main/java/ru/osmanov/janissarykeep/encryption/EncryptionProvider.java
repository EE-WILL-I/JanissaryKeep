package ru.osmanov.janissarykeep.encryption;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

import at.favre.lib.crypto.HKDF;
/**
 * Класс с параментрами шифрования: алгоритм, режим, ключи и т.д.
 Алгоритм: AES
 https://coderlessons.com/articles/java/shifrovanie-i-deshifrovanie-aes-v-java-rezhim-cbc
 Режим алгоритма: CBC — Cipher Block Chaining (Режим сцепления блоков шифротекста)
 https://ru.wikipedia.org/wiki/Режим_сцепления_блоков_шифротекста
 * **/
public class EncryptionProvider {
    //https://proandroiddev.com/security-best-practices-symmetric-encryption-with-aes-in-java-and-android-part-2-b3b80e99ad36
    //Шифр
    public static final Cipher cipher;
    private static final SecureRandom secureRandom;
    //доп. параметры для шифрования
    private static final byte[] ivBytes, salt;

    //статическая (по-умолчанию) инициализация параметров шифрования
    static {
        try {
            //Шифр
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] seed = "ave_maria".getBytes(StandardCharsets.UTF_8);
            secureRandom = new SecureRandom(seed);
            ivBytes = new byte[16];
            secureRandom.nextBytes(ivBytes);
            salt = new byte[16];
            secureRandom.nextBytes(salt);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //непосредтсвенно настоящий процесс шифровния проходит тут
    public static byte[] Encrypt(byte[] bytes, String key) throws Exception {
        //задаем ключ (пароль)
        byte[] authKey = getAuthKey(key, Cipher.ENCRYPT_MODE);
        byte[] encryptedBytes =  cipher.doFinal(bytes);
        //MAC для проверки подленности
        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(ivBytes);
        hmac.update(encryptedBytes);
        byte[] mac = hmac.doFinal();
        //комбинирование всех данных в один файл
        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + ivBytes.length + 1 + mac.length + encryptedBytes.length);
        byteBuffer.put((byte) ivBytes.length);
        byteBuffer.put(ivBytes);
        byteBuffer.put((byte) mac.length);
        byteBuffer.put(mac);
        byteBuffer.put(encryptedBytes);
        return byteBuffer.array();
    }

    //расшифровка файлов
    public static byte[] Decrypt(byte[] bytes, String key) throws Exception {
        //ключ (пароль)
        byte[] authKey = getAuthKey(key, Cipher.DECRYPT_MODE);
        //проверка IV
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int ivLength = (byteBuffer.get());
        if (ivLength != 16) { // check input parameter
            throw new IllegalArgumentException("invalid iv length");
        }
        byte[] iv = new byte[ivLength];
        byteBuffer.get(iv);
        //проверка MAC
        int macLength = (byteBuffer.get());
        if (macLength != 32) { // check input parameter
            throw new IllegalArgumentException("invalid mac length");
        }
        byte[] mac = new byte[macLength];
        byteBuffer.get(mac);
        //проверка самого шифра исходного файла
        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);
        //проверка подленности ключей
        SecretKey macKey = new SecretKeySpec(authKey, "HmacSHA256");
        Mac hmac = Mac.getInstance("HmacSHA256");
        hmac.init(macKey);
        hmac.update(iv);
        hmac.update(cipherText);
        byte[] refMac = hmac.doFinal();
        if (!MessageDigest.isEqual(refMac, mac)) {
            throw new SecurityException("could not authenticate");
        }
        //возвращаем расшифрованный файл
        return cipher.doFinal(cipherText);
    }

    //шифрование паролей пользователя по алгоритму PBKDF2 с HmacSHA1
    public static String getHashPBKDF2(String password) {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 512, 128);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //из ключа (пароля) получаем данные для проверки подлинности и шифровки\расшифровки файлов на их основе
    private static byte[] getAuthKey(String key, int decryptMode) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        //получаем HKDF ключи с помощью алгоритма HmacSha256
        byte[] encKey = HKDF.fromHmacSha256().expand(keyBytes, keyBytes, 16);
        byte[] authKey = HKDF.fromHmacSha256().expand(keyBytes, keyBytes, 32);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        //генерируем секретный ключ для нашего шифра AES
        SecretKeySpec keySpec = new SecretKeySpec(encKey, "AES");
        cipher.init(decryptMode, keySpec, ivSpec);
        return authKey;
    }
}
