package com.example.Employees.Entity;

public class AuthenticationResponse {
	
	private final String jwt;

	
	public AuthenticationResponse(String jwt) {
		this.jwt=jwt;
	}
	
	public String getResponse() {
		return jwt;
	}

	

}