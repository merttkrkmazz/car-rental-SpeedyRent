package controller;

import model.Car;
import util.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarController {

    public static boolean addCar(String model, double dailyRent, double deposit, int mileage, String status, int specificationId) {
        Connection conn = null;
        PreparedStatement psCar = null;
        PreparedStatement psHas = null;
        ResultSet rs = null;
        try {
            conn = Srent_DB.getConnection();
            conn.setAutoCommit(false);

            String insertCar = "INSERT INTO Car (model, daily_rent, deposit, mileage, vehicle_status) VALUES (?, ?, ?, ?, ?)";
            psCar = conn.prepareStatement(insertCar, Statement.RETURN_GENERATED_KEYS);
            psCar.setString(1, model);
            psCar.setDouble(2, dailyRent);
            psCar.setDouble(3, deposit);
            psCar.setInt(4, mileage);
            psCar.setString(5, status);
            psCar.executeUpdate();

            rs = psCar.getGeneratedKeys();
            if (rs.next()) {
                int carId = rs.getInt(1);
                String insertHas = "INSERT INTO has (car_id, specification_id) VALUES (?, ?)";
                psHas = conn.prepareStatement(insertHas);
                psHas.setInt(1, carId);
                psHas.setInt(2, specificationId);
                psHas.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ignored) {}
            try { if (psHas != null) psHas.close(); } catch (Exception ignored) {}
            try { if (psCar != null) psCar.close(); } catch (Exception ignored) {}
            try { if (conn != null) conn.close(); } catch (Exception ignored) {}
        }
    }

    public static boolean updateCar(int carId, String model, double dailyRent, String status) {
        String sql = "UPDATE Car SET model = ?, daily_rent = ?, vehicle_status = ? WHERE car_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, model);
            ps.setDouble(2, dailyRent);
            ps.setString(3, status);
            ps.setInt(4, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean assignSpecificationToCar(int carId, int specId) {
        String sql = "INSERT INTO has (car_id, specification_id) VALUES (?, ?)";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setInt(2, specId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean assignCarToAdmin(int adminId, int carId) {
        String sql = "INSERT INTO manages (user_id, car_id) VALUES (?, ?)";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setInt(2, carId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteCar(int carId) {
        String deleteHasSql      = "DELETE FROM has WHERE car_id = ?";
        String deleteReservesSql = "DELETE FROM reserves WHERE car_id = ?";
        String deleteManagesSql  = "DELETE FROM manages WHERE car_id = ?";
        String deleteCarSql      = "DELETE FROM Car WHERE car_id = ?";

        try (Connection conn = Srent_DB.getConnection()) {
            conn.setAutoCommit(false);

            // 1) Remove specs links
            try (PreparedStatement ps = conn.prepareStatement(deleteHasSql)) {
                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            // 2) Remove any reservations
            try (PreparedStatement ps = conn.prepareStatement(deleteReservesSql)) {
                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            // 3) Remove any admin‐car link (if you have one)
            try (PreparedStatement ps = conn.prepareStatement(deleteManagesSql)) {
                ps.setInt(1, carId);
                ps.executeUpdate();
            }

            // 4) Finally delete the car itself
            int affected;
            try (PreparedStatement ps = conn.prepareStatement(deleteCarSql)) {
                ps.setInt(1, carId);
                affected = ps.executeUpdate();
            }

            conn.commit();
            return affected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getSpecificationIdForCar(int carId) {
        String sql = "SELECT specification_id FROM has WHERE car_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("specification_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }


    public static String getCarById(int carId) {
        String sql = "SELECT c.model, c.daily_rent, c.deposit, c.mileage, c.vehicle_status, vs.fuel_type, vs.transmission_type, vs.seating_capacity " +
                "FROM Car c " +
                "JOIN has h ON c.car_id = h.car_id " +
                "JOIN VehicleSpecification vs ON h.specification_id = vs.specification_id " +
                "WHERE c.car_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("Model: %s | Rent: %.2f | Deposit: %.2f | Mileage: %d | Status: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getString("model"),
                        rs.getDouble("daily_rent"),
                        rs.getDouble("deposit"),
                        rs.getInt("mileage"),
                        rs.getString("vehicle_status"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Car not found.";
    }

    public static boolean isCarAvailable(int carId) {
        String sql = "SELECT vehicle_status FROM Car WHERE car_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return "available".equals(rs.getString("vehicle_status"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> getAllReservedCars() {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE vehicle_status != 'available'";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Status: %s | Daily Rent: %.2f | Mileage: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("vehicle_status"),
                        rs.getDouble("daily_rent"),
                        rs.getInt("mileage"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<String> getAllCars() {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Status: %s | Daily Rent: %.2f | Mileage: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("vehicle_status"),
                        rs.getDouble("daily_rent"),
                        rs.getInt("mileage"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }
    public static List<Car> getAllCarsAsObjects() {
        List<Car> cars = new ArrayList<>();
        String sql =
                "SELECT c.car_id, c.model, c.daily_rent, c.vehicle_status, " +
                        "       vs.fuel_type, vs.transmission_type, vs.seating_capacity " +
                        "  FROM Car c " +
                        "  JOIN has h ON c.car_id = h.car_id " +
                        "  JOIN VehicleSpecification vs ON h.specification_id = vs.specification_id";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("car_id"),
                        "Unknown",                       // placeholder brand
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"),
                        rs.getDouble("daily_rent"),
                        rs.getString("vehicle_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<String> getAllAvailableCars() {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE vehicle_status = 'available'";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Daily Rent: %.2f | Mileage: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getDouble("daily_rent"),
                        rs.getInt("mileage"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<Car> getAvailableCarsAsObjects() {
        List<Car> cars = new ArrayList<>();
        String sql =
                "SELECT c.car_id, c.model, c.daily_rent, c.vehicle_status, " +
                        "       vs.fuel_type, vs.transmission_type, vs.seating_capacity " +
                        "  FROM Car c " +
                        "  JOIN has h ON c.car_id = h.car_id " +
                        "  JOIN VehicleSpecification vs ON h.specification_id = vs.specification_id " +
                        " WHERE c.vehicle_status = 'available'";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cars.add(new Car(
                        rs.getInt("car_id"),
                        "Unknown",                       // placeholder for brand
                        rs.getString("model"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"),
                        rs.getDouble("daily_rent"),
                        rs.getString("vehicle_status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }


    public static List<String> getCarsByStatus(String status) {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE vehicle_status = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Daily Rent: %.2f | Mileage: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getDouble("daily_rent"),
                        rs.getInt("mileage"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<String> getCarsManagedByAdmin(int adminId) {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.model, c.vehicle_status FROM Car c " +
                "JOIN manages m ON c.car_id = m.car_id WHERE m.user_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String entry = String.format("Car ID %d: %s - %s",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getString("vehicle_status"));
                cars.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<String> getCarSpecifications(int carId) {
        List<String> specs = new ArrayList<>();
        String sql = "SELECT vs.color, vs.fuel_type, vs.transmission_type, vs.seating_capacity " +
                "FROM has h JOIN VehicleSpecification vs ON h.specification_id = vs.specification_id " +
                "WHERE h.car_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String spec = String.format("Color: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
                specs.add(spec);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specs;
    }

    public static List<String> filterCars(Double minRent, Double maxRent, Integer minMileage, Integer maxMileage,
                                          String fuelType, String transmissionType, Integer minSeats, Integer maxSeats) {
        List<String> cars = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT c.car_id, c.model, c.daily_rent, c.mileage, vs.fuel_type, vs.transmission_type, vs.seating_capacity " +
                "FROM Car c " +
                "JOIN has h ON c.car_id = h.car_id " +
                "JOIN VehicleSpecification vs ON h.specification_id = vs.specification_id WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (minRent != null) {
            sql.append(" AND c.daily_rent >= ?");
            parameters.add(minRent);
        }
        if (maxRent != null) {
            sql.append(" AND c.daily_rent <= ?");
            parameters.add(maxRent);
        }
        if (minMileage != null) {
            sql.append(" AND c.mileage >= ?");
            parameters.add(minMileage);
        }
        if (maxMileage != null) {
            sql.append(" AND c.mileage <= ?");
            parameters.add(maxMileage);
        }
        if (fuelType != null) {
            sql.append(" AND vs.fuel_type = ?");
            parameters.add(fuelType);
        }
        if (transmissionType != null) {
            sql.append(" AND vs.transmission_type = ?");
            parameters.add(transmissionType);
        }
        if (minSeats != null) {
            sql.append(" AND vs.seating_capacity >= ?");
            parameters.add(minSeats);
        }
        if (maxSeats != null) {
            sql.append(" AND vs.seating_capacity <= ?");
            parameters.add(maxSeats);
        }

        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                ps.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Daily Rent: %.2f | Mileage: %d | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getDouble("daily_rent"),
                        rs.getInt("mileage"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    public static List<String> getCarHistory(int carId) {
        List<String> history = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.start_date, b.end_date, b.booking_status, b.amount " +
                "FROM Booking b " +
                "JOIN reserves r ON b.booking_id = r.booking_id " +
                "WHERE r.car_id = ? ORDER BY b.start_date DESC";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, carId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String entry = String.format("Booking #%d | %s to %s | Status: %s | Amount: %.2f",
                        rs.getInt("booking_id"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("booking_status"),
                        rs.getDouble("amount"));
                history.add(entry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    public static List<String> getTopRentedCars(int limit) {
        List<String> cars = new ArrayList<>();
        String sql = "SELECT c.car_id, c.model, COUNT(*) AS rental_count " +
                "FROM reserves r JOIN Car c ON r.car_id = c.car_id " +
                "GROUP BY c.car_id, c.model ORDER BY rental_count DESC LIMIT ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String car = String.format("Car ID %d: %s | Rentals: %d",
                        rs.getInt("car_id"),
                        rs.getString("model"),
                        rs.getInt("rental_count"));
                cars.add(car);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }



}
