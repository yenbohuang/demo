package org.yenbo.jetty;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.DemoService;
import org.yenbo.jetty.oauth2.OAuthMemoryDataProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan("org.yenbo.jetty")
public class WebConfiguration {

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
    	return new OAuthMemoryDataProvider();
    }
    
    @Bean
    public AccessTokenService accessTokenService(OAuthDataProvider oauthDataProvider) {
    	AccessTokenService service = new AccessTokenService();
    	service.setDataProvider(oauthDataProvider);
    	return service;
    }
    
    @Bean
    public AuthorizationCodeGrantService authorizationCodeGrantService(OAuthDataProvider oauthDataProvider) {
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setDataProvider(oauthDataProvider);
    	return service;
    }
}
