package org.yenbo.jetty.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import org.yenbo.jetty.json.HelloJson;

@Component
@Path("/demo")
public class DemoService {

	@GET
	@Produces("application/json")
	public Response hello() {
		HelloJson json = new HelloJson();
		json.setMessage("Hello");
		return Response.ok().entity(json).build();
	}
}
