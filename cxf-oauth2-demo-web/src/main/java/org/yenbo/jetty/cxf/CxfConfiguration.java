package org.yenbo.jetty.cxf;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.oauth2.provider.DefaultResourceOwnerNameProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.DemoService;
import org.yenbo.jetty.api.SecretService;
import org.yenbo.jetty.domain.InMemoryClient;
import org.yenbo.jetty.oauth2.InMemoryAuthorizationCodeDataProvider;
import org.yenbo.jetty.thymeleaf.OAuthAuthorizationDataMessageBodyWriter;
import org.yenbo.jetty.thymeleaf.OAuthErrorMessageBodyWriter;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan("org.yenbo.jetty")
public class CxfConfiguration {

	private static final Logger log = LoggerFactory.getLogger(CxfConfiguration.class);
	
	private Server createServerFactory(Application application,
			List<Object> providers, List<Object> serviceBeans) {
		
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
				application, JAXRSServerFactoryBean.class);
        factory.setAddress(factory.getAddress());
        factory.setProviders(providers);
        factory.setServiceBeans(serviceBeans);
        return factory.create();
	}
		
	@Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }
	
	@Bean
    public JacksonJsonProvider jsonProvider() {
    	return new JacksonJsonProvider();
    }
	
	@Bean
	public OAuthJSONProvider oauthJSONProvider() {
		return new OAuthJSONProvider();
	}
	
	@Bean
    public Server demoServer(JacksonJsonProvider jsonProvider) {
        return createServerFactory(new DemoApplication(),
        		Arrays.<Object>asList(jsonProvider),
        		Arrays.<Object>asList(new DemoService()));
    }
	
	@Bean
    public Server secretServer(JacksonJsonProvider jsonProvider) {
		
        return createServerFactory(new SecuredApplication(),
        		Arrays.<Object>asList(jsonProvider),
        		Arrays.<Object>asList(new SecretService()));
    }
    
	@Bean
	public InMemoryAuthorizationCodeDataProvider inMemoryAuthorizationCodeDataProvider() {
		
		// TODO how do we reload supported scopes?
		Map<String, String> scopes = new HashMap<>();
    	scopes.put("demo1", "description for demo1");
    	scopes.put("demo2", "description for demo2");
    	scopes.put("demo3", "description for demo3");
    	
    	InMemoryAuthorizationCodeDataProvider dataProvider = new InMemoryAuthorizationCodeDataProvider();
    	dataProvider.setSupportedScopes(scopes);
    	
    	return dataProvider;
	}
	
	@Bean
    public AuthorizationCodeGrantService authorizationCodeGrantService(
    		InMemoryAuthorizationCodeDataProvider dataProvider) {
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setDataProvider(dataProvider);
    	service.setResourceOwnerNameProvider(new DefaultResourceOwnerNameProvider());
    	return service;
    }
    
    @Bean
    public OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter() {
    	OAuthAuthorizationDataMessageBodyWriter writer = new OAuthAuthorizationDataMessageBodyWriter();
    	writer.setTemplate("OAuthAuthorizationData");
    	return writer;
    }
    
    @Bean
    public OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter() {
    	OAuthErrorMessageBodyWriter writer = new OAuthErrorMessageBodyWriter();
    	writer.setTemplate("OAuthError");
    	return writer;
    }
    
	@Bean
    public Server oauth2Server(
    		JacksonJsonProvider jsonProvider,
    		OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter,
    		OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter,
    		AuthorizationCodeGrantService authorizationCodeGrantService) {
		
        return createServerFactory(new Oauth2Application(),
        		Arrays.<Object>asList(
        				jsonProvider,
        				oAuthAuthorizationDataMessageBodyWriter,
        				oAuthErrorMessageBodyWriter
        				),
        		Arrays.<Object>asList(
                		authorizationCodeGrantService
                		)
        		);
    }
    
    @Bean
    public InMemoryClient inMemoryClients() {
    	
    	InMemoryClient client = new InMemoryClient();
    	
    	// this is hardcoded for demo.
    	client.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
    	client.setClientSecret("7cd6bb6e1a84");
    	client.setRedirectUri("http://localhost/unknown");
    	
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
