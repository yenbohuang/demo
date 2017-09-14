package org.yenbo.jetty.oauth2;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientRepository {

	private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);
	
	private Client client;
	
	public ClientRepository() {
		
		client = new Client();
		client.setAllowedGrantTypes(Arrays.<String>asList(
				OAuthConstants.AUTHORIZATION_CODE_GRANT,
				OAuthConstants.REFRESH_TOKEN
				));
		client.setClientId("78fa6a41-aec6-4690-9237-7cd6bb6e1a84");
		client.setClientSecret("7cd6bb6e1a84");
		client.setConfidential(true);
		client.setRedirectUris(Arrays.<String>asList(
				"http://localhost/unknown"));
		client.setRegisteredScopes(Arrays.<String>asList(
				"demo1", "demo2", "demo3"));
		client.setApplicationDescription("This is application description");
		client.setApplicationName("This is application name");
		
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
	
	public Client getClient(UUID clientId) {
		if (null != clientId && client.getClientId().equals(clientId.toString())) {
			return client;
		} else {
			return null;
		}
	}
}
