package model;

/**
 * Car model class.
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
    private String status; // "available", "reserved", etc.

    public Car(int id, String brand, String model, String color, String fuelType, String transmission, int seatingCapacity, double rentalPrice, String status) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.color = color;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.rentalPrice = rentalPrice;
        this.status = status;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getColor() { return color; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public double getRentalPrice() { return rentalPrice; }
    public String getStatus() { return status; }
}
