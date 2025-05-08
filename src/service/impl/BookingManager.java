package service.impl;

import dao.BookingDAO;
import model.Booking;

public class BookingManager {
    private final BookingDAO bookingDAO;

    public BookingManager(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    public boolean bookCar(int userId, int carId, String startDate, String endDate) {
        Booking booking = new Booking(userId, carId, startDate, endDate);
        return bookingDAO.saveBooking(booking);
    }

    public boolean cancelBooking(int bookingId) {
        return bookingDAO.deleteBooking(bookingId);
    }
}
