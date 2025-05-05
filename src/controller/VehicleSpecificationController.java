package controller;

import util.Srent_DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleSpecificationController {

    public static boolean addSpecification(String color, String fuelType, String transmissionType, int seatingCapacity) {
        String sql = "INSERT INTO VehicleSpecification (color, fuel_type, transmission_type, seating_capacity) VALUES (?, ?, ?, ?)";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, color);
            ps.setString(2, fuelType);
            ps.setString(3, transmissionType);
            ps.setInt(4, seatingCapacity);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateSpecification(int specId, String color, String fuelType, String transmissionType, int seatingCapacity) {
        String sql = "UPDATE VehicleSpecification SET color = ?, fuel_type = ?, transmission_type = ?, seating_capacity = ? WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, color);
            ps.setString(2, fuelType);
            ps.setString(3, transmissionType);
            ps.setInt(4, seatingCapacity);
            ps.setInt(5, specId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteSpecification(int specId) {
        String sql = "DELETE FROM VehicleSpecification WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, specId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getAllSpecifications() {
        List<String> specs = new ArrayList<>();
        String sql = "SELECT * FROM VehicleSpecification";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String spec = String.format("Spec ID %d: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getInt("specification_id"),
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

    public static String getSpecificationById(int specId) {
        String sql = "SELECT * FROM VehicleSpecification WHERE specification_id = ?";
        try (Connection conn = Srent_DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, specId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return String.format("Color: %s | Fuel: %s | Transmission: %s | Seats: %d",
                        rs.getString("color"),
                        rs.getString("fuel_type"),
                        rs.getString("transmission_type"),
                        rs.getInt("seating_capacity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Specification not found.";
    }
}
