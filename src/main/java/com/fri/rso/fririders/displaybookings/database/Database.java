package com.fri.rso.fririders.displaybookings.database;

import com.fri.rso.fririders.displaybookings.entities.Booking;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        bookings.add(new Booking(1, 1, "88eaeccc-7b77-439f-bba8-2525d2daa7f7", new Date(), new Date()));
        bookings.add(new Booking(5, 1, "533620ef-20d2-4e8a-9099-2ddc77689dc9", parseDate("20.12.2017 09:00"), parseDate("23.12.2017 09:00")));
        bookings.add(new Booking(6, 1, "eb800255-8a15-4013-9a60-a9cf7afbd6ee", parseDate("23.12.2017 12:00"), parseDate("24.12.2017 09:00")));
        bookings.add(new Booking(7, 1, "ae3d6fe3-2b12-4f7b-82b6-20956bb74304", parseDate("25.12.2017 09:00"), parseDate("26.12.2017 12:00")));
        bookings.add(new Booking(8, 1, "88eaeccc-7b77-439f-bba8-2525d2daa7f7", parseDate("27.12.2017 09:00"), parseDate("30.12.2017 09:00")));
        bookings.add(new Booking(9, 1, "1bd484ed-b5ab-4f88-b57e-3c167d53ecb2", parseDate("30.12.2017 10:00"), parseDate("01.01.2018 09:00")));
        bookings.add(new Booking(10, 1, "709d7376-cf18-4003-8174-bced70c51d38", parseDate("03.01.2018 10:00"), parseDate("04.01.2018 09:00")));
        bookings.add(new Booking(2, 2, "1b9aec27-b80c-4baa-8467-2d0a89c5640c", new Date(), new Date()));
        bookings.add(new Booking(3, 3, "88eaeccc-7b77-439f-bba8-2525d2daa7f7", new Date(), new Date()));
        bookings.add(new Booking(4, 1, "1bd484ed-b5ab-4f88-b57e-3c167d53ecb2", new Date(), new Date()));
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

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
