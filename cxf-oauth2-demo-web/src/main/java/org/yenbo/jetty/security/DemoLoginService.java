package org.yenbo.jetty.security;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yenbo.jetty.view.OAuth2LoginView;

@Component
@Path("/login")
public class DemoLoginService {

	private static final Logger log = LoggerFactory.getLogger(DemoLoginService.class);
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response login(
			@QueryParam("error") String error) {
		
		log.debug("Generate customized login form: error={}", error);
		
		OAuth2LoginView view = new OAuth2LoginView();
		view.setError(error != null);
		return Response.ok().entity(view).build();
	}
}