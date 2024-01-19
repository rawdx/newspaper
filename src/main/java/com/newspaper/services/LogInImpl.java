package com.newspaper.services;

import java.text.MessageFormat;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.newspaper.models.User;
import com.newspaper.utils.Encryptor;

import reactor.core.publisher.Mono;

public class LogInImpl implements LogInInterface {

	private static final String LOGIN_ENDPOINT = "http://localhost:8080/api/users/{0}";

	private final WebClient webClient;

	public LogInImpl() {
		this.webClient = WebClient.create();
	}
	
    @Override
    public Mono<Boolean> loginUser(String email, String password) {
        String loginEndpoint = MessageFormat.format(LOGIN_ENDPOINT, email);
        return sendRequest(loginEndpoint)
                .flatMap(serverUser -> compareUsers(email, password, serverUser))
                .onErrorResume(WebClientResponseException.class, this::handleWebClientResponseException)
                .doOnError(this::handleUnexpectedError);
    }
    
	private Mono<User> sendRequest(String endpoint) {
		return webClient.get().uri(endpoint).retrieve().bodyToMono(User.class);
	}

    private Mono<Boolean> compareUsers(String email, String password, User serverUser) {
        if (serverUser != null && Encryptor.verifyPassword(password, serverUser.getCredential())) {
            System.out.println("User logged in successfully");
            return Mono.just(true);
        } else {
            System.out.println("Login failed - Invalid credentials");
            return Mono.just(false);
        }
    }

	private Mono<Boolean> handleWebClientResponseException(WebClientResponseException e) {
		System.err.println("WebClient error - status code: " + e.getStatusCode());
		return Mono.just(false);
	}

	private void handleUnexpectedError(Throwable e) {
		System.err.println("Unexpected error occurred: " + e.getMessage());
	}

}
