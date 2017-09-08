package org.yenbo.jetty;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.SpringServletContainerInitializer;
import org.yenbo.jetty.config.cxf.CxfConfiguration;

public class StartJetty {

	private static final Logger log = LoggerFactory.getLogger(StartJetty.class);
	
	public static void main(String[] args) {
		
		Server server = new Server(8080);
        
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setResourceBase(
        		StartJetty.class.getProtectionDomain().getCodeSource().getLocation().toString());
        webapp.setConfigurations(new Configuration[] {
        		new WebInfConfiguration(),
        		new AnnotationConfiguration()
        		});

        webapp.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
            ".*/[^/]*\\.jar$|.*/target/classes/.*|.*/target/test-classes/.*");
        
        List<ContainerInitializer> initializers = new ArrayList<>();
        initializers.add(new ContainerInitializer(
                new SpringServletContainerInitializer(),
                new Class[] {CxfConfiguration.class}
                ));
        webapp.setAttribute("org.eclipse.jetty.containerInitializers", initializers);
        
        server.insertHandler(webapp);
        
        try {
        	server.start();
        	server.dumpStdErr();
        	server.join();
        } catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
