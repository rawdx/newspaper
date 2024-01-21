package com.newspaper.services;

import reactor.core.publisher.Mono;

public interface LoginInterface {

	Mono<Boolean> loginUser(String email, String password);
}
