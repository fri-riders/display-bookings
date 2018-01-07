package com.fri.rso.fririders.displaybookings.services;

import com.fri.rso.fririders.displaybookings.config.ConfigProperties;
import com.fri.rso.fririders.displaybookings.database.Database;
import com.fri.rso.fririders.displaybookings.entities.Accommodation;
import com.fri.rso.fririders.displaybookings.entities.Booking;
import com.fri.rso.fririders.displaybookings.entities.User;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;
import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
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
@Log
public class BookingsBean {

    private Logger logger = LogManager.getLogger(BookingsBean.class.getName());

    private static Client client = ClientBuilder.newClient();

    @Inject
    private ConfigProperties configProperties;

    @Inject AuthBean authBean;

    @Inject
    @DiscoverService(value="users", version = "*", environment = "dev", accessType = AccessType.DIRECT)
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

    public List<Booking> getUserBookings(String userId){
        List<Booking> bookings = new ArrayList<>();
        for(Booking b : Database.getBookings()){
            if(b.getIdUser().equals(userId))
                bookings.add(b);
        }
        return bookings;
    }

    public List<Booking> getAccommodationBookings(long aId){
        List<Booking> bookings = new ArrayList<>();
        for(Booking b : Database.getBookings()){
            if(b.getIdAccommodation() == aId)
                bookings.add(b);
        }
        return bookings;
    }

    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getBookingsAccommodationFallback")
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
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

    public Accommodation getBookingsAccommodationFallback(int bookingId) {
        logger.warn("getBookingsAccommodationFallback called.");
        Accommodation a = new Accommodation(1, "N/A", "N/A", "N/A", 0, 0.0);
        return a;
    }


    @CircuitBreaker(requestVolumeThreshold = 2)
    @Fallback(fallbackMethod = "getBookingUserFallback")
    @CommandKey("http-booking-user")
    @Timeout(value = 1, unit = ChronoUnit.SECONDS)
    public User getBookingUser(int bookingId) {
        if (this.usersUrl.isPresent()){
            Booking booking = Database.getBooking(bookingId);
            if(booking != null) {
                try {
                    String userId = booking.getIdUser();
                    logger.info("Calling user service ...");
                    String url = this.usersUrl.get() + "/v1/users";
                    logger.info("URL: " + url);

                    String token = authBean.getAuthToken();

                    //find info about user
                    List<User> users =
                            client.target(url)
                                    .property("authToken", token)
                                    .request(MediaType.APPLICATION_JSON)
                                    .get((new GenericType<List<User>>() {
                                    }));
                    if (users != null || users.size() != 0) {
                        logger.info("List of users successfully retrieved ...");
                        for(User u : users){
                            if (u.getUuid().equals(userId))
                                return u;
                        }
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

    public User getBookingUserFallback(int bookingId) {
        logger.warn("getBookingUserFallback called.");
        User u = new User();
        u.setFirstName("N/A");
        u.setLastName("N/A");
        return u;
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
