package com.newspaper.services;

import java.text.MessageFormat;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.newspaper.models.User;
import com.newspaper.utils.Encryptor;
import com.newspaper.utils.ErrorHandler;

import reactor.core.publisher.Mono;

public class LoginImpl implements LoginInterface {

	private static final String LOGIN_ENDPOINT = "http://localhost:8080/api/users/{0}";

	private final WebClient webClient;

	public LoginImpl() {
		this.webClient = WebClient.create();
	}
	
    @Override
    public Mono<Boolean> loginUser(String email, String password) {
        String loginEndpoint = MessageFormat.format(LOGIN_ENDPOINT, email);
        return sendRequest(loginEndpoint)
                .flatMap(serverUser -> compareUsers(email, password, serverUser))
                .onErrorResume(WebClientResponseException.class, ErrorHandler::handleWebClientResponseException)
                .doOnError(ErrorHandler::handleUnexpectedError)
                .defaultIfEmpty(false)
                .doOnTerminate(() -> {
                	ErrorHandler.handleLoginTerminate(email);
                });
    }
    
	private Mono<User> sendRequest(String endpoint) {
		return webClient.get().uri(endpoint).retrieve().bodyToMono(User.class);
	}

    private Mono<Boolean> compareUsers(String email, String password, User serverUser) {
        if (serverUser != null && Encryptor.verifyPassword(password, serverUser.getCredential())) {
            return Mono.just(true);
        } else {
            return Mono.just(false);
        }
    }
}
