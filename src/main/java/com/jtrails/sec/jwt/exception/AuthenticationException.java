/**
 * 
 */
package com.jtrails.sec.jwt.exception;

/**
 * @author Lejil
 *
 */
@SuppressWarnings("serial")
public class AuthenticationException extends RuntimeException {

	public AuthenticationException(Long id) {
		super("User with ID " + id + " not found.");
	}

	public AuthenticationException(String message) {
		super(message);
	}

}
