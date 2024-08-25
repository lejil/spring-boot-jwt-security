/**
 * 
 */
package com.jtrails.sec.jwt.filter;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jtrails.sec.jwt.exception.ErrorResponse;
import com.jtrails.sec.jwt.service.SecurityService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtSecurityFilter extends OncePerRequestFilter {

	private static final Logger LOG = LoggerFactory.getLogger(JwtSecurityFilter.class);

	private final SecurityService securityService;
	private final ObjectMapper objectMapper;

	JwtSecurityFilter(SecurityService securityService, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.securityService = securityService;
	}

	private static final String INVALID_TOKEN = "Missing or invalid token";

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (request.getRequestURI().startsWith("/auth")) {
			filterChain.doFilter(request, response);
			return;
		} else if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			createErrorResponse(response, INVALID_TOKEN);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userEmail = securityService.getUserName(jwt);
			Collection<GrantedAuthority> roles = securityService.getGrantedAuthority(jwt);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (userEmail != null && authentication == null) {
				if (securityService.isTokenValid(jwt, userEmail)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail,
							null, roles);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				} else {
					createErrorResponse(response, INVALID_TOKEN);
					return;
				}
			}
			filterChain.doFilter(request, response);
		} catch (Exception exception) {
			LOG.error(exception.getMessage());
			createErrorResponse(response, exception.getMessage());
		}
	}

	private void createErrorResponse(HttpServletResponse response, String errorMessage)
			throws IOException {
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), errorMessage);
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}

}