package com.newspaper.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.newspaper.models.User;
import com.newspaper.services.SignUpImpl;
import com.newspaper.services.SignUpInterface;
import com.newspaper.utils.Encryptor;

@Controller
public class AuthController {

	@PostMapping("login")
	public String login(@RequestParam String username, @RequestParam String password) {
		password = Encryptor.encrypt(password);

		return "redirect:/loginResult";
	}

	@PostMapping("/signup")
	public String signup(@RequestParam String username, @RequestParam String password) {
		password = Encryptor.encrypt(password);

		User user = new User(username, password);

		SignUpInterface signUpImpl = new SignUpImpl();
		
		if (signUpImpl.signUpUser(user))
			return "redirect:/signupResult";
		return "redirect:/fallo";
	}
}
