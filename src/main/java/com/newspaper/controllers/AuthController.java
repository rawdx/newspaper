package com.newspaper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newspaper.models.User;
import com.newspaper.services.AuthImpl;
import com.newspaper.services.AuthInterface;
import com.newspaper.utils.Encryptor;

import reactor.core.publisher.Mono;

@Controller
public class AuthController {
    
	@GetMapping("login")
	public String login() {
		return "redirect:/";
	}

	@PostMapping("login")
	public String login(@RequestParam String email, @RequestParam String password) {
		password = Encryptor.encrypt(password);

		AuthInterface authImpl = new AuthImpl();
		
//		return authImpl.loginUser(user).map(success -> {
//			if (success) {
//				return "redirect:/signupResult";
//			} else {
//				return "redirect:/fallo";
//			}
//		});
		return null;
	}

	@GetMapping("signup")
	public String signup() {
		return "redirect:/";
	}

	@PostMapping("/signup")
	public Mono<String> signup(@RequestParam String email, @RequestParam String password,
			@RequestParam String firstName, @RequestParam String lastName, @RequestParam String phoneNumber) {
		password = Encryptor.encrypt(password);

		AuthInterface authImpl = new AuthImpl();

		User user = new User(email, password, authImpl.createFullName(firstName, lastName), authImpl.processPhoneNumber(phoneNumber));

		return authImpl.signUpUser(user).map(success -> {
			if (success) {
				return "redirect:/signupResult";
			} else {
				return "redirect:/fallo";
			}
		});
	}
}
