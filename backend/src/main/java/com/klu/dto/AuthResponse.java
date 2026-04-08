package com.klu.dto;

import com.klu.entity.User;

public class AuthResponse {

	private String token;
	private String tokenType = "Bearer";
	private long expiresInMs;
	private User user;

	public AuthResponse() {
	}

	public AuthResponse(String token, long expiresInMs, User user) {
		this.token = token;
		this.expiresInMs = expiresInMs;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public long getExpiresInMs() {
		return expiresInMs;
	}

	public void setExpiresInMs(long expiresInMs) {
		this.expiresInMs = expiresInMs;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}