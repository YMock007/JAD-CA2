package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import Utils.ConfigLoader;

public class DatabaseConnection {

	private static final String URL = ConfigLoader.get("DATABASE_URL");
	private static final String USER =ConfigLoader.get("DATABASE_USER");;
	private static final String PASSWORD = ConfigLoader.get("DATABASE_PASSWORD");;
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

	public static Connection getConnection() throws SQLException {
		// TODO Auto-generated method stub
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Database connection established successfully.");
            return connection;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found", e);
            throw new SQLException("Driver not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failed", e);
            throw e;
        }
    }

}