package com.newspaper.services;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.reactive.function.client.WebClient;

import com.newspaper.models.Token;
import com.newspaper.models.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import reactor.core.publisher.Mono;

public class EmailVerificationImpl implements EmailVerificationInterface {

    private static final String VERIFICATION_ENDPOINT = "http://localhost:8080/api/verify-email";

    private final WebClient webClient;
    private final JavaMailSender javaMailSender;

    public EmailVerificationImpl(@Qualifier("javaMailSender")JavaMailSender javaMailSender) {
        this.webClient = WebClient.create();
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendVerificationEmail(User user) {
        Token token = generateVerificationToken(user);
        
        String verificationUrl = buildVerificationUrl(token.getValue());

        // Customize the email content and send it using your email service or API
        String emailContent = buildEmailContent(user.getEmail(), verificationUrl);
        sendEmail(user.getEmail(), "Email Verification", emailContent);
    }

    private Token generateVerificationToken(User user) {
        // Implement your logic to generate a verification token
        // You can use a token generation library or create your own logic
    	
        // Set token expiration to 24 hours (86400000 milliseconds)
        long expirationTimeMillis = System.currentTimeMillis() + 86400000L;

        // Create a secure random string or use a library for token generation
        String secureRandomToken = generateSecureRandomToken();
        
        // Store the token and expiration in the database
        saveTokenToDatabase(user.getEmail(), secureRandomToken, expirationTimeMillis).subscribe();
        
        return new Token(secureRandomToken, user.getEmail(), expirationTimeMillis);
    }
    
    private Mono<Void> saveTokenToDatabase(String email, String value, long expirationTimeMillis) {
    	
        return webClient.post()
                .uri("http://localhost:8080/api/token")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new Token(value, email, expirationTimeMillis))
                .retrieve()
                .toBodilessEntity()
                .then()
                .doOnError(error -> {
                    System.err.println("Client-side error: " + error.getMessage());
                });
    }
    
    private String buildVerificationUrl(String tokenValue) {
        return VERIFICATION_ENDPOINT + "?token=" + tokenValue;
    }

    private String buildEmailContent(String userEmail, String verificationUrl) {
        // Customize the email content based on your requirements
        return "<html><body><p>Hello,</p><p>Please click the following link to verify your email: <a href='" + verificationUrl + "'>Verify Email</a></p></body></html>";
    }
    
    private void sendEmail(String to, String subject, String content) {
    	
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper;
        
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true); // Set the second parameter to true to indicate HTML content

            // Use the JavaMailSender to send the HTML email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            // Handle the exception appropriately
            System.out.println(e);
        }
   }
    
    private String generateSecureRandomToken() {
        // Use SecureRandom to generate a random token
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        
        // Convert the byte array to a hexadecimal string
        return bytesToHex(tokenBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }
}
