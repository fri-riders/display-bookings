package com.fri.rso.fririders.displaybookings.resources;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("health-demo")
@Log
public class HealthCheckResource {

    @Inject
    private ConfigProperties configProperties;

    private static final Logger logger = LogManager.getLogger( HealthCheckResource.class.getName() );


    @GET
    @Path("/instanceid")
    public Response getInstanceId() {
        logger.info("Returning instance id ...");
        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";

        return Response.ok(instanceId).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        logger.info("Setting health parameter to: " + healthy);

        configProperties.setHealthy(healthy);
        return Response.ok().build();
    }

    @POST
    @Path("load")
    public Response loadOrder(Integer n) {
        logger.info("Simulating heavy load ...");

        long fib = 0;
        for (int i = 1; i <= n; i++) {
            fib = fibonacci(i);
        }

        return Response.status(Response.Status.OK).entity(fib).build();
    }

    private long fibonacci(int n) {
        if (n <= 2) return n;
        else return fibonacci(n - 1) + fibonacci(n - 2) + fibonacci(n - 3);
    }

}
