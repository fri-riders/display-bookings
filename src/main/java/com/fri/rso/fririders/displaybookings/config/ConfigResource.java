package com.fri.rso.fririders.displaybookings.config;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@RequestScoped
@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConfigResource {
    @Inject
    private ConfigProperties properties;

    private static final Logger logger = Logger.getLogger( ConfigResource.class.getName() );

    @GET
    @Path("")
    public Response getProperties() {
        logger.info("Is insert enabled: " + properties.isInsertEnabled());

        String response =
                "{\"booleanProperty\": %b}";

        response = String.format(
                response,
                properties.isInsertEnabled());

        return Response.ok(response).build();
    }

    //TODO: this should be @POST
    @GET
    @Path("bookingsInsert/{isEnabled}")
    public Response bookingsInsert(@PathParam("isEnabled") boolean isEnabled){
        properties.setInsertEnabled(isEnabled);
        return  Response.ok().entity("Bookings insertion is now: " + (isEnabled ? "enabled." : "disabled.")).build();
    }

}
