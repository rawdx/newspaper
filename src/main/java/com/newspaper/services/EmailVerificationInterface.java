package com.newspaper.services;

import com.newspaper.models.User;

import reactor.core.publisher.Mono;

public interface EmailVerificationInterface {

	Mono<Void> sendVerificationEmail(User user);

}
