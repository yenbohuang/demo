package org.yenbo.jetty.config.cxf;

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
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
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
import org.yenbo.jetty.oauth2.OAuthAuthorizationDataMessageBodyWriter;

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
    
    private AuthorizationCodeGrantService authorizationCodeGrantService() {
    	
    	Map<String, OAuthPermission> permissionMap = new HashMap<>();
    	permissionMap.put("demo1", new OAuthPermission("demo1"));
    	permissionMap.put("demo2", new OAuthPermission("demo2"));
    	
    	InMemoryAuthorizationCodeDataProvider dataProvider = new InMemoryAuthorizationCodeDataProvider();
    	dataProvider.setPermissionMap(permissionMap);
    	
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setDataProvider(dataProvider);
    	return service;
    }
    
    @Bean
    public OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter() {
    	return new OAuthAuthorizationDataMessageBodyWriter();
    }
    
	@Bean
    public Server oauth2Server(JacksonJsonProvider jsonProvider,
    		OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter) {
		
        return createServerFactory(new Oauth2Application(),
        		Arrays.<Object>asList(
        				jsonProvider,
        				oAuthAuthorizationDataMessageBodyWriter
        				),
        		Arrays.<Object>asList(
                		authorizationCodeGrantService()
                		)
        		);
    }
    
    @Bean
    public InMemoryClient inMemoryClients() {
    	
    	InMemoryClient client = new InMemoryClient();
    	
    	// this is hardcoded for demo.
    	client.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
    	client.setClientSecret("7cd6bb6e1a84");
    	client.setRedirectUri("http://localhost:8080/api/demo");
    	
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
