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
        bookings.add(new Booking(1,1,"8f6ecebf-0387-4326-8b0d-c5025f4da8ee",new Date(), new Date()));
        bookings.add(new Booking(2,2,"8f6ecebf-0387-4326-8b0d-c5025f4da8ee",new Date(), new Date()));
        bookings.add(new Booking(3,3,"008ecd25-2c02-4b69-aac3-9a4baa43d359",new Date(), new Date()));
        bookings.add(new Booking(1,1,"008ecd25-2c02-4b69-aac3-9a4baa43d359",new Date(), new Date()));
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
