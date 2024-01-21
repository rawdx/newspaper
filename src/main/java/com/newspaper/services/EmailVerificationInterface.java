package com.newspaper.services;

import com.newspaper.models.User;

public interface EmailVerificationInterface {

	void sendVerificationEmail(User user);

}
