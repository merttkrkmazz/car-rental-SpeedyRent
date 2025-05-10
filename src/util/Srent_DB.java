package util;

import java.sql.*;
import java.util.*;

public class Srent_DB {
    private static String userType;
    private static Connection conn;
    private static PreparedStatement ps;
    private static Scanner scanner;
    private static String HOST_NAME = "127.0.0.1";
    private static final String PORT = "3306";               // mysql use this port(default)
    private static final String USER_NAME = "root";          // admin name: root
    private static final String PASSWORD = "Wthrw_<>1215@";     // admin pwd
    private static final String DB_NAME = "srent";

    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME; // need to specify DB_NAME
            Connection connection = DriverManager.getConnection(url, USER_NAME, PASSWORD);
            return connection;

        } catch(SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getDBName() {
        return DB_NAME;
    }

    /* checks if the user is exist or not */
    public static boolean authenticateUser(int userID, String password) {
        try {
            boolean isFound;
            conn = getConnection();
            String sql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            conn.close();

            isFound = rs.next();
            System.out.println(isFound ? "User is found" : "User is not found");
            return isFound;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occured! \n");
            return false;
        }
    }

    /* reserves car for that user */
    public static boolean reserveCar(int userID, int carID) {
        try {
            conn = getConnection();
            String sql = "INSERT INTO reserves VALUES(?,?)";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, carID);
            ResultSet rs = ps.executeQuery();
            conn.close();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occured! \n");
            return false;
        }
    }

    public static boolean cancelReservation(int bookingID) {
        try {
            conn = getConnection();
            String sql = "DELETE FROM reservations WHERE booking_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            conn.close();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error occured! \n");
            return false;
        }
    }
}
