package model;

/**
 * Araç model sınıfı.
 */
public class Car {
    private int id;
    private String brand;
    private String model;
    private String color;
    private String fuelType;
    private String transmission;
    private int seatingCapacity;
    private double rentalPrice;
    private boolean availability;

    /**
     * Car constructor.
     */
    public Car(int id, String brand, String model, String color, String fuelType, String transmission, int seatingCapacity, double rentalPrice, boolean availability) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.rentalPrice = rentalPrice;
        this.availability = availability;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public double getRentalPrice() { return rentalPrice; }
    public boolean isAvailable() { return availability; }
}
