package com.fri.rso.fririders.displaybookings.resources;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.database.Database;
import com.fri.rso.fririders.displaybookings.entities.User;
import com.fri.rso.fririders.displaybookings.services.BookingsBean;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;

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

    @Inject
    private ConfigProperties configProperties;

    @Inject
    private BookingsBean bookingsBean;


    @GET
    @Metered
    public Response getAllBookings() {
        logger.info("REST CALL: getAllBookings");
        List<Booking> bookings = bookingsBean.getAllBookings();
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
        Booking booking = bookingsBean.getBooking(bookingId);
        if(booking != null) {
            return Response.ok(booking).build();
        }
        else
            return Response.status(Response.Status.NOT_FOUND).entity("Booking with id " + bookingId + " not found.").build();
    }

    @GET
    @Path("/{bookingId}/accommodation")
    @Timed(name = "get_booking_accommodation")
    public Response getBookingAccommodation(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingAccommodation.");
        try {
            Accommodation accommodation = bookingsBean.getBookingAccommodation(bookingId);
            if (accommodation != null)
                return Response.ok(accommodation).build();
            else {
                logger.error("Accommodation for requested booking not found.");
                return Response.status(Response.Status.NOT_FOUND).entity("Accommodation for requested booking not found.").build();
            }
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodation for requested booking not found.").build();
        }
    }

    @GET
    @Path("/{bookingId}/user")
    @Timed(name = "get_booking_user")
    public Response getBookingUser(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingUser.");
        try {
            User user = bookingsBean.getBookingUser(bookingId);
            if (user != null) {
                logger.info("User with id" + user.getUuid() + " successfully retrieved ...");
                return Response.ok(user).build();
            } else
                return Response.status(Response.Status.NOT_FOUND).entity("User of requested booking not found.").build();
        }
        catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User of requested booking not found.").build();
        }
    }

    @POST
    public Response addBooking(Booking b){
        logger.info("REST CALL: addBooking.");
        // for etcd testing
        if(!configProperties.isInsertEnabled()){
            logger.error("Booking insertion is disabled.");
            return Response.status(Response.Status.FORBIDDEN).entity("Booking insertion is disabled.").build();
        }

        boolean res = bookingsBean.addBooking(b);
        if(res)
            return Response.ok(b).build();
        else
            return Response.status(Response.Status.CONFLICT).entity("Inserting booking in database failed.").build();
    }

    @DELETE
    @Path("/{bookingId}")
    public Response deleteBooking(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: deleteBooking.");

        boolean res = bookingsBean.deleteBooking(bookingId);
        if (res)
            return Response.ok("Booking successfully deleted.").build();
        else
            return Response.status(Response.Status.CONFLICT).entity("Deleting booking from database failed.").build();
    }
}
