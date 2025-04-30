import java.sql.*;
import java.util.Date;
import java.util.Objects;
import java.util.*;
import java.time.LocalDate;

public class Srent_DB {
    private static String userType;
    private static Connection conn;
    private static PreparedStatement ps;
    private static Scanner scanner;
    private static String HOST_NAME = "127.0.0.1";
    private static final String PORT = "3306";               // mysql use this port(default)
    private static final String USER_NAME = "root";          // admin name: root
    private static final String PASSWORD = "#123321#%&";     // admin pwd
    private static final String DB_NAME = "srent";

    public static Connection getConnection() {

        try{
            String url = "jdbc:mysql://" + HOST_NAME + ":" + PORT + "/" + DB_NAME; // need to specify DB_NAME
            Connection connection = DriverManager.getConnection(url, USER_NAME, PASSWORD);

            return connection;

        } catch(SQLException e){
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
            String sql;

            sql = "SELECT * FROM user WHERE user_id = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, userID);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            conn.close();

            isFound = rs.next();
            System.out.println(isFound ? "User is found" : "User is not found");

            return isFound;

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n");
        }

    }

    /* reserves car for that user with the car user selected and if the enaough amount
    *  if he/she MADE booking, so reserve that booked ones, available olmalı, amount üstü olmalı,
    *  booking (confirmed) olmalı
    */
    public static boolean reserveCar(int userID, int carID){
        try {
            conn = getConnection();
            String sql;

            sql = "INSERT INTO reserves VALUES(?,?) ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ps.setInt(2, carID);
            ResultSet rs = ps.executeQuery();
            conn.close();

            return rs.next();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n");
        }
    }
    public static boolean cancelReservation(int bookingID){
        try {
            conn = getConnection();
            String sql;

            sql = "DELETE FROM reservations WHERE booking_id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookingID);
            ResultSet rs = ps.executeQuery();
            conn.close();

            return rs.next();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n");
        }
    }

    public static boolean addCar(Object carInfo){
        /******** what is car info **********/
    }

    public static boolean updateCar(int carID, Object newInfo){
        /******** what is newinfo **********/
    }
    public static boolean deleteCar(int carID){
        try {
            conn = getConnection();
            String sql;

            sql = "DELETE FROM car WHERE car_id = ? ";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, carID);
            ResultSet rs = ps.executeQuery();
            conn.close();

            return rs.next();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n");
        }
    }

    public static boolean checkAvailability(int carID){
        try {
            conn = getConnection();
            String sql;

            sql = "SELECT * FROM car WHERE vehicle_status = ? ";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "available");
            ResultSet rs = ps.executeQuery();
            conn.close();

            return rs.next();

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Error occured! \n");
        }
    }

}