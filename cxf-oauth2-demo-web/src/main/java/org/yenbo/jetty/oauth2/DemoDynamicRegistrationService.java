package org.yenbo.jetty.oauth2;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistration;
import org.apache.cxf.rs.security.oauth2.services.ClientRegistrationResponse;
import org.apache.cxf.rs.security.oauth2.services.DynamicRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yenbo.jetty.cxf.Oauth2DynamicRegistrationConfiguration;
import org.yenbo.jetty.data.InMemoryUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DemoDynamicRegistrationService extends DynamicRegistrationService {

	private static final Logger log = LoggerFactory.getLogger(DemoDynamicRegistrationService.class);
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	public DemoDynamicRegistrationService() {
		super();
	}
	
	@Override
	protected String generateClientId() {
		return UUID.randomUUID().toString();
	}
	
	@Override
	protected Client createNewClient(ClientRegistration request) {
		
		Client client = super.createNewClient(request);
		Oauth2Factory.fill(request, client);
		
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
		Oauth2Factory.fill(client, response);
		
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
	
	@Override
	protected ClientRegistration fromClientToClientRegistration(Client client) {
		
		ClientRegistration clientRegistration = super.fromClientToClientRegistration(client);
        
        if (Oauth2DynamicRegistrationConfiguration.SUPPORT_REGISTRATION_ACCESS_TOKEN) {
            // both registration access token and uri are either included or excluded
        	UriBuilder ub = getMessageContext().getUriInfo().getAbsolutePathBuilder();
        	
        	clientRegistration.setProperty(ClientRegistrationResponse.REG_CLIENT_URI,
        			ub.build().toString());
            clientRegistration.setProperty(ClientRegistrationResponse.REG_ACCESS_TOKEN,
    				getRequestAccessToken());
        }
		
		Oauth2Factory.fill(client, clientRegistration);
		
		if (log.isDebugEnabled()) {
			try {
				log.debug("Client={}", OBJECT_MAPPER.writeValueAsString(client));
				log.debug("ClientRegistrationResponse={}", OBJECT_MAPPER.writeValueAsString(
						clientRegistration));
			} catch (JsonProcessingException e) {
				log.error(e.getMessage(), e);
			}
		}
		
		return clientRegistration;
	}
	
	@Override
	protected void checkInitialAuthentication() {
		
		String accessToken = getRequestAccessToken();
        if (!InMemoryUser.ACCESS_TOKEN_ADMIN.equals(accessToken)) {
        	log.debug("Initial access token not match: {}", accessToken);
            throw ExceptionUtils.toNotAuthorizedException(null, null);
        } else {
        	log.debug("Initial access token matched.");
        }
	}
	
	@Override
	protected String createRegAccessToken(Client client) {
		
		String accessToken = getRequestAccessToken();
		client.getProperties().put(ClientRegistrationResponse.REG_ACCESS_TOKEN, accessToken);
		return accessToken;
	}
	
	@Override
	protected void checkRegistrationAccessToken(Client c, String accessToken) {
		
		// only check if access token belongs to admin user
		if (!InMemoryUser.ACCESS_TOKEN_ADMIN.equals(accessToken)) {
			log.debug("Token not match: expected={}, actual={}",
					InMemoryUser.ACCESS_TOKEN_ADMIN, accessToken);
			throw ExceptionUtils.toNotAuthorizedException(null, null);
		}
	}
	
	@Override
	@PUT
    @Path("{clientId}")
    @Consumes("application/json")
    public Response updateClientRegistration(@PathParam("clientId") String clientId) {
		
		// TODO implement
        return Response.ok().build();
    }
}
