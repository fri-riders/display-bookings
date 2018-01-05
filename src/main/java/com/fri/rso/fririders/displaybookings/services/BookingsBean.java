package com.fri.rso.fririders.displaybookings.services;

import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.database.Database;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.entities.User;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestScoped
public class BookingsBean {

    private Logger logger = LogManager.getLogger(BookingsBean.class.getName());

    private static Client client = ClientBuilder.newClient();

    @Inject
    private ConfigProperties configProperties;

    @Inject
    @DiscoverService(value="users", version = "1.0.x", environment = "dev")
    private Optional<String> usersUrl;


    public List<Booking> getAllBookings() {
        List<Booking> bookings = Database.getBookings();
        if(bookings != null && bookings.size() > 0) {
            logger.info("Found "+ bookings.size() + " bookings.");
            return bookings;
        }
        else {
            logger.error("Zero bookings found");
            return new ArrayList<>();
        }
    }


    public Booking getBooking(int bookingId) {
        Booking booking = Database.getBooking(bookingId);
        return booking;
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getBookingsAccommodationFallback")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    public Accommodation getBookingAccommodation(int bookingId) {
        Booking booking = Database.getBooking(bookingId);
        if(booking != null) {
            try {
                logger.info("Calling accommodations service.");
                String url = "http://accommodations:8081/v1/accommodations";
                logger.info("URL: " + url);

                //find info about accommodation
                List<Accommodation> accommodations =
                        client.target(url)
                                .request(MediaType.APPLICATION_JSON)
                                .get((new GenericType<List<Accommodation>>() {
                                }));
                for (Accommodation a : accommodations)
                    if (a.getId() == booking.getId())
                        return a;
                return null;
            }
            catch (WebApplicationException | ProcessingException e) {
                logger.error(e);
                throw new InternalServerErrorException(e);
            }
        }
        else
            return null;
    }

    public Object getBookingsAccommodationFallback(int bookingId) {
        logger.warn("getBookingsAccommodationFallback called.");
        return null;
    }


    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getBookingUserFallback")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    public User getBookingUser(int bookingId) {
        if (!this.usersUrl.isPresent()){
            Booking booking = Database.getBooking(bookingId);
            if(booking != null) {
                try {
                    String userId = booking.getIdUser();
                    logger.info("Calling user service ...");
                    String url = this.usersUrl.get() + "/v1/users/" + userId;
                    logger.info("URL: " + url);

                    //find info about user
                    User user =
                            client.target(url)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get((new GenericType<User>() {
                                    }));
                    if (user != null) {
                        logger.info("User with id" + userId + " successfully retrieved ...");
                        return user;
                    }
                    return null;
                }
                catch (WebApplicationException | ProcessingException e) {
                    logger.error(e);
                    throw new InternalServerErrorException(e);
                }
            }
            else
                return null;
        }
        else{
            logger.error("Users service not reachable.");
            return null;
        }
    }

    public Object getBookingUserFallback(int bookingId) {
        logger.warn("getBookingUserFallback called.");
        return null;
    }

    public boolean addBooking(Booking b){
        // for etcd testing
        if(!configProperties.isInsertEnabled()){
            logger.error("Booking insertion is disabled.");
            return false;
        }

        try {
            Database.addBooking(b);
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean deleteBooking(int bookingId) {
        try {
            Database.deleteBooking(bookingId);
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            return false;
        }
    }

}
