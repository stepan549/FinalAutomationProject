package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static Connection CONNECTION;
    private static String URL = "jdbc:mysql://localhost:3306/autotest?useSSL=false&serverTimezone=UTC";
    private static String USER = "root";
    private static String PASSWORD = "5190";

    private ConnectionDB() {
    }

    public static Connection getConnection() {
        if (CONNECTION == null) {
            try {
                CONNECTION = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return CONNECTION;
    }
}
