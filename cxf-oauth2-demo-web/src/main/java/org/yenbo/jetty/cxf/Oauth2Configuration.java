package org.yenbo.jetty.cxf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeGrantHandler;
import org.apache.cxf.rs.security.oauth2.grants.refresh.RefreshTokenGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler;
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
	
	private static final boolean PARTIAL_MATCH_SCOPE_VALIDATION = true;
	private static final boolean CAN_SUPPORT_PUBLIC_CLIENTS = false;
	private static final long GRANT_LIFE_TIME = 360L;
	
	@Bean
	public InMemoryAuthorizationCodeDataProvider inMemoryAuthorizationCodeDataProvider() {
		
		InMemoryAuthorizationCodeDataProvider dataProvider =
				new InMemoryAuthorizationCodeDataProvider();
		dataProvider.setGrantLifetime(GRANT_LIFE_TIME);
		return dataProvider;
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
    	service.setCanSupportPublicClients(CAN_SUPPORT_PUBLIC_CLIENTS);
    	service.setDataProvider(dataProvider);
    	service.setResourceOwnerNameProvider(resourceOwnerNameProvider);
    	service.setPartialMatchScopeValidation(PARTIAL_MATCH_SCOPE_VALIDATION);
    	return service;
    }
	
	@Bean
	public AccessTokenService accessTokenService(InMemoryAuthorizationCodeDataProvider dataProvider) {
		
		RefreshTokenGrantHandler refreshTokenGrantHandler = new RefreshTokenGrantHandler();
		refreshTokenGrantHandler.setDataProvider(dataProvider);
		refreshTokenGrantHandler.setPartialMatchScopeValidation(PARTIAL_MATCH_SCOPE_VALIDATION);
		
		AuthorizationCodeGrantHandler authorizationCodeGrantHandler = new AuthorizationCodeGrantHandler();
		authorizationCodeGrantHandler.setCanSupportPublicClients(CAN_SUPPORT_PUBLIC_CLIENTS);
		authorizationCodeGrantHandler.setDataProvider(dataProvider);
		authorizationCodeGrantHandler.setPartialMatchScopeValidation(PARTIAL_MATCH_SCOPE_VALIDATION);
		
		List<AccessTokenGrantHandler> handlers = new ArrayList<>();
		handlers.add(authorizationCodeGrantHandler);
		handlers.add(refreshTokenGrantHandler);
		
		AccessTokenService service = new AccessTokenService();
		service.setDataProvider(dataProvider);
		service.setGrantHandlers(handlers);
		return service;
	}
    
	@Bean
    public Server oauth2Server(
    		OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter,
    		OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter,
    		AuthorizationCodeGrantService authorizationCodeGrantService,
    		AccessTokenService accessTokenService) {
		
        return CxfConfiguration.createServerFactory(new Oauth2Application(),
        		Arrays.<Object>asList(
        				new OAuthJSONProvider(),
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
