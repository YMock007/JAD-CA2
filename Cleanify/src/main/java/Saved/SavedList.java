package Saved;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import db.DatabaseConnection;
import Persons.Person;
import Services.ServiceList;

public class SavedList {
	private static List<Saved> savedItems = new ArrayList<>();
    private static boolean isLoaded = false;
    private static boolean hasNewUpdate = false;

    private static void loadCartsFromDatabase(Person person) {
    	person.clearSavedItems();
        savedItems.clear();
        try (Connection conn = DatabaseConnection.getConnection()) {

            String query = "SELECT sd.id AS saved_id, " +
                           "s.id AS service_id, " +
                           "s.name AS service_name, " +
                           "s.price AS service_price, " +
                           "s.image_Url, " +
                           "cat.name AS category_name " +
                           "FROM Saved sd " +
                           "JOIN Service s ON sd.service_id = s.id " +
                           "JOIN Category cat ON cat.id = s.category_id " +
                           "WHERE sd.person_id = ?";

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, person.getId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int savedId = rs.getInt("saved_id");
                int serviceId = rs.getInt("service_id");
                String serviceName = rs.getString("service_name");
                String categoryName = rs.getString("category_name");
                String serviceImageUrl = rs.getString("image_Url");

                Saved savedItem = new Saved(savedId, serviceId, serviceName, serviceImageUrl, categoryName);
                savedItems.add(savedItem);
                person.addSavedItem(savedItem);
            }

            rs.close();
            stmt.close();
            isLoaded = true;
            hasNewUpdate = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Public method to get all carts
    public static List<Saved> getAllSavedItems(Person person) {
        if (!isLoaded || hasNewUpdate) {
            loadCartsFromDatabase(person);
        }
        return savedItems;
    }
    
    public static boolean checkSavedItems(Person member, int serviceId) throws SQLException {
        String query = "SELECT * FROM Saved WHERE person_id = ? AND service_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, member.getId());
            stmt.setInt(2, serviceId);
            
            // Execute the query
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return false;  
            }
            return true;  
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;  
        }
    }

    
    public static void addToSavedItems(Person member, int serviceId) {
    	
        String query = "INSERT INTO Saved (person_id, service_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, member.getId());
            stmt.setInt(2, serviceId);
            stmt.executeUpdate();
            markAsUpdated(); 
            member.hasUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    // Method for add to cart
    public static void removeFromSavedItems(int savedId, Person member) {
        String query = "DELETE FROM Saved where id = ? and person_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, savedId);
            stmt.setInt(2, member.getId());
            stmt.executeUpdate();
            markAsUpdated();
            member.hasUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

    private static void markAsUpdated() {
        hasNewUpdate = true;
    }
}