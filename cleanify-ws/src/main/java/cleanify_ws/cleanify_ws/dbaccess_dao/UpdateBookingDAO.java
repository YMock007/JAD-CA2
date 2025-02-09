package cleanify_ws.cleanify_ws.dbaccess_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cleanify_ws.cleanify_ws.dbaccess_common.DBConnection;

public class UpdateBookingDAO {

    // ✅ Accept booking by updating provider_id & status_id
	public boolean updateBooking(int bookingId, int providerId, int statusId) throws SQLException {
	    Connection conn = null;
	    PreparedStatement checkStmt = null;
	    PreparedStatement conflictStmt = null;
	    PreparedStatement updateStmt = null;
	    ResultSet rs = null;

	    try {
	        conn = DBConnection.getConnection();

	        // ✅ Step 1: Check if booking exists & is not accepted
	        String checkQuery = "SELECT provider_id FROM Booking WHERE id = ?";
	        checkStmt = conn.prepareStatement(checkQuery);
	        checkStmt.setInt(1, bookingId);
	        rs = checkStmt.executeQuery();

	        if (!rs.next()) {
	            System.out.println("⚠ ERROR: Booking ID " + bookingId + " does not exist.");
	            return false;
	        } else if (rs.getInt("provider_id") != 0) {
	            System.out.println("⚠ ERROR: Booking " + bookingId + " is already accepted by another worker.");
	            return false;
	        }

	        // ✅ Step 2: Get booking date & time
	        java.sql.Date bookingDate = getBookingDate(bookingId, conn);
	        java.sql.Time bookingTime = getBookingTime(bookingId, conn);
	        if (bookingDate == null || bookingTime == null) {
	            System.out.println("⚠ ERROR: Booking " + bookingId + " has no date/time.");
	            return false;
	        }

	        // ✅ Step 3: Check for time conflict
	        String conflictQuery = "SELECT COUNT(*) FROM Booking WHERE provider_id = ? AND date_requested = ? AND time_requested = ?";
	        conflictStmt = conn.prepareStatement(conflictQuery);
	        conflictStmt.setInt(1, providerId);
	        conflictStmt.setDate(2, bookingDate);
	        conflictStmt.setTime(3, bookingTime);

	        ResultSet conflictResult = conflictStmt.executeQuery();
	        if (conflictResult.next() && conflictResult.getInt(1) > 0) {
	            System.out.println("⚠ ERROR: Worker " + providerId + " already has a booking at this time.");
	            return false;
	        }

	        // ✅ Step 4: Accept the booking
	        String updateQuery = "UPDATE Booking SET provider_id = ?, status_id = ? WHERE id = ? AND status_id = 1";
	        updateStmt = conn.prepareStatement(updateQuery);
	        updateStmt.setInt(1, providerId);
	        updateStmt.setInt(2, statusId);
	        updateStmt.setInt(3, bookingId);

	        int rowsUpdated = updateStmt.executeUpdate();
	        if (rowsUpdated > 0) {
	            System.out.println("✅ SUCCESS: Booking " + bookingId + " accepted by Worker " + providerId);
	            return true;
	        } else {
	            System.out.println("⚠ ERROR: Booking " + bookingId + " not updated. Maybe status_id != 1?");
	            return false;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        if (rs != null) rs.close();
	        if (checkStmt != null) checkStmt.close();
	        if (conflictStmt != null) conflictStmt.close();
	        if (updateStmt != null) updateStmt.close();
	        if (conn != null) conn.close();
	    }
	}


    // ✅ Helper method to get the date of a booking
    private java.sql.Date getBookingDate(int bookingId, Connection conn) throws SQLException {
        String query = "SELECT date_requested FROM Booking WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDate("date_requested");
            }
        }
        return null;
    }

    // ✅ Helper method to get the time of a booking
    private java.sql.Time getBookingTime(int bookingId, Connection conn) throws SQLException {
        String query = "SELECT time_requested FROM Booking WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getTime("time_requested");
            }
        }
        return null;
    }
    
    
    public boolean completeBooking(int bookingId, int providerId, int statusId) throws SQLException {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            // ✅ Step 1: Check if the booking exists and is assigned to the correct provider
            String checkQuery = "SELECT provider_id, status_id FROM Booking WHERE id = ?";
            checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, bookingId);
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("⚠ ERROR: Booking ID " + bookingId + " does not exist.");
                return false;
            } 

            int existingProviderId = rs.getInt("provider_id");
            int existingStatusId = rs.getInt("status_id");

            if (existingProviderId != providerId) {
                System.out.println("⚠ ERROR: Worker " + providerId + " is not assigned to Booking " + bookingId);
                return false;
            }

            if (existingStatusId != 4) { // Assuming 4 is "Accepted"
                System.out.println("⚠ ERROR: Booking " + bookingId + " is not in the Accepted state.");
                return false;
            }

            // ✅ Step 2: Update the booking status to "Completed"
            String updateQuery = "UPDATE Booking SET status_id = ? WHERE id = ? AND provider_id = ?";
            updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setInt(1, statusId);  // Assuming statusId = 2 represents "Completed"
            updateStmt.setInt(2, bookingId);
            updateStmt.setInt(3, providerId);

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ SUCCESS: Booking " + bookingId + " marked as Completed by Worker " + providerId);
                return true;
            } else {
                System.out.println("⚠ ERROR: Booking " + bookingId + " was not updated.");
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (rs != null) rs.close();
            if (checkStmt != null) checkStmt.close();
            if (updateStmt != null) updateStmt.close();
            if (conn != null) conn.close();
        }
    }

}
