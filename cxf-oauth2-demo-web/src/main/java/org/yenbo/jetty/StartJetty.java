package org.yenbo.jetty;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartJetty {

	private static final Logger log = LoggerFactory.getLogger(StartJetty.class);
	
	public static void main(String[] args) {
		
		Server server = new Server(8080);
        server.setHandler(new DemoHandler());
        
        try {
        	server.start();
        	server.join();
        } catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
