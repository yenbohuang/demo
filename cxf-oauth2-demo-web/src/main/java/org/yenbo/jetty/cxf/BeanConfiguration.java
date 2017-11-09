package org.yenbo.jetty.cxf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.data.InMemoryClient;
import org.yenbo.jetty.repo.AccessTokenRepository;
import org.yenbo.jetty.repo.AuthorizationCodeRepository;
import org.yenbo.jetty.repo.ClientRepository;
import org.yenbo.jetty.repo.RefreshTokenRepository;
import org.yenbo.jetty.repo.ScopeRepository;

@Configuration
public class BeanConfiguration {
	
	private static final Logger log = LoggerFactory.getLogger(BeanConfiguration.class);
	
	@Bean
	public ClientRepository clientRepository() {
		
		InMemoryClient client = new InMemoryClient();
		client.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
		client.setClientSecret("7cd6bb6e1a84");
		client.getRedirectUris().add("http://localhost/unknown");
		client.getRedirectUris().add("https://www.getpostman.com/oauth2/callback"); // For testing by PostMan
		client.getScopes().add("demo1");
		client.getScopes().add("demo2");
		client.getScopes().add("demo3");
		client.setDescription("This is application description");
		client.setName("This is application name");
		
		// copy this line from log file and proceed with other tests
    	log.debug("client_id={}, client_secret={}", client.getClientId(), client.getClientSecret());
    	
    	for (String uri : client.getRedirectUris()) {
    		try {
    			log.debug("redirect_uri={}", URLEncoder.encode(uri, "UTF-8"));
    		} catch (UnsupportedEncodingException e) {
    			log.error(e.getMessage(), e);
    		}
    	}
		
    	ClientRepository repository = new ClientRepository();
    	repository.save(client, client.getClientId());
    	return repository;
	}
	
	@Bean
	public ScopeRepository scopeRepository() {
		return new ScopeRepository();
	}
	
	@Bean
	public AuthorizationCodeRepository authorizationCodeRepository() {
		return new AuthorizationCodeRepository();
	}
	
	@Bean
	public AccessTokenRepository tokenRepository() {
		return new AccessTokenRepository();
	}
	
	@Bean
	public RefreshTokenRepository refreshTokenRepository() {
		return new RefreshTokenRepository();
	}
}
