package cleanify_ws.cleanify_ws.dbaccess_dao;

import cleanify_ws.cleanify_ws.dto.*;
import cleanify_ws.cleanify_ws.dbaccess_common.DBConnection;

import java.sql.*;
import java.util.*;

public class ShareServiceDAO {

    public ArrayList<CategoryDTO> getAvailableBookings() {
    	Connection conn = null;
        Map<Integer, CategoryDTO> categoryMap = new HashMap<>();

        

        try{
        	conn = DBConnection.getConnection();
        	String query = "SELECT c.id AS category_id, c.name AS category_name, " +
                    "b.id AS booking_id, p.name AS requester_name, p.phNumber AS requester_phone, " +
                    "b.date_requested, b.time_requested, b.address, b.postalCode, b.remark, " +
                    "s.name AS service_name, s.description, s.price, s.image_Url, " +
                    "st.name AS status_name " +  // Include status name
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id " +
                    "JOIN Category c ON s.category_id = c.id " +
                    "JOIN Person p ON b.requester_id = p.id " +
                    "JOIN Status st ON b.status_id = st.id " +  // Join Status table
                    "WHERE b.provider_id IS NULL " +   // Booking not yet assigned
                    "AND st.name = 'Pending' " +      // Only fetch pending bookings
                    "ORDER BY c.id";
        	PreparedStatement pstmt = conn.prepareStatement(query);
        	ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");

                if (!categoryMap.containsKey(categoryId)) {
                    categoryMap.put(categoryId, new CategoryDTO(categoryId, categoryName, new ArrayList<>()));
                }

                BookingDTO booking = new BookingDTO(
                        rs.getInt("booking_id"),
                        rs.getString("requester_name"),
                        rs.getString("requester_phone"),
                        rs.getString("date_requested"),
                        rs.getString("time_requested"),
                        rs.getString("address"),
                        rs.getString("postalCode"),
                        rs.getString("remark"),
                        new ServiceDTO(
                                rs.getString("service_name"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("image_Url")
                        )
                );

                categoryMap.get(categoryId).getBookings().add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(categoryMap.values());
    }

    public ArrayList<CategoryDTO> getAcceptedBookings(int workerId) {
        Connection conn = null;
        Map<Integer, CategoryDTO> categoryMap = new HashMap<>();

        try {
            conn = DBConnection.getConnection();
            String query = "SELECT c.id AS category_id, c.name AS category_name, " +
                    "b.id AS booking_id, p.name AS requester_name, p.phNumber AS requester_phone, " +
                    "b.date_requested, b.time_requested, b.address, b.postalCode, b.remark, " +
                    "s.name AS service_name, s.description, s.price, s.image_Url, " +
                    "st.name AS status_name " +
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id " +
                    "JOIN Category c ON s.category_id = c.id " +
                    "JOIN Person p ON b.requester_id = p.id " +
                    "JOIN Status st ON b.status_id = st.id " +
                    "WHERE b.provider_id = ? " +  // Fetch only bookings accepted by this worker
                    "AND b.status_id = 4 " +  // Only fetch accepted bookings (status_id = 4)
                    "ORDER BY c.id";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, workerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int categoryId = rs.getInt("category_id");
                String categoryName = rs.getString("category_name");

                if (!categoryMap.containsKey(categoryId)) {
                    categoryMap.put(categoryId, new CategoryDTO(categoryId, categoryName, new ArrayList<>()));
                }

                BookingDTO booking = new BookingDTO(
                        rs.getInt("booking_id"),
                        rs.getString("requester_name"),
                        rs.getString("requester_phone"),
                        rs.getString("date_requested"),
                        rs.getString("time_requested"),
                        rs.getString("address"),
                        rs.getString("postalCode"),
                        rs.getString("remark"),
                        new ServiceDTO(
                                rs.getString("service_name"),
                                rs.getString("description"),
                                rs.getDouble("price"),
                                rs.getString("image_Url")
                        )
                );

                categoryMap.get(categoryId).getBookings().add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>(categoryMap.values());
    }

    
    
    public ArrayList<CompletedBookingDTO> getCompletedBookings(int workerId) {
        Connection conn = null;
        ArrayList<CompletedBookingDTO> completedBookings = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();
            String query = "SELECT b.id AS booking_id, b.date_requested, b.time_requested, b.address, " +
                    "s.name AS service_name, s.price " +
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id " +
                    "WHERE b.provider_id = ? " +
                    "AND b.status_id = 2 " +  // ✅ Status ID 2 means 'Completed'
                    "ORDER BY b.date_requested DESC";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, workerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                CompletedBookingDTO booking = new CompletedBookingDTO(
                        rs.getInt("booking_id"),
                        rs.getString("date_requested"),
                        rs.getString("time_requested"),
                        rs.getString("address"),
                        new CompletedServiceDTO(rs.getString("service_name"), rs.getDouble("price"))
                );
                completedBookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedBookings;
    }




    
    
    
    
}
