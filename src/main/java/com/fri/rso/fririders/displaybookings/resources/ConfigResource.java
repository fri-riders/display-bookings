package com.fri.rso.fririders.displaybookings.resources;

import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@RequestScoped
@Path("/config")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Log
public class ConfigResource {
    @Inject
    private ConfigProperties properties;

    private static final Logger logger = LogManager.getLogger( ConfigResource.class.getName() );

    @GET
    @Path("")
    public Response getProperties() {
        logger.info("Is insert enabled: " + properties.isInsertEnabled());

        String response =
                "{\"Insert enabled\": %b,\"Instance healthy\": %b}";

        response = String.format(
                response,
                properties.isInsertEnabled(),
                properties.isHealthy());

        return Response.ok(response).build();
    }

    @POST
    @Path("/config/bookingsInsert")
    public Response bookingsInsert(boolean isEnabled){
        properties.setInsertEnabled(isEnabled);
        return  Response.ok().entity("Bookings insertion is now: " + (isEnabled ? "enabled." : "disabled.")).build();
    }

}
