package org.yenbo.jetty.security;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yenbo.jetty.oauth2.ClientRepository;
import org.yenbo.jetty.view.OAuth2LoginView;

@Component
@Path("/login")
public class DemoLoginService {

	private static final Logger log = LoggerFactory.getLogger(DemoLoginService.class);
	
	@Autowired
	private ClientRepository clientRepository;
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response login(
			@QueryParam("error") String error,
			@QueryParam(OAuthConstants.CLIENT_ID) String clientId) {
		
		log.debug("Generate customized login form: error={}, clientId={}", error, clientId);
		
		OAuth2LoginView view = new OAuth2LoginView();
		view.setError(error != null);
		
		if (null != clientId) {
			
			Client client = clientRepository.getClient(UUID.fromString(clientId));
			
			if (null != client) {
				view.setAppName(client.getApplicationName());
			}
		}
		
		return Response.ok().entity(view).build();
	}
}
