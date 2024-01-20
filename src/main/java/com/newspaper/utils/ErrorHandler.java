package com.newspaper.utils;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

public class ErrorHandler {

    public static Mono<Boolean> handleWebClientResponseException(WebClientResponseException e) {
            System.out.println("WebClient error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return Mono.just(false); // Indicate that the user already exists
    }
	public static void handleUnexpectedError(Throwable e) {
		System.err.println("Unexpected error occurred: " + e.getMessage());
	}

	public static void handleLoginTerminate(String email) {
		System.out.println("Login failed - Invalid credentials or user not found: " + email);
	}
}
