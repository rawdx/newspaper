package com.newspaper.models;

public class Token {

    private String value;
    private String email;
    private final long expiryDate;
    
    
	public Token(String value, String email, long expiryDate) {
		super();
		this.value = value;
		this.email = email;
		this.expiryDate = expiryDate;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public long getExpiryDate() {
		return expiryDate;
	}
    
	
}

