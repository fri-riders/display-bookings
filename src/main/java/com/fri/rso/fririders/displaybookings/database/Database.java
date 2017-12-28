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
        bookings.add(new Booking(1,1,"7f35f032-bc71-4c58-bcca-e0e44dbfa7d9",new Date(), new Date()));
        bookings.add(new Booking(2,2,"7f35f032-bc71-4c58-bcca-e0e44dbfa7d9",new Date(), new Date()));
        bookings.add(new Booking(3,3,"5c956e57-a3a5-4034-a38f-581609f7722c",new Date(), new Date()));
        bookings.add(new Booking(1,1,"5c956e57-a3a5-4034-a38f-581609f7722c",new Date(), new Date()));
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
