package com.fri.rso.fririders.displaybookings.database;

import com.fri.rso.fririders.displaybookings.entities.Booking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Database {

    private static List<Booking> bookings = new ArrayList<>();

    public static List<Booking> getBookings() {
        return bookings;
    }

    // fake bookings
    static {
        bookings.add(new Booking(1,1,"09b18602-9e3f-4aa1-8ec3-4c29e927d875",new Date(), new Date()));
        bookings.add(new Booking(2,2,"97c758f4-39fc-4fe1-a3e4-01c6e5e95d92",new Date(), new Date()));
        bookings.add(new Booking(3,3,"97c758f4-39fc-4fe1-a3e4-01c6e5e95d92",new Date(), new Date()));
        bookings.add(new Booking(1,1,"09b18602-9e3f-4aa1-8ec3-4c29e927d875",new Date(), new Date()));
    }

    public static Booking getBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getId() == bookingId)
                return booking;
        }
        return null;
    }

    public static void addBooking(Booking booking) throws Exception{
        try {
            bookings.add(booking);
        }catch (Exception e){
            throw new Exception("Error: inserting booking in database failed.");
        }
    }

    public static void deleteBooking(int bookingId) throws Exception{
        for (Booking b : bookings){
            if(b.getId() == bookingId) {
                bookings.remove(b);
                return;
            }
        }
    }

}
