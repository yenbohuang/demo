package org.yenbo.jetty.cxf;

import java.util.Arrays;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.data.InMemoryUser;
import org.yenbo.jetty.oauth2.DemoDynamicRegistrationService;
import org.yenbo.jetty.oauth2.DynamicRegistrationOAuthDataProvider;
import org.yenbo.jetty.oauth2.OAuthExtensionJSONProvider;

@Configuration
public class Oauth2DynamicRegistrationConfiguration {

	private static final String OAUTH_FILTER_BEAN_NAME = "oauthFilterForDynamicRegistration";
	
	@Bean
	public DynamicRegistrationOAuthDataProvider dynamicRegistrationOAuthDataProvider() {
		return new DynamicRegistrationOAuthDataProvider();
	}
	
	@Bean
	public DemoDynamicRegistrationService dynamicRegistrationService(
			DynamicRegistrationOAuthDataProvider dynamicRegistrationOAuthDataProvider) {
		
		DemoDynamicRegistrationService dynamicRegistrationService = new DemoDynamicRegistrationService();
		dynamicRegistrationService.setClientProvider(dynamicRegistrationOAuthDataProvider);
		dynamicRegistrationService.setUserRole(InMemoryUser.ROLE_ADMIN);
		dynamicRegistrationService.setSupportRegistrationAccessTokens(false);
		return dynamicRegistrationService;
	}
	
	@Bean(name = OAUTH_FILTER_BEAN_NAME)
	public OAuthRequestFilter oauthFilterForDynamicRegistration(
			DynamicRegistrationOAuthDataProvider dynamicRegistrationOAuthDataProvider) {
		
		OAuthRequestFilter filter = new OAuthRequestFilter();
		filter.setDataProvider(dynamicRegistrationOAuthDataProvider);		
		filter.setAllPermissionsMatch(false);
		filter.setBlockPublicClients(true);
		filter.setUseUserSubject(true);
		return filter;
	}
	
	@Bean
	public Server oauth2DynamicRegistrationServer(
			@Qualifier(OAUTH_FILTER_BEAN_NAME) OAuthRequestFilter oauthFilterForDynamicRegistration,
			DemoDynamicRegistrationService dynamicRegistrationService) {
		
		return CxfConfiguration.createServerFactory(new Oauth2DynamicRegistrationApplication(),
        		Arrays.<Object>asList(
        				new OAuthExtensionJSONProvider(),
        				new OAuthJSONProvider(),
        				oauthFilterForDynamicRegistration
        				),
        		Arrays.<Object>asList(
        				dynamicRegistrationService
                		)
        		);
	}
}
