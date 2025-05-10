package model;

/**
 * Represents a booking made by a user for a specific car within a date range.
 */
public class Booking {
    /** The ID of the user who made the booking. */
    private final int userId;

    /** The ID of the car that is booked. */
    private final int carId;

    /** The start date of the booking (in format YYYY-MM-DD). */
    private final String startDate;

    /** The end date of the booking (in format YYYY-MM-DD). */
    private final String endDate;

    /**
     * Constructs a new Booking instance.
     *
     * @param userId    the ID of the user making the booking
     * @param carId     the ID of the car being booked
     * @param startDate the start date of the booking
     * @param endDate   the end date of the booking
     */
    public Booking(int userId, int carId, String startDate, String endDate) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getter methods would be documented here if provided
}
