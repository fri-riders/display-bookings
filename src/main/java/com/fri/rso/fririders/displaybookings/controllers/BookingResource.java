package com.fri.rso.fririders.displaybookings.controllers;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.database.Database;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("bookings")
public class BookingResource {

    private static final Logger logger = Logger.getLogger( BookingResource.class.getName() );

    private static Client client = ClientBuilder.newClient();

    @Inject
    private ConfigProperties configProperties;

    /*
    * 'accommodations' microservice url
    * */
    private static String accommodationsHost = "http://localhost";

    /*
    * 'accommodations' microservice port
    * */
    private static String accommodationsPort = "3001";

    @GET
    public Response getAllBookings() {
        logger.info("REST CALL: getAllBookings");
        List<Booking> bookings = Database.getBookings();
        if(bookings != null && bookings.size() > 0) {
            logger.info("Found "+ bookings.size() + " bookings.");
            return Response.ok(bookings).build();
        }
        else {
            logger.warning("Zero bookings found");
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
        Booking booking = Database.getBooking(bookingId);
        if(booking != null) {
            //find info about accommodation
            List<Accommodation> accommodations =
                    client.target(this.accommodationsHost + ":" + this.accommodationsPort + "/accommodations/all")
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

    @POST
    public Response addBooking(Booking b){
        logger.info("REST CALL: addBooking.");
        // for etcd testing
        if(!configProperties.isInsertEnabled()){
            logger.warning("Booking insertion is disabled.");
            return Response.status(Response.Status.FORBIDDEN).entity("Booking insertion is disabled.").build();
        }

        try {
            Database.addBooking(b);
            return Response.ok(b).build();
        }
        catch (Exception e){
            logger.warning(e.getMessage());
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
            logger.warning(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}
