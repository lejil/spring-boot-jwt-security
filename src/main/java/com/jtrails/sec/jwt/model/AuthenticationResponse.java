/**
 * 
 */
package com.jtrails.sec.jwt.model;

/**
 * @author Lejil
 *
 */
public class AuthenticationResponse {
	
	/**
	 * @param token
	 */
	public AuthenticationResponse(String token) {
		super();
		this.token = token;
	}

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
