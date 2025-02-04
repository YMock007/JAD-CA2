package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InitTables {
    private static final Logger LOGGER = Logger.getLogger(InitTables.class.getName());

    public static Boolean executeSQLScript(String filePath) {
        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            if (connection == null) {
                System.out.println("Failed to make a connection to the database.");
                return false;
            }

            System.out.println("Connection successful!");
            Statement stmt = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                sql.append(line).append("\n");
            }
            stmt.executeUpdate(sql.toString());
            System.out.println("Tables initialized successfully!");
            stmt.close();
            return true;
        } catch (SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error initializing tables", e);
            return false;
        }
    }
}
