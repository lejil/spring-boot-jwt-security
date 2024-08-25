/**
 * 
 */
package com.jtrails.sec.jwt.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtrails.sec.jwt.model.AuthenticationRequest;
import com.jtrails.sec.jwt.model.AuthenticationResponse;
import com.jtrails.sec.jwt.model.Role;
import com.jtrails.sec.jwt.model.User;
import com.jtrails.sec.jwt.model.UserRegistrationRequest;
import com.jtrails.sec.jwt.service.AuthenticationService;

/**
 * @author Lejil
 *
 */

@RequestMapping("auth")
@RestController
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	
	AuthenticationController(AuthenticationService authenticationService){
		this.authenticationService = authenticationService;
	}

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody UserRegistrationRequest userRegistrationRequest) {
		User user = authenticationService.signup(userRegistrationRequest);
		return ResponseEntity.ok(user);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody AuthenticationRequest authenticationRequest) {
		AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
		return ResponseEntity.ok(authenticationResponse);
	}

	@PostMapping("/role")
	public ResponseEntity<Role> createRoles(@RequestBody Role roleRequest) {
		Role role = authenticationService.createRole(roleRequest);
		return ResponseEntity.ok(role);
	}

	@PostMapping("/user/{email}/addRoles")
	public ResponseEntity<User> addRoles(@PathVariable String email,@RequestBody List<Long> roleIds) {
		User user = authenticationService.addRole(email, roleIds);
		return ResponseEntity.ok(user);
	}

}
