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
}