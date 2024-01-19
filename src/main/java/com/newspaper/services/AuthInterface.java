package com.newspaper.services;

import com.newspaper.models.User;

import reactor.core.publisher.Mono;

public interface AuthInterface {
	Mono<Boolean> signUpUser(User user);


	String processPhoneNumber(String phoneNumber);


	String createFullName(String firstName, String lastName);
}
