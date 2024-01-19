package com.newspaper.utils;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

public class ErrorHandler {
    
    public static Mono<Boolean> handleWebClientResponseException(WebClientResponseException e) {
        System.err.println("WebClient error - status code: " + e.getStatusCode());
        return Mono.just(false);
    }

    public static void handleUnexpectedError(Throwable e) {
        System.err.println("Unexpected error occurred: " + e.getMessage());
    }
}
