package com.newspaper.services;

import com.newspaper.models.User;

import reactor.core.publisher.Mono;

public interface LogInInterface {
	
	Mono<Boolean> loginUser(User user);
}
