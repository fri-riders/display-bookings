package com.fri.rso.fririders.displaybookings.resources;


import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.database.Database;
import com.fri.rso.fririders.displaybookings.entities.User;
import com.fri.rso.fririders.displaybookings.services.BookingsBean;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.cdi.Log;
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
import java.util.logging.Logger;


@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("bookings")
@Log
@Metered
public class BookingResource {

    private static final Logger logger = Logger.getLogger( BookingResource.class.getName() );

    private static Client client = ClientBuilder.newClient();

    @Inject
    private ConfigProperties configProperties;

    @Inject
    private BookingsBean bookingsBean;


    @Inject
    @DiscoverService(value = "rso-accommodations", version = "1.0.x", environment = "dev")
    private Optional<String> accommodationsUrl;

    @Inject
    @DiscoverService(value="rso-users", version = "1.0.x", environment = "dev")
    private Optional<String> usersUrl;


    @GET
    @Metered
    public Response getAllBookings() {
        logger.info("REST CALL: getAllBookings");
        //List<Booking> bookings = Database.getBookings();
        try {
            List<Booking> bookings = bookingsBean.getAllBookings();
            logger.info("Found "+ bookings.size() + " bookings.");
            return Response.ok(bookings).build();
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{bookingId}")
    public Response getBooking(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBooking.");
        //Booking booking = Database.getBooking(bookingId);
        try {
            Booking booking = bookingsBean.getBooking(bookingId);
            return Response.ok(booking).build();
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{bookingId}/accommodation")
    public Response getBookingAccommodation(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingAccommodation.");
        if (!this.accommodationsUrl.isPresent())
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodations service cannot be reached.").build();

        try {
            Booking booking = bookingsBean.getBooking(bookingId);
            //find info about accommodation
            List<Accommodation> accommodations =
                    client.target(this.accommodationsUrl.get() + "/accommodations/all")
                            .request(MediaType.APPLICATION_JSON)
                            .get((new GenericType<List<Accommodation>>() {}));
            for(Accommodation a : accommodations)
                if(a.getId() == booking.getId())
                    return Response.ok(a).build();
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodation for requested booking not found.").build();
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{bookingId}/owner")
    public Response getBookingOwner(@PathParam("bookingId") int bookingId) {
        logger.info("REST CALL: getBookingOwner.");
        if (!this.usersUrl.isPresent())
            return Response.status(Response.Status.NOT_FOUND).entity("Users service cannot be reached.").build();

        try {
            Booking booking = bookingsBean.getBooking(bookingId);
            int userId = booking.getIdUser();
            User user =
                    client.target(this.usersUrl.get() + "/" + userId)
                            .request(MediaType.APPLICATION_JSON)
                            .get((new GenericType<User>() {}));
            if (user != null)
                return Response.ok(user).build();
            return Response.status(Response.Status.NOT_FOUND).entity("Accommodation for requested booking not found.").build();
        }
        catch (Exception e) {
            logger.severe(e.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
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
            //Database.addBooking(b);
            bookingsBean.create(b);
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
            //Database.deleteBooking(bookingId);
            bookingsBean.delete(bookingId);
            return Response.ok("Booking successfully deleted.").build();
        }
        catch (Exception e){
            logger.warning(e.getMessage());
            return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
        }
    }
}
