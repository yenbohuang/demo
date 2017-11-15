package org.yenbo.jetty.cxf;

import java.util.Arrays;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.data.InMemoryUser;
import org.yenbo.jetty.oauth2.DemoDynamicRegistrationService;
import org.yenbo.jetty.oauth2.DynamicRegistrationOAuthDataProvider;
import org.yenbo.jetty.oauth2.OAuthExtensionJSONProvider;

@Configuration
public class Oauth2DynamicRegistrationConfiguration {

	public static final boolean SUPPORT_REGISTRATION_ACCESS_TOKEN = true;
	
	@Autowired
	private DynamicRegistrationOAuthDataProvider dynamicRegistrationOAuthDataProvider;
	@Autowired
	private DemoDynamicRegistrationService dynamicRegistrationService;
	
	@Bean
	public DynamicRegistrationOAuthDataProvider dynamicRegistrationOAuthDataProvider() {
		return new DynamicRegistrationOAuthDataProvider();
	}
	
	@Bean
	public DemoDynamicRegistrationService dynamicRegistrationService() {
		
		DemoDynamicRegistrationService dynamicRegistrationService = new DemoDynamicRegistrationService();
		dynamicRegistrationService.setClientProvider(dynamicRegistrationOAuthDataProvider);
		dynamicRegistrationService.setUserRole(InMemoryUser.ROLE_ADMIN);
		dynamicRegistrationService.setSupportRegistrationAccessTokens(SUPPORT_REGISTRATION_ACCESS_TOKEN);
		
		return dynamicRegistrationService;
	}
	
	@Bean
	public Server oauth2DynamicRegistrationServer() {
		
		return CxfConfiguration.createServerFactory(new Oauth2DynamicRegistrationApplication(),
        		Arrays.<Object>asList(
        				new OAuthExtensionJSONProvider(),
        				new OAuthJSONProvider()
        				),
        		Arrays.<Object>asList(
        				dynamicRegistrationService
                		)
        		);
	}
}
