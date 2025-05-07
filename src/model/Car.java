package model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String fuelType;
    private String transmission;
    private int seatingCapacity;
    private double rentalPrice;
    private String availability;


    public Car(int id, String brand, String model, String fuelType, String transmission, int seatingCapacity, double rentalPrice, String availability) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.rentalPrice = rentalPrice;
        this.availability = availability;

    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public double getRentalPrice() { return rentalPrice; }
    public String getAvailability() { return availability;}
}