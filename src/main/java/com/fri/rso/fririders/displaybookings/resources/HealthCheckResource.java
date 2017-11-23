package com.fri.rso.fririders.displaybookings.resources;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.kumuluz.ee.common.runtime.EeRuntime;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("health-demo")
public class HealthCheckResource {

    @Inject
    private ConfigProperties configProperties;


    @GET
    @Path("/instanceid")
    public Response getInstanceId() {

        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";

        return Response.ok(instanceId).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        configProperties.setHealthy(healthy);
        return Response.ok().build();
    }

}
