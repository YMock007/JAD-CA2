package db;

import handlingPassword.PasswordHashing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Seeder {
    private static final Logger LOGGER = Logger.getLogger(Seeder.class.getName());

    public static Boolean executeSQLScript(String filePath) {
        try (Connection connection = DatabaseConnection.getConnection();
             BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            if (connection == null) {
                LOGGER.severe("Failed to connect to the database.");
                return false;
            }

            LOGGER.info("Database connection successful!");

            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("--")) {
                    continue;
                }

                sql.append(line).append(" ");

                if (line.endsWith(";")) {
                    String fullSql = sql.toString().trim();
                    sql.setLength(0); // Clear buffer

                    if (fullSql.startsWith("INSERT INTO Worker ")) {
                        seedWorkerTable(fullSql, connection);
                    } else if (fullSql.startsWith("INSERT INTO WorkerCategory")) {
                        seedWorkerCategoryTable(fullSql, connection);
                    } else {
                        executeRawSQL(fullSql, connection);
                    }
                }
            }

            LOGGER.info("Database seeding completed successfully!");
            return true;
        } catch (SQLException | IOException e) {
            LOGGER.log(Level.SEVERE, "Error seeding data", e);
            return false;
        }
    }

    /**
     * Inserts hashed passwords for workers before executing the SQL statement.
     */
    private static void seedWorkerTable(String sqlLine, Connection connection) throws SQLException {
        sqlLine = sqlLine.replaceAll("ON CONFLICT.*;", ""); // ✅ Remove `ON CONFLICT` before processing

        String valuesPart = sqlLine.substring(sqlLine.indexOf("VALUES") + 6).trim();
        valuesPart = valuesPart.replaceAll("^\\(", "").replaceAll("\\);?$", "");
        String[] rows = valuesPart.split("\\),\\s*\\("); // Splitting each row

        String insertWorkerSQL = "INSERT INTO Worker (name, email, phone, password, role_id) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertWorkerSQL)) {
            for (String row : rows) {
                LOGGER.info("Processing Worker row: " + row);
                String[] columns = row.split(",(?=(?:[^']*'[^']*')*[^']*$)"); // Preserve quoted values

                if (columns.length < 5) {
                    LOGGER.severe("Invalid Worker row format: " + row);
                    throw new SQLException("Invalid column count in Worker table INSERT statement.");
                }

                String name = cleanValue(columns[0]);
                String email = cleanValue(columns[1]);
                String phone = cleanValue(columns[2]);

                // ✅ Validate phone length before inserting
                if (phone.length() > 15) {
                    LOGGER.severe("Invalid phone number length: " + phone);
                    throw new SQLException("Phone number exceeds max length of 15 characters.");
                }

                String plainPassword = cleanValue(columns[3]);
                String hashedPassword = PasswordHashing.hashPassword(plainPassword);

                // ✅ Ensure only numeric values in role_id
                String roleIdRaw = cleanValue(columns[4]).replaceAll("[^0-9]", ""); // Remove non-numeric characters
                if (roleIdRaw.isEmpty()) {
                    LOGGER.severe("Invalid role_id format in row: " + row);
                    throw new SQLException("Invalid role_id format in Worker table INSERT statement.");
                }
                int roleId = Integer.parseInt(roleIdRaw); // Now it's a valid integer

                stmt.setString(1, name);
                stmt.setString(2, email);
                stmt.setString(3, phone);
                stmt.setString(4, hashedPassword);
                stmt.setInt(5, roleId);
                stmt.addBatch();
            }

            stmt.executeBatch();
            LOGGER.info("Worker table seeded successfully with hashed passwords!");
        }
    }

    /**
     * Inserts Worker-Category relationships.
     */
    private static void seedWorkerCategoryTable(String sqlLine, Connection connection) throws SQLException {
        // ✅ Remove `ON CONFLICT` to process correctly
        sqlLine = sqlLine.replaceAll("ON CONFLICT.*;", "");

        // ✅ Remove all SQL comments (Lines starting with `--`)
        sqlLine = sqlLine.replaceAll("--.*?\\n", "").trim();

        // ✅ Extract the VALUES section properly
        String valuesPart = sqlLine.substring(sqlLine.indexOf("VALUES") + 6).trim();
        valuesPart = valuesPart.replaceAll("^\\(", "").replaceAll("\\);?$", "");

        // ✅ Split each (worker_id, category_id) entry correctly
        String[] rows = valuesPart.split("\\),\\s*\\("); 

        LOGGER.info("Extracted WorkerCategory rows: " + Arrays.toString(rows));

        String insertWorkerCategorySQL = "INSERT INTO WorkerCategory (worker_id, category_id) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(insertWorkerCategorySQL)) {
            for (String row : rows) {
                row = row.replaceAll("[()']", "").trim(); // ✅ Remove extra spaces and brackets

                LOGGER.info("Processing WorkerCategory row: " + row);
                String[] columns = row.split(",");

                if (columns.length < 2) {
                    LOGGER.severe("Invalid WorkerCategory row format: " + row);
                    throw new SQLException("Invalid column count in WorkerCategory INSERT statement.");
                }

                int workerId = Integer.parseInt(columns[0].trim());
                int categoryId = Integer.parseInt(columns[1].trim());

                stmt.setInt(1, workerId);
                stmt.setInt(2, categoryId);
                stmt.addBatch();
            }

            stmt.executeBatch(); // ✅ Execute after adding all rows
            LOGGER.info("WorkerCategory table seeded successfully!");
        }
    }



    private static String cleanValue(String value) {
        return value.replaceAll("[()']", "").trim();
    }

    private static void executeRawSQL(String sqlLine, Connection connection) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            LOGGER.info("Executing SQL: " + sqlLine);
            stmt.executeUpdate(sqlLine);
        }
    }
}
