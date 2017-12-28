package com.fri.rso.fririders.displaybookings.resources;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.database.Database;
import com.fri.rso.fririders.displaybookings.entities.User;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;



@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("bookings")
@Log(LogParams.METRICS)
@Metered
public class BookingResource {

    private Logger logger = LogManager.getLogger( BookingResource.class.getName() );

    private static Client client = ClientBuilder.newClient();

    @Inject
    private ConfigProperties configProperties;

    @Inject
    @DiscoverService(value = "accommodations", version = "1.0.x", environment = "dev")
    private Optional<String> accommodationsUrl;

    @Inject
    @DiscoverService(value="users", version = "1.0.x", environment = "dev")
    private Optional<String> usersUrl;


    @GET
    @Metered
    public Response getAllBookings() {
        logger.info("REST CALL: getAllBookings");
        List<Booking> bookings = Database.getBookings();
        if(bookings != null && bookings.size() > 0) {
            logger.info("Found "+ bookings.size() + " bookings.");
            return Response.ok(bookings).build();
        }
        else {
            logger.error("Zero bookings found");
            return Response.status(Response.Status.NOT_FOUND).entity("Zero bookings found.").build();
        }
    }

    @GET
    @Path("/{bookingId}")
    public Response getBooking(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBooking.");
        Booking booking = Database.getBooking(bookingId);
        if(booking != null) {
            return Response.ok(booking).build();
        }
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Booking with id " + bookingId + " not found.").build();
    }

    @GET
    @Path("/{bookingId}/accommodation")
    public Response getBookingAccommodation(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingAccommodation.");
        if (!this.accommodationsUrl.isPresent())
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodations service cannot be reached.").build();

        Booking booking = Database.getBooking(bookingId);
        if(booking != null) {
            logger.info("Calling accommodations service.");
            String url = this.accommodationsUrl.get() + "/accommodations/all";
            logger.info("URL: " + url);

            //find info about accommodation
            List<Accommodation> accommodations =
                    client.target(url)
                            .request(MediaType.APPLICATION_JSON)
                            .get((new GenericType<List<Accommodation>>() {}));
            for(Accommodation a : accommodations)
                if(a.getId() == booking.getId())
                    return Response.ok(a).build();
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodation for requested booking not found.").build();
        }
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Booking with id " + bookingId + " not found.").build();
    }

    @GET
    @Path("/{bookingId}/user")
    public Response getBookingUser(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingUser.");
        if (!this.usersUrl.isPresent())
            return Response.status(Response.Status.NOT_FOUND).entity("Users service cannot be reached.").build();

        Booking booking = Database.getBooking(bookingId);
        if(booking != null) {
            String userId = booking.getIdUser();
            logger.info("Calling user service ...");
            String url = this.usersUrl.get() + "/v1/users/" + userId;
            logger.info("URL: " + url);

            //find info about user
            User user =
                    client.target(url)
                            .request(MediaType.APPLICATION_JSON)
                            .get((new GenericType<User>() {}));
            if (user != null) {
                logger.info("User with id" + userId+" successfully retrieved ...");
                return Response.ok(user).build();
            }
            return Response.status(Response.Status.NOT_FOUND).entity("User of requested booking not found.").build();
        }
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Booking with id " + bookingId + " not found.").build();
    }

    @POST
    public Response addBooking(Booking b){
        logger.info("REST CALL: addBooking.");
        // for etcd testing
        if(!configProperties.isInsertEnabled()){
            logger.error("Booking insertion is disabled.");
            return Response.status(Response.Status.FORBIDDEN).entity("Booking insertion is disabled.").build();
        }

        try {
            Database.addBooking(b);
            return Response.ok(b).build();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{bookingId}")
    public Response deleteBooking(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: deleteBooking.");
        try {
            Database.deleteBooking(bookingId);
            return Response.ok("Booking successfully deleted.").build();
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}
