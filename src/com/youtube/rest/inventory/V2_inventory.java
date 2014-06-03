package com.youtube.rest.inventory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONArray;

/**
 * Version 2 of inventory.
 * 
 * We are version controlling via the URL path.
 * This method of version controlling makes the changes more
 * transparent to the users and developers.
 * It allows the uses to have more time for transition.
 * 
 * @author Jack
 *
 */
@Path("/v2/inventory/")
public class V2_inventory {
	
	/**
	 * This method will return the specific brand of PC parts the user is looking for.
	 * It uses QueryParam to bring in the data to the method.
	 * 
	 * Example:
	 * http://localhost:7001/com.youtube.rest/api/v2/inventory?brand=ASUS
	 * 
	 * @param brand - product brand name
	 * @return - JSON array result list from the database 
	 * @throws Exception
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response returnAllPcParts (@QueryParam("brand") String brand) throws Exception {
		
		String returnString = null;
		JSONArray jsonArray = new JSONArray();
		
		try {
			
		}
		catch (Exception e) {
			e.printStackTrace();
			Response.status(500).entity("Server was not able to process your request.").build();
		}
		
		
		return Response.ok(returnString).build();
	}

}