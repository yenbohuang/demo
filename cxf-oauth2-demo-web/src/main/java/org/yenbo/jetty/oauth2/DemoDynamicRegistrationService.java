package org.yenbo.jetty.oauth2;

import java.util.UUID;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistrationResponse;
import org.apache.cxf.rs.security.oauth2.services.DynamicRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoDynamicRegistrationService extends DynamicRegistrationService {

	private static final Logger log = LoggerFactory.getLogger(DemoDynamicRegistrationService.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public DemoDynamicRegistrationService() {
		super();
	}
	
	@Override
	protected Client createNewClient(ClientRegistration request) {
		
		Client client = super.createNewClient(request);
		client.setClientId(UUID.randomUUID().toString());
		Oauth2Factory.fill(client, request);
		
		if (log.isDebugEnabled()) {
			try {
				log.debug("ClientRegistration={}", OBJECT_MAPPER.writeValueAsString(request));
				log.debug("Client={}", OBJECT_MAPPER.writeValueAsString(client));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return client;
	}
	
	@Override
	protected ClientRegistrationResponse fromClientToRegistrationResponse(Client client) {
		
		ClientRegistrationResponse response = super.fromClientToRegistrationResponse(client);
		Oauth2Factory.fill(response, client);
		
		if (log.isDebugEnabled()) {
			try {
				log.debug("Client={}", OBJECT_MAPPER.writeValueAsString(client));
				log.debug("ClientRegistrationResponse={}", OBJECT_MAPPER.writeValueAsString(response));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return response;
	}
}
