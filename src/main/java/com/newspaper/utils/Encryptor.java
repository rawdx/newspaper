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
			System.out.println(e);
			return null;
		}
	}
}
