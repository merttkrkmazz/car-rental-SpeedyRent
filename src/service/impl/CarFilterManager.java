package service.impl;

import dao.CarDAO;
import model.Car;
import service.interfaces.CarBrowsing;

import java.util.List;

public class CarFilterManager implements CarBrowsing {
    private CarDAO carDAO;

    public CarFilterManager() {
        this.carDAO = new CarDAO();
    }

    @Override
    public List<Car> getAllCars() {
        return carDAO.getAllCars();
    }
}
