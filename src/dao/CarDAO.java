package dao;

import model.Car;
import java.sql.*;
import java.util.*;

public class CarDAO {
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/speedyrent";
        String user = "root";
        String password = ""; // Kendi şifreni yaz

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars")) {

            while (rs.next()) {
                Car car = new Car(
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission"),
                        rs.getInt("seating_capacity"),
                        rs.getDouble("rental_price")
                );
                cars.add(car);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cars;
    }
}
