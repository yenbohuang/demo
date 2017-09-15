package org.yenbo.jetty.cxf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.oauth2.TokenRepository;
import org.yenbo.jetty.oauth2.AuthorizationCodeRepository;
import org.yenbo.jetty.oauth2.ClientRepository;
import org.yenbo.jetty.oauth2.ScopeRepository;

@Configuration
public class BeanConfiguration {
	
	@Bean
	public ClientRepository clientRepository() {
		return new ClientRepository();
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
	public TokenRepository tokenRepository() {
		return new TokenRepository();
	}
}
