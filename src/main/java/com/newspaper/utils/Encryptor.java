package com.newspaper.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Encryptor {

    public static String encrypt(String password) {
        try {
            // Generate a random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Create a MessageDigest instance for SHA-256
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // Add salt to the password and hash it
            messageDigest.update(salt);
            byte[] hashedPassword = messageDigest.digest(password.getBytes());

            // Combine the salt and hashed password into a byte array
            byte[] saltedHashedPassword = new byte[salt.length + hashedPassword.length];
            System.arraycopy(salt, 0, saltedHashedPassword, 0, salt.length);
            System.arraycopy(hashedPassword, 0, saltedHashedPassword, salt.length, hashedPassword.length);

            // Convert the byte array to a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : saltedHashedPassword) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encrypting password", e);
        }
    }
    
    public static boolean verifyPassword(String enteredPassword, String storedPassword) {
        // Convert the stored hashed password to a byte array
        byte[] storedSaltedHashedPassword = hexStringToByteArray(storedPassword);

        // Extract the salt and hashed password from the stored value
        byte[] storedSalt = new byte[16];
        byte[] storedHashedPassword = new byte[storedSaltedHashedPassword.length - storedSalt.length];
        System.arraycopy(storedSaltedHashedPassword, 0, storedSalt, 0, storedSalt.length);
        System.arraycopy(storedSaltedHashedPassword, storedSalt.length, storedHashedPassword, 0,
                storedHashedPassword.length);

        // Create a MessageDigest instance for SHA-256
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

            // Add the entered salt to the entered password and hash it
            messageDigest.update(storedSalt);
            byte[] enteredHashedPassword = messageDigest.digest(enteredPassword.getBytes());

            // Compare the stored hashed password with the newly hashed entered password
            return MessageDigest.isEqual(enteredHashedPassword, storedHashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password", e);
        }
    }
    
    private static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
