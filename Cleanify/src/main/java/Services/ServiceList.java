package Services;

import Categories.CategoryList;
import Reviews.Review;
import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
public class ServiceList {

    // Retrieve all services
    public static List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM Service";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Service service = new Service(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getFloat("price"),
                        resultSet.getInt("category_id"),
                        resultSet.getString("image_Url"),
                        resultSet.getFloat("est_duration")
                );
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return services;
    }

    // Add a new service
    public static boolean addService(Service service) {
        String query = "INSERT INTO Service (name, description, price, category_id, image_Url, est_duration) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.setFloat(3, service.getPrice());
            statement.setInt(4, service.getCategoryId());
            statement.setString(5, service.getImageUrl());
            statement.setFloat(6, service.getEstDuration());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Notify CategoryList of changes
                CategoryList.markAsUpdated();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update an existing service
    public static boolean updateService(Service service) {
        String query = "UPDATE Service SET name = ?, description = ?, price = ?, category_id = ?, image_Url = ?, est_duration = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, service.getName());
            statement.setString(2, service.getDescription());
            statement.setFloat(3, service.getPrice());
            statement.setInt(4, service.getCategoryId());
            statement.setString(5, service.getImageUrl());
            statement.setFloat(6, service.getEstDuration());
            statement.setInt(7, service.getId());

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Notify CategoryList of changes
                CategoryList.markAsUpdated();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a service
    public static boolean deleteService(int id) {
        String query = "DELETE FROM Service WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                CategoryList.markAsUpdated();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Service getServiceById(int id) {
        String serviceQuery = "SELECT * FROM Service WHERE id = ?";
        String reviewsQuery = "SELECT * FROM Review WHERE booking_id IN (SELECT id FROM Booking WHERE service_id = ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement serviceStatement = connection.prepareStatement(serviceQuery)) {

            // Retrieve the service details
            serviceStatement.setInt(1, id);
            
            try (ResultSet serviceResultSet = serviceStatement.executeQuery()) {
                if (serviceResultSet.next()) {
                    Service service = new Service(
                            serviceResultSet.getInt("id"),
                            serviceResultSet.getString("name"),
                            serviceResultSet.getString("description"),
                            serviceResultSet.getFloat("price"),
                            serviceResultSet.getInt("category_id"),
                            serviceResultSet.getString("image_Url"),
                            serviceResultSet.getFloat("est_duration")
                    );

                    // Now retrieve reviews for this service
                    List<Review> reviews = new ArrayList<>();
                    try (PreparedStatement reviewsStatement = connection.prepareStatement(reviewsQuery)) {
                        reviewsStatement.setInt(1, id);
                        
                        try (ResultSet reviewResultSet = reviewsStatement.executeQuery()) {
                            while (reviewResultSet.next()) {
                                // Map each review to a Review object
                                Review review = new Review(
                                        reviewResultSet.getInt("id"),
                                        reviewResultSet.getInt("rating"),
                                        reviewResultSet.getString("content"),
                                        reviewResultSet.getTimestamp("date_created"),
                                        reviewResultSet.getInt("booking_id")
                                );
                                reviews.add(review);
                            }
                        }
                    }

                    service.setReviews(reviews);

                    return service;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving service with ID " + id);
            e.printStackTrace();
            return null;
        }
    }


 // Method to retrieve services by the cart
    public static List<Service> getServicesByCart(HashMap<Integer, Integer> cart) {
        List<Service> services = new ArrayList<>();
        
        if (cart == null || cart.isEmpty()) {
            return services;
        }

        StringBuilder idList = new StringBuilder();
        for (Integer serviceId : cart.keySet()) {
            idList.append(serviceId).append(",");
        }
        idList.deleteCharAt(idList.length() - 1);

        String query = "SELECT * FROM Service WHERE id IN (" + idList.toString() + ")";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    services.add(new Service(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getFloat("price"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("image_Url"),
                            resultSet.getFloat("est_duration")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error while retrieving services for cart");
            e.printStackTrace();
        }

        return services; 
    }
    
 // Retrieve a service by name
    public static Service getServiceByName(String name) {
        String query = "SELECT * FROM Service WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Service(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"),
                            resultSet.getFloat("price"),
                            resultSet.getInt("category_id"),
                            resultSet.getString("image_Url"),
                            resultSet.getFloat("est_duration")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
