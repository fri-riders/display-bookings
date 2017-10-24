package com.fri.rso.fririders.displaybookings.database;

import java.util.ArrayList;
import java.util.List;

public class Database {

    private static List<Booking> bookings = new ArrayList<>();

    public static List<Booking> getBookings() {
        return bookings;
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
