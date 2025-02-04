package db;

import java.sql.Connection;
import java.sql.SQLException;
public class TestDatabaseConnection {
    public static void main(String[] args) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        if (connection != null) {
            System.out.println("Connection successful!");
        } else {
            System.out.println("Connection failed!");
        }
    }
}

