/**
 * 
 */
package com.jtrails.sec.jwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jtrails.sec.jwt.model.User;
import com.jtrails.sec.jwt.service.AuthenticationService;

/**
 * @author Lejil
 *
 */
@RequestMapping("user-service")
@RestController
public class AuthValidatorController {

	private final AuthenticationService authenticationService;

	AuthValidatorController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@GetMapping("hello")
	public ResponseEntity<String> helloWorld() {
		return ResponseEntity.ok("HelloWorld");
	}

	@GetMapping("user/{email}")
	public ResponseEntity<User> getUser(@PathVariable String email) {
		return ResponseEntity.ok(authenticationService.getUser(email));
	}

}
