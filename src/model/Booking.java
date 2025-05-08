package model;

public class Booking {
    // alanlar ve yapıcı
    private final int userId;
    private final int carId;
    private final String startDate;
    private final String endDate;

    public Booking(int userId, int carId, String startDate, String endDate) {
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    // getter’lar…
}
