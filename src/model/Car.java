package model;

public class Car {
    private int id;
    private String brand;
    private String model;
    private String fuelType;
    private String transmission;
    private int seatingCapacity;
    private double rentalPrice;

    public Car(int id, String brand, String model, String fuelType, String transmission, int seatingCapacity, double rentalPrice) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.seatingCapacity = seatingCapacity;
        this.rentalPrice = rentalPrice;
    }

    public int getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public String getFuelType() { return fuelType; }
    public String getTransmission() { return transmission; }
    public int getSeatingCapacity() { return seatingCapacity; }
    public double getRentalPrice() { return rentalPrice; }
}
