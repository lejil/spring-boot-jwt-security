/**
 * 
 */
package com.jtrails.sec.jwt.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.jtrails.sec.jwt.exception.AuthenticationException;
import com.jtrails.sec.jwt.model.AuthenticationRequest;
import com.jtrails.sec.jwt.model.AuthenticationResponse;
import com.jtrails.sec.jwt.model.Role;
import com.jtrails.sec.jwt.model.User;
import com.jtrails.sec.jwt.model.UserRegistrationRequest;
import com.jtrails.sec.jwt.repository.RoleRepository;
import com.jtrails.sec.jwt.repository.UserRepository;
import com.jtrails.sec.jwt.service.AuthenticationService;
import com.jtrails.sec.jwt.service.SecurityService;

/**
 * @author Lejil
 *
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final SecurityService securityService;

	private static final String NOT_FOUND = "Search key  %s not found .";

	AuthenticationServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
			SecurityService securityService) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.securityService = securityService;
	}

	/***
	 * 
	 * @param userRegistration
	 * @return
	 */
	public User signup(UserRegistrationRequest userRegistration) {
		User user = new User(userRegistration.getFullName(), userRegistration.getEmail(),
				securityService.encryptKey(userRegistration.getPassword()));
		return userRepository.save(user);
	}

	/**
	 * 
	 * @param request
	 * @return
	 */
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		Optional<User> user = Optional.ofNullable(userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new AuthenticationException(String.format(NOT_FOUND, request.getEmail()))));

		Optional<String> password = user.map(u -> Optional.ofNullable(u.getPassword())
				.orElseThrow(() -> new AuthenticationException("Password cannot be null")));

		String storedPassword = securityService.decryptKey(password.get());

		if (!request.getPassword().equals(storedPassword)) {
			throw new AuthenticationException("Invalid username or password  " + request.getEmail());
		}

		return new AuthenticationResponse(securityService.generateToken(user.get()));

	}

	@Override
	public Role createRole(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public User addRole(String email, List<Long> roleIds) {

		Optional<User> optionalUser = Optional.ofNullable(userRepository.findByEmail(email)
				.orElseThrow(() -> new AuthenticationException(String.format(NOT_FOUND, email))));

		Optional<User> updatedUser = optionalUser.map(usr -> {
			List<Role> roles = StreamSupport.stream(roleRepository.findAllById(roleIds).spliterator(), false)
					.collect(Collectors.toList());
			usr.setRoles(roles);

			userRepository.save(usr);

			return usr;
		});

		return updatedUser.orElseThrow(() -> new AuthenticationException(String.format(NOT_FOUND, email)));
	}

	@Override
	public User getUser(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new AuthenticationException("User not found ."));
	}

}
