package service;

import dao.CarDAO;
import model.Car;

import java.util.List;
import java.util.stream.Collectors;

public class CarFilterManager {
    private final CarDAO carDAO;

    public CarFilterManager(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    public List<Car> filterByBrand(String brand) {
        return carDAO.getAllCars().stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    public List<Car> filterByColor(String color) {
        return carDAO.getAllCars().stream()
                .filter(car -> car.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

    public List<Car> filterAvailableCars() {
        return carDAO.getAllCars().stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }
}
+