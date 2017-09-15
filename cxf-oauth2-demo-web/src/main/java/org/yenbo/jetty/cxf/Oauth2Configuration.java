package org.yenbo.jetty.cxf;

import java.util.Arrays;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.rs.security.oauth2.provider.DefaultResourceOwnerNameProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.provider.ResourceOwnerNameProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.oauth2.InMemoryAuthorizationCodeDataProvider;
import org.yenbo.jetty.thymeleaf.OAuthAuthorizationDataMessageBodyWriter;
import org.yenbo.jetty.thymeleaf.OAuthErrorMessageBodyWriter;

@Configuration
public class Oauth2Configuration {

	@Bean
	public OAuthJSONProvider oauthJSONProvider() {
		return new OAuthJSONProvider();
	}
	
	@Bean
	public InMemoryAuthorizationCodeDataProvider inMemoryAuthorizationCodeDataProvider() {
		return new InMemoryAuthorizationCodeDataProvider();
	}
	
	@Bean
	public ResourceOwnerNameProvider resourceOwnerNameProvider() {
		// return login name on UI
		return new DefaultResourceOwnerNameProvider();
	}
	
	@Bean
    public AuthorizationCodeGrantService authorizationCodeGrantService(
    		InMemoryAuthorizationCodeDataProvider dataProvider,
    		ResourceOwnerNameProvider resourceOwnerNameProvider) {
		
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setCanSupportPublicClients(false);
    	service.setDataProvider(dataProvider);
    	service.setResourceOwnerNameProvider(resourceOwnerNameProvider);
    	service.setPartialMatchScopeValidation(true);
    	return service;
    }
	
	@Bean
	public AccessTokenService accessTokenService(InMemoryAuthorizationCodeDataProvider dataProvider) {
		
		AccessTokenService service = new AccessTokenService();
		service.setDataProvider(dataProvider);
		return service;
	}
    
	@Bean
    public Server oauth2Server(
    		OAuthJSONProvider oauthJSONProvider,
    		OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter,
    		OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter,
    		AuthorizationCodeGrantService authorizationCodeGrantService,
    		AccessTokenService accessTokenService) {
		
        return CxfConfiguration.createServerFactory(new Oauth2Application(),
        		Arrays.<Object>asList(
        				oauthJSONProvider,
        				oAuthAuthorizationDataMessageBodyWriter,
        				oAuthErrorMessageBodyWriter
        				),
        		Arrays.<Object>asList(
                		authorizationCodeGrantService,
                		accessTokenService
                		)
        		);
    }
	
	@Bean
    public OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter() {
    	return new OAuthAuthorizationDataMessageBodyWriter();
    }
    
    @Bean
    public OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter() {
    	return new OAuthErrorMessageBodyWriter();
    }
}
