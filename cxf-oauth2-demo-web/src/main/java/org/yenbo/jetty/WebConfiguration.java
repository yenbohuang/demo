package org.yenbo.jetty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.DemoService;
import org.yenbo.jetty.domain.InMemoryClient;
import org.yenbo.jetty.oauth2.InMemoryDataProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan("org.yenbo.jetty")
public class WebConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WebConfiguration.class);
	
	@Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }
	
	@Bean
    public Server jaxRsServer(
    		JaxRsApiApplication jaxRsApiApplication,
    		JacksonJsonProvider jsonProvider,
    		DemoService demoService,
    		AuthorizationCodeGrantService authorizationCodeGrantService,
    		AccessTokenService accessTokenService) {
		
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
        		jaxRsApiApplication, JAXRSServerFactoryBean.class);
        factory.setProviders(Arrays.<Object>asList(jsonProvider));
        factory.setAddress(factory.getAddress());
        
        factory.setServiceBeans(Arrays.<Object>asList(
        		demoService,
        		authorizationCodeGrantService,
        		accessTokenService
        		));
        return factory.create();
    }
	
    @Bean
    public JaxRsApiApplication jaxRsApiApplication() {
        return new JaxRsApiApplication();
    }
    
    @Bean
    public JacksonJsonProvider jsonProvider() {
    	return new JacksonJsonProvider();
    }

    @Bean 
    public DemoService demoService() {
        return new DemoService();
    }
    
    @Bean
    public OAuthDataProvider oauthDataProvider() {
    	return new InMemoryDataProvider();
    }
    
    @Bean
    public AccessTokenService accessTokenService(OAuthDataProvider oauthDataProvider) {
    	AccessTokenService service = new AccessTokenService();
    	service.setDataProvider(oauthDataProvider);
    	return service;
    }
    
    @Bean
    public AuthorizationCodeGrantService authorizationCodeGrantService(
    		OAuthDataProvider oauthDataProvider) {
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setDataProvider(oauthDataProvider);
    	return service;
    }
    
    @Bean
    public InMemoryClient inMemoryClients() {
    	
    	InMemoryClient client = new InMemoryClient();
    	
    	// this is hardcoded for demo.
    	client.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
    	client.setClientSecret("7cd6bb6e1a84");
    	client.setRedirectUri("http://localhost:8080/oauth2/demo");
    	
    	// copy this line from log file and proceed with other tests
    	try {
			log.info("clientId={}, clientSecret={}, redirectUri={}",
					client.getClientId(), client.getClientSecret(),
					URLEncoder.encode(client.getRedirectUri(), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
    	
    	return client;
    }
}
