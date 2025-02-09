package db;

import handlingPassword.passwordHashing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Seeder {
    private static final Logger LOGGER = Logger.getLogger(Seeder.class.getName());

    public static Boolean executeSQLScript(String filePath) {
        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            if (connection == null) {
                System.out.println("Failed to connect to the database.");
                return false;
            }

            System.out.println("Executing SQL script: " + filePath);
            Statement stmt = connection.createStatement();
            StringBuilder sql = new StringBuilder();
            String line;
            boolean insideWithClause = false; // Tracks multi-line WITH statements

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--")) continue; // Skip empty lines and comments

                sql.append(line).append(" ");

                // Handle multi-line WITH clause
                if (line.matches("(?i)^WITH\\s.*")) { 
                    insideWithClause = true; 
                }
                if (insideWithClause && line.endsWith(";")) { 
                    insideWithClause = false; // WITH clause completed
                }

                // Execute when a full statement is formed
                if (!insideWithClause && line.endsWith(";")) {
                    String fullSql = sql.toString().trim();
                    System.out.println("Executing: " + fullSql); // Debugging output

                    try {
                        if (fullSql.startsWith("INSERT INTO Person")) {
                            seedPersonTable(fullSql, connection);
                        } else {
                            executeRawSQL(fullSql, connection);
                        }
                    } catch (SQLException e) {
                        System.out.println("SQL Execution Failed: " + fullSql);
                        e.printStackTrace();
                    }

                    sql.setLength(0); // Reset for next statement
                }
            }

            System.out.println("SQL script executed successfully!");
            stmt.close();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void seedPersonTable(String sqlLine, Connection connection) throws SQLException {
        // Extract the VALUES part of the SQL
        String valuesPart = sqlLine.substring(sqlLine.indexOf("VALUES") + 6).trim();
        valuesPart = valuesPart.replaceFirst("^\\(", "").replaceFirst("\\)$", "");
        String[] rows = splitRows(valuesPart);

        String insertPersonSQL = "INSERT INTO Person (name, password, email, phNumber, address, postalCode, role_id, is_google_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertPersonSQL)) {
            for (String row : rows) {
                System.out.println("Raw Row: " + row);
                String[] columns = splitColumns(row);

                if (columns.length != 8) {
                    LOGGER.severe("Invalid row: " + row);
                    throw new SQLException("Invalid column count in Person table INSERT statement.");
                }

                // Extract and hash the password
                String name = columns[0].replace("'", "").trim();
                String plainPassword = columns[1].replace("'", "").trim();
                String hashedPassword = passwordHashing.hashPassword(plainPassword);
                String email = columns[2].replace("'", "").trim();
                String phNumber = columns[3].replace("'", "").trim();
                phNumber = phNumber.equalsIgnoreCase("NULL") ? null : phNumber;
                String address = columns[4].replace("'", "").trim();
                String postalCode = columns[5].replace("'", "").trim();
                int roleId = Integer.parseInt(columns[6].trim());
                boolean isGoogleUser = Boolean.parseBoolean(columns[7].trim());

                // Set values in the prepared statement
                stmt.setString(1, name);
                stmt.setString(2, hashedPassword);
                stmt.setString(3, email);
                stmt.setString(4, phNumber);
                stmt.setString(5, address);
                stmt.setString(6, postalCode);
                stmt.setInt(7, roleId);
                stmt.setBoolean(8, isGoogleUser);
                stmt.addBatch();
            }

            stmt.executeBatch();
            System.out.println("Person table seeded successfully with hashed passwords!");
        }
    }

    private static String[] splitRows(String valuesPart) {
        // Splits rows by closing parenthesis followed by a comma and an opening parenthesis
        return valuesPart.split("\\),\\s*\\(");
    }

    private static String[] splitColumns(String row) {
        // Splits the row by commas while ignoring commas inside quotes
        return row.split(",(?=(?:[^']*'[^']*')*[^']*$)");
    }

    private static void executeRawSQL(String sqlLine, Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            System.out.println("Executing SQL: " + sqlLine); 
            stmt.executeUpdate(sqlLine);
        }
    }
}
