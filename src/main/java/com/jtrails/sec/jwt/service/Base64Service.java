/**
 * 
 */
package com.jtrails.sec.jwt.service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.PostConstruct;

@Component
public class Base64Service {

	private static final String ALGORITHM = "AES";

	private SecretKeySpec secretKey;

	@Value("${aes.secret}")
	private String secret;

	@PostConstruct
	private void init() {

		if (secret.length() < 16) {
			throw new IllegalArgumentException("Secret key must be 16 characters long.");
		}
		this.secretKey = new SecretKeySpec(getSigningKey(secret), ALGORITHM);
	}

	public byte[] getSigningKey(String secret) {
		return Decoders.BASE64.decode(secret);

	}

	public String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(encryptedBytes);
		} catch (Exception e) {
			throw new RuntimeException("Error while encrypting  " + e.getMessage(), e);
		}
	}

	public String decrypt(String base64EncodedData) {
		try {
			Cipher cipher = Cipher.getInstance(ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedBytes = Base64.getDecoder().decode(base64EncodedData);
			byte[] decryptedBytes = cipher.doFinal(decodedBytes);
			return new String(decryptedBytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Error while decrypting: " + e.getMessage(), e);
		}
	}
}
