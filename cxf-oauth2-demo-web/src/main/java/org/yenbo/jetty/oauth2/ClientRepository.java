package org.yenbo.jetty.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.oauth2.data.InMemoryClient;

public class ClientRepository {

	private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);
	
	private InMemoryClient client;
	
	public ClientRepository() {
		
		client = new InMemoryClient();
		client.setClientId(UUID.fromString("78fa6a41-aec6-4690-9237-7cd6bb6e1a84"));
		client.setClientSecret("7cd6bb6e1a84");
		client.getRedirectUris().add("http://localhost/unknown");
		client.getRedirectUris().add("https://www.getpostman.com/oauth2/callback"); // For testing by PostMan
		client.getScopes().add("demo1");
		client.getScopes().add("demo2");
		client.getScopes().add("demo3");
		client.setDescription("This is application description");
		client.setName("This is application name");
		
		// copy this line from log file and proceed with other tests
    	log.debug("client_id={}, client_secret={}", client.getClientId(), client.getClientSecret());
    	
    	for (String uri : client.getRedirectUris()) {
    		try {
    			log.debug("redirect_uri={}", URLEncoder.encode(uri, "UTF-8"));
    		} catch (UnsupportedEncodingException e) {
    			log.error(e.getMessage(), e);
    		}
    	}
	}
	
	public InMemoryClient get(UUID clientId) {
		if (null != clientId && client.getClientId().equals(clientId)) {
			log.debug("Found: {}", clientId);
			return client;
		} else {
			log.debug("Not found: {}", clientId);
			return null;
		}
	}
}
