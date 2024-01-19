package com.newspaper.services;

import reactor.core.publisher.Mono;

public interface LogInInterface {

	Mono<Boolean> loginUser(String email, String password);
}
