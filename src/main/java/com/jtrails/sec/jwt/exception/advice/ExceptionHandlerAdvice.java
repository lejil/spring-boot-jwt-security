/**
 * 
 */
package com.jtrails.sec.jwt.exception.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jtrails.sec.jwt.exception.AuthenticationException;
import com.jtrails.sec.jwt.exception.ErrorResponse;

/**
 * @author Lejil
 *
 */
@RestControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticatioException(AuthenticationException ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK.value(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.OK.value(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.OK);
	}

}
