package com.newspaper.models;

public class User {

	private long idUser;
	private String email;
	private String credential;
	private String name;
	private String phoneNumber;
	private byte[] profilePicture;
	private boolean isSuscribed;
	private boolean isWriter;
	private boolean isAdmin;
	
	
	
	public User() {
		super();
	}

	public User(String email, String credential) {
		super();
		this.email = email;
		this.credential = credential;
	}

	public User(String email, String credential, String name, String phoneNumber) {
		super();
		this.email = email;
		this.credential = credential;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.profilePicture = null;
		this.isSuscribed = false;
		this.isWriter = false;
		this.isAdmin = false;
	}


	public long getIdUser() {
		return idUser;
	}


	public void setIdUser(long idUser) {
		this.idUser = idUser;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getCredential() {
		return credential;
	}


	public void setCredential(String credential) {
		this.credential = credential;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	
	public byte[] getProfilePicture() {
		return profilePicture;
	}


	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	
	public boolean isSuscribed() {
		return isSuscribed;
	}


	public void setSuscribed(boolean isSuscribed) {
		this.isSuscribed = isSuscribed;
	}


	public boolean isWriter() {
		return isWriter;
	}


	public void setWriter(boolean isWriter) {
		this.isWriter = isWriter;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	
}
