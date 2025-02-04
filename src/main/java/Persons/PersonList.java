package Persons;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import handlingPassword.passwordHashing;

public class PersonList {
    private static List<Person> people = new ArrayList<>();
    private static boolean isLoaded = false;
    private static boolean hasNewUpdate = false;

    public static List<Person> getPeople() {
        if (!isLoaded || hasNewUpdate) {
            loadPeopleFromDatabase();
        }
        return people;
    }

    private static void loadPeopleFromDatabase() {
        people.clear(); 
        String query = "SELECT p.*, r.name AS role_name FROM Person p " +
                       "LEFT JOIN Role r ON p.role_id = r.id ORDER BY p.id";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Person person = new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phNumber"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getInt("role_id"),
                    rs.getBoolean("is_google_user")
                );
                people.add(person); 
            }
            isLoaded = true;
            hasNewUpdate = false;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addPerson(Person person) {
        String query = "INSERT INTO Person (name, password, email, phNumber, address, postalCode, role_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, person.getName());
            stmt.setString(2, passwordHashing.hashPassword(person.getPassword()));
            stmt.setString(3, person.getEmail());
            stmt.setString(4, person.getPhNumber());
            stmt.setString(5, person.getAddress());
            stmt.setString(6, person.getPostalCode());
            stmt.setInt(7, person.getRoleId());
            stmt.executeUpdate();
            markAsUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePerson(Person person) {
        String query = "UPDATE Person SET name = ?, email = ?, phNumber = ?, address = ?, postalCode = ?, role_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, person.getName());
            stmt.setString(2, person.getEmail());
            stmt.setString(3, person.getPhNumber());
            stmt.setString(4, person.getAddress());
            stmt.setString(5, person.getPostalCode());
            stmt.setInt(6, person.getRoleId());
            stmt.setInt(7, person.getId());
            stmt.executeUpdate();
            markAsUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deletePerson(int personId) {
        String query = "DELETE FROM Person WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, personId);
            stmt.executeUpdate();
            markAsUpdated();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void markAsUpdated() {
        hasNewUpdate = true;
    }
    
    public static boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM Person WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; 
    }

    public static Person getPersonById(int id) {
        String query = "SELECT * FROM Person WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phNumber"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getInt("role_id"),
                    rs.getBoolean("is_google_user")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; 
    }
    
    public static List<Person> getAllCleaners() {
        List<Person> cleaners = new ArrayList<>();
        String query = "SELECT p.*, r.name AS role_name FROM Person p " +
                       "LEFT JOIN Role r ON p.role_id = r.id " +
                       "WHERE p.role_id = 3 " +
                       "ORDER BY p.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Person person = new Person(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phNumber"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getInt("role_id"),
                    rs.getBoolean("is_google_user"));
                cleaners.add(person); 
            }
            isLoaded = true;
            hasNewUpdate = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cleaners;
    }


}
