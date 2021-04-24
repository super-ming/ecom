package com.example.demo.model.requests;

import com.example.demo.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Size;

@PasswordMatches
public class CreateUserRequest {

	@JsonProperty
	private String username;

	@JsonProperty
	@Size(min = 4)
	private String password;

	@JsonProperty
	private String confirmPassword;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
}
