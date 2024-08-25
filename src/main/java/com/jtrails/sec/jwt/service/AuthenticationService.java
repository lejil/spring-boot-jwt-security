/**
 * 
 */
package com.jtrails.sec.jwt.service;

import java.util.List;

import com.jtrails.sec.jwt.model.AuthenticationRequest;
import com.jtrails.sec.jwt.model.AuthenticationResponse;
import com.jtrails.sec.jwt.model.Role;
import com.jtrails.sec.jwt.model.User;
import com.jtrails.sec.jwt.model.UserRegistrationRequest;

/**
 * @author Lejil
 *
 */
public interface AuthenticationService {

	/**
	 * 
	 * @param userRegistration
	 * @return
	 */
	public User signup(UserRegistrationRequest userRegistration);

	/**
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationResponse authenticate(AuthenticationRequest request);

	/**
	 * 
	 * @param role
	 * @return
	 */
	public Role createRole(Role role);

	/**
	 * 
	 * @param email
	 * @param roles
	 * @return
	 */
	public User addRole(String email, List<Long> roles);

	/***
	 * 
	 * @param email
	 * @return
	 */
	public User getUser(String email);
}
