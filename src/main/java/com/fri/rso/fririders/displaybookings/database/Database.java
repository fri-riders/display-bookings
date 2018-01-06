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
        bookings.add(new Booking(1,1,"1bd484ed-b5ab-4f88-b57e-3c167d53ecb2" ,new Date(), new Date()));
        bookings.add(new Booking(2,2,"1b9aec27-b80c-4baa-8467-2d0a89c5640c" ,new Date(), new Date()));
        bookings.add(new Booking(3,3,"88eaeccc-7b77-439f-bba8-2525d2daa7f7" ,new Date(), new Date()));
        bookings.add(new Booking(4,1,"1bd484ed-b5ab-4f88-b57e-3c167d53ecb2" ,new Date(), new Date()));
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
