package Categories;

import Services.Service;

import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CategoryList {
    private static List<Category> categories = new ArrayList<>();
    private static boolean isLoaded = false;
    private static boolean hasNewUpdate = false;

    public static List<Category> getCategories() {
        if (!isLoaded || hasNewUpdate) {
            loadCategoriesFromDatabase();
        }
        return categories;
    }

    private static void loadCategoriesFromDatabase() {
        HashMap<Integer, Category> categoryMap = new HashMap<>();
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Updated query to include image_url and properly format SQL
            String query = "SELECT c.id AS category_id, c.name AS category_name, " +
                           "s.id AS service_id, s.name AS service_name, s.description, s.est_duration, s.price, s.image_url " +
                           "FROM Category c " +
                           "LEFT JOIN Service s ON c.id = s.category_id";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");

                // Use category ID as the key in computeIfAbsent
                Category category = categoryMap.computeIfAbsent(categoryId, k -> new Category(k, categoryName, new ArrayList<>()));

                int serviceId = rs.getInt("service_id");
                if (!rs.wasNull()) {
                    String serviceName = rs.getString("service_name");
                    String description = rs.getString("description");
                    float estDuration = rs.getFloat("est_duration");
                    float price = rs.getFloat("price");
                    String imageUrl = rs.getString("image_url"); 

                    // Create a Service object including the image URL
                    Service service = new Service(serviceId, serviceName, description, price, categoryId, imageUrl, estDuration);
                    category.getServices().add(service);
                }
            }

            categories = new ArrayList<>(categoryMap.values());
            isLoaded = true;
            hasNewUpdate = false;

            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Method to add a new category
    public static void addCategory(Category category) {
        String query = "INSERT INTO Category (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
            markAsUpdated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to update an existing category
    public static void updateCategory(Category category) {
        String query = "UPDATE Category SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, category.getName());
            stmt.setInt(2, category.getId());
            stmt.executeUpdate();
            markAsUpdated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to delete a category
    public static void deleteCategory(int categoryId) {
        String query = "DELETE FROM Category WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            stmt.executeUpdate();
            markAsUpdated();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Mark that there's a new update so data will be reloaded
    public static void markAsUpdated() {
        hasNewUpdate = true;
    }
    
    public static boolean isCategoryNameExists(String categoryName) {
        String query = "SELECT COUNT(*) FROM Category WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean isCategoryNameExistsExcludingId(String categoryName, int excludeCategoryId) {
        String query = "SELECT COUNT(*) FROM Category WHERE name = ? AND id != ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, categoryName);
            statement.setInt(2, excludeCategoryId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
