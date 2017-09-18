package org.yenbo.jetty.security;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoUserDetailsService implements UserDetailsService {

	private static final Logger log = LoggerFactory.getLogger(DemoUserDetailsService.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		if (false == "user".equals(username)) {
			throw new UsernameNotFoundException("Username not found: " + username);
		}
		
		User user = new User(username, new StandardPasswordEncoder().encode("password"),
				new ArrayList<>());
		
		try {
			// Do not log password in production environment. This is only for demo.
			log.debug("{}", OBJECT_MAPPER.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
		
		return user;
	}

}
