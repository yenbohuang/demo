package org.yenbo.jetty;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.yenbo.jetty.api.DemoService;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
@ComponentScan("org.yenbo.jetty")
public class WebConfiguration {

	@Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        return new SpringBus();
    }
	
	@Bean
	@DependsOn("cxf")
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
        		jaxRsApiApplication(), JAXRSServerFactoryBean.class);
        factory.setServiceBeans(Arrays.<Object>asList(
        		demoService()
        		));
        factory.setProviders(Arrays.<Object>asList(
        		jsonProvider()
        		));
        factory.setAddress(factory.getAddress());
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
}
