package com.newspaper.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspaper.models.User;
import com.newspaper.utils.ErrorHandler;

import reactor.core.publisher.Mono;

public class SignUpImpl implements SignUpInterface {

	private static final String SIGNUP_ENDPOINT = "http://localhost:8080/api/users";

	private final WebClient webClient;

	public SignUpImpl() {
		this.webClient = WebClient.create();
	}

    @Override
    public Mono<Boolean> signUpUser(User user) {
        String userJson;
        try {
            userJson = convertUserToJson(user);
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }

        return sendRequest(SIGNUP_ENDPOINT, userJson)
                .flatMap(this::handleResponse)
                .onErrorResume(WebClientResponseException.class, ErrorHandler::handleWebClientResponseException)
                .doOnError(ErrorHandler::handleUnexpectedError);
    }
	
	@Override
	public String processFullName(String firstName, String lastName) {
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

    private Mono<Boolean> handleResponse(ResponseEntity<Void> responseEntity) {
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("User created successfully");
            return Mono.just(true);
        } else {
            System.out.println("Server response: " + responseEntity.getStatusCode());
            return Mono.just(false);
        }
    }

	private String convertUserToJson(User user) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(user);
	}
}
