package com.newspaper.services;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newspaper.models.User;

public class SignUpImpl implements SignUpInterface {

	@Override
	public boolean signUpUser(User user) {
		try {
			String userJson = convertUserToJson(user);
			HttpURLConnection connection = openConnection();

			sendUserJsonToServer(userJson, connection);

			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == HttpURLConnection.HTTP_OK) {
				System.out.println("User created successfully");
				return true;
			} else {
				System.out.println("Server response: " + responseCode + " " + connection.getResponseMessage());
			}
		} catch (JsonProcessingException e) {
			System.out.println(e);
		} catch (IOException | URISyntaxException e) {
			System.out.println(e);
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	private String convertUserToJson(User user) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(user);
	}

	private HttpURLConnection openConnection() throws IOException, URISyntaxException {
		URI uri = new URI("http://localhost:8080/api/users");
		return (HttpURLConnection) uri.toURL().openConnection();
	}

	private void sendUserJsonToServer(String userJson, HttpURLConnection connection) throws IOException {
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", "application/json");
		connection.setDoOutput(true);

		try (OutputStream os = connection.getOutputStream()) {
			byte[] input = userJson.getBytes("utf-8");
			os.write(input, 0, input.length);
		}
	}
}
