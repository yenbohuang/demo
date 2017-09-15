package org.yenbo.jetty.cxf;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.rs.security.oauth2.provider.DefaultResourceOwnerNameProvider;
import org.apache.cxf.rs.security.oauth2.provider.OAuthJSONProvider;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService;
import org.apache.cxf.rs.security.oauth2.services.AuthorizationCodeGrantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.yenbo.jetty.api.DemoService;
import org.yenbo.jetty.api.SecretService;
import org.yenbo.jetty.oauth2.AccessTokenRepository;
import org.yenbo.jetty.oauth2.AuthorizationCodeRepository;
import org.yenbo.jetty.oauth2.ClientRepository;
import org.yenbo.jetty.oauth2.InMemoryAuthorizationCodeDataProvider;
import org.yenbo.jetty.oauth2.ScopeRepository;
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
        
        Server server = factory.create();
        log.debug("Create server: {}", server.getEndpoint().getService().getName());
        return server;
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
		return new InMemoryAuthorizationCodeDataProvider();
	}
	
	@Bean
    public AuthorizationCodeGrantService authorizationCodeGrantService(
    		InMemoryAuthorizationCodeDataProvider dataProvider) {
		
    	AuthorizationCodeGrantService service = new AuthorizationCodeGrantService();
    	service.setCanSupportPublicClients(false);
    	service.setDataProvider(dataProvider);
    	service.setResourceOwnerNameProvider(new DefaultResourceOwnerNameProvider());
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
    public OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter() {
    	return new OAuthAuthorizationDataMessageBodyWriter();
    }
    
    @Bean
    public OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter() {
    	return new OAuthErrorMessageBodyWriter();
    }
    
	@Bean
    public Server oauth2Server(
    		OAuthAuthorizationDataMessageBodyWriter oAuthAuthorizationDataMessageBodyWriter,
    		OAuthErrorMessageBodyWriter oAuthErrorMessageBodyWriter,
    		AuthorizationCodeGrantService authorizationCodeGrantService,
    		AccessTokenService accessTokenService) {
		
        return createServerFactory(new Oauth2Application(),
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
	public ClientRepository clientRepository() {
		return new ClientRepository();
	}
	
	@Bean
	public ScopeRepository scopeRepository() {
		return new ScopeRepository();
	}
	
	@Bean
	public AuthorizationCodeRepository authorizationCodeRepository() {
		return new AuthorizationCodeRepository();
	}
	
	@Bean
	public AccessTokenRepository accessTokenRepository() {
		return new AccessTokenRepository();
	}
}
