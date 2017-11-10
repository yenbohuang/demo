package org.yenbo.jetty.oauth2;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;
import org.apache.cxf.rs.security.oauth2.services.DynamicRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoDynamicRegistrationService extends DynamicRegistrationService {

	private static final Logger log = LoggerFactory.getLogger(DemoDynamicRegistrationService.class);
	
	public DemoDynamicRegistrationService() {
		super();
	}

	private UUID createClientIdFromSoftwareId(String softwareId) {
		
		UUID clientId = null;
		
		if (StringUtils.isNotBlank(softwareId)) {
			
			try {
				clientId = UUID.fromString(softwareId);
				log.debug("Create client_id from software_id: {}", clientId);
			} catch (IllegalArgumentException ex) {
				log.error("Invalid software ID format", ex);
			}
		}
		
		if (clientId != null) {
			return clientId;
		} else {
			return UUID.randomUUID();
		}
	}
	
	@Override
	protected Client createNewClient(ClientRegistration request) {
		
		Client client = super.createNewClient(request);
		
		client.setApplicationDescription(
				request.getStringProperty(OAuthExtensionConstants.CLIENT_DESCRIPTION));
		client.setClientId(createClientIdFromSoftwareId(
				request.getStringProperty(OAuthExtensionConstants.SOFTWARE_ID)).toString());
		
		// TODO client name L10N???
		
		return client;
	}
}
