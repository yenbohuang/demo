package org.yenbo.jetty.cxf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.repo.AccessTokenRepository;
import org.yenbo.jetty.repo.AuthorizationCodeRepository;
import org.yenbo.jetty.repo.ClientRepository;
import org.yenbo.jetty.repo.RefreshTokenRepository;
import org.yenbo.jetty.repo.ScopeRepository;

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
