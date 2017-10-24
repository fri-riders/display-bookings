package com.fri.rso.fririders.displaybookings.controllers;


import com.fri.rso.fririders.displaybookings.database.Booking;
import com.fri.rso.fririders.displaybookings.database.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("bookings")
public class BookingResource {

    private static final Logger logger = Logger.getLogger( BookingResource.class.getName() );

    @GET
    public Response getAllBookings() {
        logger.info("REST CALL: getAllBookings");
        List<Booking> bookings = Database.getBookings();
        if(bookings != null && bookings.size() > 0) {
            logger.info("Found "+ bookings.size() + " bookings.");
            return Response.ok(bookings).build();
        }
        else {
            logger.warning("Zero bokings found");
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/{bookingId}")
    public Response getBooking(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBooking.");
        Booking booking = Database.getBooking(bookingId);
        if(booking != null)
            return Response.ok(booking).build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response addBooking(Booking b){
        logger.info("REST CALL: addBooking.");
        try {
            Database.addBooking(b);
            return Response.ok(b).build();
        }
        catch (Exception e){
            logger.warning(e.getMessage());
            return Response.status(Response.Status.CONFLICT).build();
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
            return Response.status(Response.Status.CONFLICT).build();
        }
    }
}
