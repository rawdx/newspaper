package com.newspaper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newspaper.models.User;
import com.newspaper.services.LogInImpl;
import com.newspaper.services.LogInInterface;
import com.newspaper.services.SignUpImpl;
import com.newspaper.services.SignUpInterface;
import com.newspaper.utils.Encryptor;

import reactor.core.publisher.Mono;

@Controller
public class AuthController {

	@GetMapping("login")
	public String login() {
		return "redirect:/";
	}

	@PostMapping("/login")
	public Mono<String> login(@RequestParam String email, @RequestParam String password) {

		LogInInterface authImpl = new LogInImpl();

		return authImpl.loginUser(email, password).map(success -> {
			if (success) {
				return "redirect:/aaaa";
			} else {
				return "redirect:/";
			}
		}).defaultIfEmpty("redirect:/");
	}

	@GetMapping("signup")
	public String signup() {
		return "redirect:/";
	}

	@PostMapping("/signup")
	public Mono<String> signup(@RequestParam String email, @RequestParam String password,
			@RequestParam String firstName, @RequestParam String lastName, @RequestParam String phoneNumber) {
		password = Encryptor.encrypt(password);

		SignUpInterface signUpImpl = new SignUpImpl();

		User user = new User(email, password, signUpImpl.createFullName(firstName, lastName),
				signUpImpl.processPhoneNumber(phoneNumber));

		return signUpImpl.signUpUser(user).map(success -> {
			if (success) {
				return "redirect:/signupResult";
			} else {
				return "redirect:/fallo";
			}
		});
	}
}
