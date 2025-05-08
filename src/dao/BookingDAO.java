package dao;

import model.Booking;

public interface BookingDAO {
    boolean saveBooking(Booking booking);
    boolean deleteBooking(int bookingId);
}
