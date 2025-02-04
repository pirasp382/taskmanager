package org.example.util;

import lombok.experimental.UtilityClass;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

@UtilityClass
public class Encryption {

  private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

  public static SecretKey generateKey(final byte[] salt, final String password)
          throws NoSuchAlgorithmException, InvalidKeySpecException {
    final SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    final KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100000, 256);
    return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
  }

  public static byte[] generateSalt() {
    final byte[] salt = new byte[16];
    new SecureRandom().nextBytes(salt);
    return salt;
  }

  public static IvParameterSpec generateIv() {
    final byte[] iv = new byte[16];
    new SecureRandom().nextBytes(iv);
    return new IvParameterSpec(iv);
  }

  public static String encrypt(final String input, final String password)
           {
             try{
    final byte[] salt = generateSalt();
    final SecretKey key = generateKey(salt, password);
    final IvParameterSpec iv = generateIv();

    final Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, key, iv);
    final byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

    // Kombiniere Salt, IV und Ciphertext
    final byte[] result = new byte[salt.length + iv.getIV().length + cipherText.length];
    System.arraycopy(salt, 0, result, 0, salt.length);
    System.arraycopy(iv.getIV(), 0, result, salt.length, iv.getIV().length);
    System.arraycopy(cipherText, 0, result, salt.length + iv.getIV().length, cipherText.length);

    return Base64.getEncoder().encodeToString(result);
    }
             catch (final Exception e){
               return null;
             }
  }

  public static String decrypt(final String input, final String password) {
try{
    final byte[] decodedInput = Base64.getDecoder().decode(input);

    final byte[] salt = new byte[16];
    final byte[] iv = new byte[16];
    final byte[] cipherText = new byte[decodedInput.length - 32];

    // Salt, IV und Ciphertext extrahieren
    System.arraycopy(decodedInput, 0, salt, 0, 16);
    System.arraycopy(decodedInput, 16, iv, 0, 16);
    System.arraycopy(decodedInput, 32, cipherText, 0, cipherText.length);

    final SecretKey key = generateKey(salt, password);
    final IvParameterSpec ivSpec = new IvParameterSpec(iv);

    final Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);

    return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }
catch (final Exception e){
  return null;
}
  }
}
