package org.yenbo.jetty.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Do not use this class since it is only for demo.
 *
 */
public class StupidPasswordEncoder implements PasswordEncoder {

	private static final Logger log = LoggerFactory.getLogger(StupidPasswordEncoder.class);
	
	@Override
	public String encode(CharSequence rawPassword) {
		String encrypted = String.format("1234-%s-abcd", rawPassword);
		log.debug(encrypted);
		return encrypted;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		log.debug("rawPassword={}, encodedPassword={}", rawPassword, encodedPassword);
		return encodedPassword.equals(encode(rawPassword));
	}

}
