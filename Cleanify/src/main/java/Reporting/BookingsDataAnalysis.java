package Reporting;

import Bookings.BookingReport;
import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookingsDataAnalysis {

    // Bookings by Date for a Specific Year and Month
    public static List<BookingReport> getBookingsByDate(int year, int month) {
        List<BookingReport> bookings = new ArrayList<>();
        String query = "SELECT date_requested, COUNT(*) AS count " +
                       "FROM Booking " +
                       "WHERE EXTRACT(YEAR FROM date_requested) = ? AND EXTRACT(MONTH FROM date_requested) = ? " +
                       "GROUP BY date_requested ORDER BY date_requested";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookings.add(new BookingReport(rs.getDate("date_requested").toString(), rs.getInt("count")));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Top 10 Customers by Booking Value
    public static List<BookingReport> getTopCustomers() {
        List<BookingReport> topCustomers = new ArrayList<>();
        String query = "SELECT p.name, SUM(s.price) AS value " +
                       "FROM Booking b " +
                       "JOIN Person p ON b.requester_id = p.id " +
                       "JOIN Service s ON b.service_id = s.id " +
                       "GROUP BY p.name ORDER BY value DESC LIMIT 10";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                topCustomers.add(new BookingReport(rs.getString("name"), rs.getDouble("value")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topCustomers;
    }

    // Service Bookings
    public static List<BookingReport> getServiceBookings() {
        List<BookingReport> serviceBookings = new ArrayList<>();
        String query = "SELECT s.name AS serviceName, COUNT(*) AS count " +
                       "FROM Booking b " +
                       "JOIN Service s ON b.service_id = s.id " +
                       "GROUP BY s.name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                serviceBookings.add(new BookingReport(rs.getString("serviceName"), rs.getInt("count")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serviceBookings;
    }
}
