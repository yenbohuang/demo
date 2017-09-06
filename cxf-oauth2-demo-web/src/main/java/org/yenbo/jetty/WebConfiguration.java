package org.yenbo.jetty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.provider.RequestDispatcherProvider;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.DemoService;
import org.yenbo.jetty.domain.InMemoryClient;
import org.yenbo.jetty.oauth2.InMemoryAuthorizationCodeDataProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan("org.yenbo.jetty")
public class WebConfiguration {

	private static final Logger log = LoggerFactory.getLogger(WebConfiguration.class);
	
	private JAXRSServerFactoryBean createServerFactory(Application application) {
		JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
				application, JAXRSServerFactoryBean.class);
        factory.setAddress(factory.getAddress());
        return factory;
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
    public Server demoServer(JacksonJsonProvider jsonProvider) {
		
        JAXRSServerFactoryBean factory = createServerFactory(new DemoApplication());
        
        factory.setProviders(Arrays.<Object>asList(
        		jsonProvider
        		));
        
        factory.setServiceBeans(Arrays.<Object>asList(
        		new DemoService()
        		));
        
        return factory.create();
    }
    
    private AuthorizationCodeGrantService authorizationCodeGrantService() {
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setDataProvider(new InMemoryAuthorizationCodeDataProvider());
    	return service;
    }
	
    private RequestDispatcherProvider requestDispatcherProvider() {
    	RequestDispatcherProvider provider = new RequestDispatcherProvider();
    	provider.setResourcePath("/login.jsp");
    	return provider;
    }
    
	@Bean
    public Server oauth2Server(JacksonJsonProvider jsonProvider) {
		
        JAXRSServerFactoryBean factory = createServerFactory(new Oauth2Application());
        
        factory.setProviders(Arrays.<Object>asList(
        		jsonProvider,
        		requestDispatcherProvider()
        		));
        
        factory.setServiceBeans(Arrays.<Object>asList(
        		authorizationCodeGrantService()
        		));
        
        return factory.create();
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
