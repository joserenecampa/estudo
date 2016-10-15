package gov.demoiselle.web;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusEndpoint {

	@GET
	public Response status() {
		return Response.ok("OK").build();
	}

}
