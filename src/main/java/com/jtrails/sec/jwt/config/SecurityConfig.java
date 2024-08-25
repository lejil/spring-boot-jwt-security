/**
 * 
 */
package com.jtrails.sec.jwt.config;

/**
 * @author Lejil
 *
 */
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.jtrails.sec.jwt.filter.JwtSecurityFilter;
import com.jtrails.sec.jwt.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final UserRepository userRepository;

	private final JwtSecurityFilter jwtSecurityFilter;

	SecurityConfig(JwtSecurityFilter jwtSecurityFilter, UserRepository userRepository) {
		this.userRepository = userRepository;
		this.jwtSecurityFilter = jwtSecurityFilter;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						requests -> requests
								.requestMatchers(AntPathRequestMatcher.antMatcher("/auth/user/*/addRoles"),
										AntPathRequestMatcher.antMatcher("/auth/*"))
								.permitAll().anyRequest().authenticated())
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.userDetailsService(userDetailsService())
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtSecurityFilter, UsernamePasswordAuthenticationFilter.class).build();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return username -> userRepository.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found !"));
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;

	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
