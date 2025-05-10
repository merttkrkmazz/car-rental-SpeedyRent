package model;

/**
 * Represents a car available for rent in the system.
 */
public class Car {

    /** Unique identifier for the car. */
    private int id;

    /** Brand of the car (e.g., Toyota, BMW). */
    private String brand;

    /** Model of the car (e.g., Corolla, 3 Series). */
    private String model;

    /** Color of the car. */
    private String color;

    /** Type of fuel the car uses (e.g., petrol, diesel, electric). */
    private String fuelType;

    /** Transmission type (e.g., manual, automatic). */
    private String transmission;

    /** Number of seats in the car. */
    private int seatingCapacity;

    /** Rental price per day for the car. */
    private double rentalPrice;

    /** Current status of the car (e.g., "available", "reserved"). */
    private String status;

    /**
     * Constructs a new Car instance with the given attributes.
     *
     * @param id              the unique ID of the car
     * @param brand           the brand name of the car
     * @param model           the model name of the car
     * @param color           the color of the car
     * @param fuelType        the type of fuel the car uses
     * @param transmission    the transmission type of the car
     * @param seatingCapacity the number of seats in the car
     * @param rentalPrice     the rental price per day
     * @param status          the current status of the car
     */
    public Car(int id, String brand, String model, String color, String fuelType, String transmission,
               int seatingCapacity, double rentalPrice, String status) {
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

    /** @return the unique ID of the car */
    public int getId() { return id; }

    /** @return the brand of the car */
    public String getBrand() { return brand; }

    /** @return the model of the car */
    public String getModel() { return model; }

    /** @return the color of the car */
    public String getColor() { return color; }

    /** @return the fuel type of the car */
    public String getFuelType() { return fuelType; }

    /** @return the transmission type of the car */
    public String getTransmission() { return transmission; }

    /** @return the number of seats in the car */
    public int getSeatingCapacity() { return seatingCapacity; }

    /** @return the rental price per day for the car */
    public double getRentalPrice() { return rentalPrice; }

    /** @return the current status of the car */
    public String getStatus() { return status; }
}
