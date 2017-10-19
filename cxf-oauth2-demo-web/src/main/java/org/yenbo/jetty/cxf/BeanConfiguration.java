package org.yenbo.jetty.cxf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.oauth2.AccessTokenRepository;
import org.yenbo.jetty.oauth2.AuthorizationCodeRepository;
import org.yenbo.jetty.oauth2.ClientRepository;
import org.yenbo.jetty.oauth2.RefreshTokenRepository;
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
	public AccessTokenRepository tokenRepository() {
		return new AccessTokenRepository();
	}
	
	@Bean
	public RefreshTokenRepository refreshTokenRepository() {
		return new RefreshTokenRepository();
	}
}
