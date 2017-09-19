package org.yenbo.jetty.cxf;

import java.util.Arrays;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.rs.security.oauth2.filters.OAuthRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.OAuth2ResourceService;
import org.yenbo.jetty.oauth2.InMemoryAuthorizationCodeDataProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
public class ResourceConfiguration {
	
	@Bean
	public OAuthRequestFilter oauthFilter(
			InMemoryAuthorizationCodeDataProvider inMemoryAuthorizationCodeDataProvider) {
		
		OAuthRequestFilter filter = new OAuthRequestFilter();
		filter.setDataProvider(inMemoryAuthorizationCodeDataProvider);
		
		// requiredScopes = [demo1, demo2], registeredScopes = [demo1, demo2, demo3]
		// When allPermissionsMatch is false: [demo1] -> fail, [demo1, demo2] -> pass, [demo1, demo2, demo3] -> pass
		// When allPermissionsMatch is true:  [demo1] -> fail, [demo1, demo2] -> pass, [demo1, demo2, demo3] -> fail
		filter.setAllPermissionsMatch(false);
		filter.setRequiredScopes(Arrays.<String>asList("demo1", "demo2"));
		
		filter.setBlockPublicClients(true);
		return filter;
	}
	
	@Bean
    public Server resourceServer(
    		JacksonJsonProvider jsonProvider,
    		OAuthRequestFilter oauthFilter) {
		
        return CxfConfiguration.createServerFactory(new ResourceApplication(),
        		Arrays.<Object>asList(
        				jsonProvider,
        				oauthFilter),
        		Arrays.<Object>asList(new OAuth2ResourceService()));
    }
}
