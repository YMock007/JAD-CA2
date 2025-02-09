package Reporting;

import Services.ServiceReport; // Import ServiceReport instead of Service
import db.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Handles service-related reporting queries.
 */
public class ServicesDataAnalysis {

    // Get the best-rated services based on average ratings
    public static List<ServiceReport> getBestRatedServices() {
        return getServicesByRating("DESC");
    }

    // Get the lowest-rated services based on average ratings
    public static List<ServiceReport> getLowestRatedServices() {
        return getServicesByRating("ASC");
    }

    private static List<ServiceReport> getServicesByRating(String order) {
        List<ServiceReport> services = new ArrayList<>();
        String query = "SELECT s.id, s.name, s.price, s.category_id, " +
                       "COALESCE(AVG(r.rating), 0) AS avg_rating " +
                       "FROM Service s " +
                       "LEFT JOIN Review r ON s.id = r.booking_id " +
                       "GROUP BY s.id " +
                       "ORDER BY avg_rating " + order + " LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ServiceReport service = new ServiceReport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getFloat("price"),
                        rs.getInt("category_id"),
                        rs.getDouble("avg_rating")
                );
                services.add(service);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return services;
    }

    // Get high-demand services (most booked)
    public static List<String> getHighDemandServiceNames() {
        return getServiceNames("ORDER BY COUNT(b.id) DESC LIMIT 5");
    }

    public static List<Integer> getHighDemandBookings() {
        return getBookingCounts("ORDER BY COUNT(b.id) DESC LIMIT 5");
    }

    private static List<String> getServiceNames(String orderBy) {
        List<String> serviceNames = new ArrayList<>();
        String query = "SELECT s.name FROM Service s " +
                       "LEFT JOIN Booking b ON s.id = b.service_id " +
                       "GROUP BY s.name " + orderBy;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                serviceNames.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serviceNames;
    }

    private static List<Integer> getBookingCounts(String orderBy) {
        List<Integer> counts = new ArrayList<>();
        String query = "SELECT COUNT(b.id) FROM Service s " +
                       "LEFT JOIN Booking b ON s.id = b.service_id " +
                       "GROUP BY s.name " + orderBy;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                counts.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counts;
    }


    // Get revenue breakdown by category
    public static List<String> getCategoryNames() {
        return getCategoryData("SELECT DISTINCT c.name FROM Category c");
    }

    public static List<Double> getCategoryRevenues() {
        List<Double> revenues = new ArrayList<>();
        String query = "SELECT SUM(s.price) FROM Service s " +
                       "JOIN Category c ON s.category_id = c.id " +
                       "JOIN Booking b ON s.id = b.service_id " +
                       "GROUP BY c.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                revenues.add(rs.getDouble(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return revenues;
    }

    private static List<String> getCategoryData(String query) {
        List<String> data = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                data.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    public static List<DailyRevenueService> getDailyRevenueTrends(int year, int month) {
        List<DailyRevenueService> dailyTrends = new ArrayList<>();

        // ✅ Correctly formatted SQL query
        String query = "SELECT TO_CHAR(b.date_requested, 'YYYY-MM-DD') AS day, " +
                       "SUM(s.price) AS total_revenue " +
                       "FROM Booking b " +
                       "JOIN Service s ON b.service_id = s.id " +
                       "WHERE DATE_PART('year', b.date_requested) = ? " +
                       "AND DATE_PART('month', b.date_requested) = ? " +
                       "GROUP BY day " +
                       "ORDER BY day;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, year);
            stmt.setInt(2, month);

            ResultSet rs = stmt.executeQuery();

            // ✅ Debugging: Print year & month
            System.out.println("Fetching Daily Revenue Trends for Year: " + year + ", Month: " + month);

            while (rs.next()) {
                String day = rs.getString("day");
                double revenue = rs.getDouble("total_revenue");

                dailyTrends.add(new DailyRevenueService(day, revenue));

                // ✅ Debugging: Print fetched data
                System.out.println("Loaded Data: " + day + " -> Revenue: " + revenue);
            }

            // ✅ Debugging: Check if list is empty
            if (dailyTrends.isEmpty()) {
                System.out.println("⚠️ WARNING: No revenue data found for " + year + "-" + month);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dailyTrends;
    }



    
    // Get most booked services
    public static List<String> getMostBookedServiceNames() {
        return getServiceNames("ORDER BY COUNT(b.id) DESC LIMIT 5");
    }

    public static List<Integer> getMostBookedBookings() {
        return getBookingCounts("ORDER BY COUNT(b.id) DESC LIMIT 5");
    }
}
