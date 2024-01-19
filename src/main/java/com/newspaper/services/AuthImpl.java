package com.newspaper.services;

import java.text.MessageFormat;

import org.apache.logging.log4j.message.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspaper.models.User;

import reactor.core.publisher.Mono;

public class AuthImpl implements AuthInterface {

	private static final String SIGNUP_ENDPOINT = "http://localhost:8080/api/users";

	private final WebClient webClient;

	public AuthImpl() {
		this.webClient = WebClient.create();
	}

	@Override
	public Mono<Boolean> signUpUser(User user) {
		try {
			String userJson = convertUserToJson(user);
			return sendRequest(SIGNUP_ENDPOINT, userJson)
					.flatMap(responseEntity -> handleResponse(responseEntity, "User created successfully",
							"Failed to create user"))
					.onErrorResume(WebClientResponseException.class, this::handleWebClientResponseException)
					.doOnError(this::handleUnexpectedError);
		} catch (JsonProcessingException e) {
			return Mono.error(e);
		}
	}
	
	@Override
	public String createFullName(String firstName, String lastName) {
	    return (firstName.isEmpty() && lastName.isEmpty()) ? null : firstName + (lastName.isEmpty() ? "" : " " + lastName);
	}

	@Override
	public String processPhoneNumber(String phoneNumber) {
	    return phoneNumber.isEmpty() ? null : phoneNumber;
	}

	private Mono<ResponseEntity<Void>> sendRequest(String endpoint, String body) {
		return webClient.post().uri(endpoint).header("Content-Type", "application/json")
				.body(BodyInserters.fromValue(body)).retrieve().toBodilessEntity();
	}

	private Mono<Boolean> handleResponse(ResponseEntity<Void> responseEntity, String successMessage,
			String failureMessage) {
		if (responseEntity.getStatusCode().is2xxSuccessful()) {
			System.out.println("Request successful");
			return Mono.just(true);
		} else {
			System.out.println("Server response: " + responseEntity.getStatusCode());
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

	private String convertUserToJson(User user) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(user);
	}
}
