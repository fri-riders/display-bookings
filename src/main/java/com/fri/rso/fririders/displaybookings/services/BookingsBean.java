package com.fri.rso.fririders.displaybookings.services;

import com.fri.rso.fririders.displaybookings.entities.Booking;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class BookingsBean {

    private Logger logger = Logger.getLogger(BookingsBean.class.getName());

    @PersistenceContext
    private EntityManager em;

    public List<Booking> getAllBookings() throws Exception{
        logger.info("BookingsBean <getAllBookings>: retrieving all bookings ... ");
        Query q = em.createNamedQuery("Booking.findAll");
        List<Booking> bookings = q.getResultList();
        if (bookings == null || bookings.size() == 0)
            throw new Exception("Error: Bookings not found.");
        else{
            logger.info("BookingsBean <getAllBookings>: bookings successfully retireved.");
            return bookings;
        }

    }

    public Booking getBooking(int id) throws Exception{
        logger.info("BookingsBean <getBooking>: retrieving booking with id " + id + "... ");
        Booking b = em.find(Booking.class, id);
        if (b != null)
            return b;
        else
            throw new Exception("Error: Booking with id " + id + " not found.");
    }

    public void create(Booking b) throws Exception{
        logger.info("BookingsBean <create>: inserting booking in the database ... ");
        if(b != null){
            em.persist(b);
            logger.info("BookingsBean <create>: booking successfully inserted ... ");
        }
        else throw new Exception("Error: Booking inserting failed.");
    }

    public void update(Booking b) throws Exception{
        logger.info("BookingsBean <update>: inserting booking in the database ... ");
        if(b != null){
            em.merge(b);
            logger.info("BookingsBean <update>: booking successfully updated ... ");
        }
        else throw new Exception("Error: Updating booking failed.");
    }

    public void delete(int id) throws Exception{
        logger.info("BookingsBean <delete>: deleting booking with id " + id + "... ");
        Booking b = getBooking(id);
        if(b != null){
            em.remove(b);
            logger.info("BookingsBean <delete>: booking successfully deleted ... ");
        }
        else throw new Exception("Error: Booking with id " + id + " not found.");
    }

}
