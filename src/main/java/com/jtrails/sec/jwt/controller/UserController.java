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

/**
 * @author Lejil
 *
 */

@RequestMapping("users")
@RestController
public class UserController {

	@GetMapping("/user/{email}")
	public ResponseEntity<User> getUser(@PathVariable String email) {
		User user = new User(email, "Lejil", "test");
		return ResponseEntity.ok(user);
	}

}
