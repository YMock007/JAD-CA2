package cleanify_ws.cleanify_ws.dbaccess_booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cleanify_ws.cleanify_ws.dbaccess_common.DBConnection;

public class BookingDAO {
	
	// all bookings getting
	public ArrayList<BookingDTO> getAllBookings() throws SQLException {
		
		Connection conn = null;
        ArrayList<BookingDTO> bookings = new ArrayList<>();
        

        try{
        	conn = DBConnection.getConnection();
        	String query = "SELECT b.id, s.name AS service_name, b.date_requested, b.time_requested, " +
                    "b.address, b.postalCode, b.remark " +
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id";
        	Statement stmt = conn.createStatement();
        	ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                BookingDTO booking = new BookingDTO(
                    rs.getInt("id"),
                    rs.getString("service_name"),
                    rs.getDate("date_requested"),
                    rs.getTime("time_requested"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getString("remark")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	conn.close();
        }
        return bookings;
    }
	

	// get booking by worker
	public ArrayList<BookingDTO> getBookingsForWorker(int workerId) throws SQLException {
		Connection conn = null;
        ArrayList<BookingDTO> bookings = new ArrayList<>();
         

        try{
        	conn = DBConnection.getConnection();
        	String query = "SELECT DISTINCT b.id, s.name AS service_name, b.date_requested, b.time_requested, " +
                    "b.address, b.postalCode, b.remark " +
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id " +
                    "JOIN WorkerCategory wc ON wc.category_id = s.category_id " +
                    "WHERE wc.worker_id = ? AND b.status_id = 1"; 
        	PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, workerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BookingDTO booking = new BookingDTO(
                    rs.getInt("id"),
                    rs.getString("service_name"),
                    rs.getDate("date_requested"),
                    rs.getTime("time_requested"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getString("remark")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	conn.close();
        }
        return bookings;
    }
	

	// Fetch accepted booking details (Includes requester info only if worker accepted)
	public BookingDTO getBookingById(int bookingId, int workerId) throws SQLException {
	    Connection conn = null;

	    try {
	        conn = DBConnection.getConnection();
	        
	        // ✅ Ensure worker (provider_id) matches the logged-in worker
	        String query = "SELECT b.id, s.name AS service_name, b.date_requested, b.time_requested, " +
                    "b.address, b.postalCode, b.remark, " +
                    "p.name AS requester_name, b.phNumber AS requester_phone "+
                    "FROM Booking b " +
                    "JOIN Service s ON b.service_id = s.id " +
                    "JOIN Person p ON b.requester_id = p.id " +  
                    "WHERE b.id = ? AND b.status_id = 2 AND b.provider_id = ?";
	        
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, bookingId);
	        pstmt.setInt(2, workerId);
	        
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return new BookingDTO(
	                rs.getInt("id"),
	                rs.getString("service_name"),
	                rs.getDate("date_requested"),
	                rs.getTime("time_requested"),
	                rs.getString("address"),
	                rs.getString("postalCode"),
	                rs.getString("remark"),
	                rs.getString("requester_name"),
	                rs.getString("requester_phone")  
	            );
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        conn.close();
	    }
	    
	    return null; 
	}

	
    // Worker accepts a booking (Updates provider_id and status)
	public int acceptBooking(int bookingId, int workerId) throws SQLException {
	    Connection conn = null;

	    try {
	        conn = DBConnection.getConnection();
	        String query = "SELECT date_requested, time_requested FROM Booking WHERE id = ?";
	        PreparedStatement pstmt = conn.prepareStatement(query);
	        pstmt.setInt(1, bookingId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            Date dateRequested = rs.getDate("date_requested");
	            Time timeRequested = rs.getTime("time_requested");

	            if (hasExistingBooking(workerId, dateRequested, timeRequested)) {
	                System.out.println("Worker already has a confirmed booking at this time.");
	                return -1; 
	            } else {
	                String updateQuery = "UPDATE Booking SET provider_id = ?, status_id = 2 WHERE id = ? AND status_id = 1";
	                PreparedStatement updatePstmt = conn.prepareStatement(updateQuery);
	                updatePstmt.setInt(1, workerId);
	                updatePstmt.setInt(2, bookingId);
	                int updatedRows = updatePstmt.executeUpdate();

	                if (updatedRows > 0) {
	                    return 1; 
	                } else {
	                    return 0;
	                }
	            }
	        } else {
	            return -2; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -99; 
	    } finally {
	            conn.close();
	    }
	}


	
    
    // Checking if worker has existing booking at same requested day and time
    public boolean hasExistingBooking(int workerId, Date dateRequested, Time timeRequested) throws SQLException {
    	Connection conn = null;
        

        try{
        	conn = DBConnection.getConnection();
        	String query = "SELECT COUNT(*) FROM Booking " +
                    "WHERE provider_id = ? AND date_requested = ? AND time_requested = ? " +
                    "AND status_id = 2";  
        	PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, workerId);
            pstmt.setDate(2, dateRequested);
            pstmt.setTime(3, timeRequested);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	conn.close();
        }
        return false;
    }
    
    
    
    // get bookings accepted by a worker
    public ArrayList<BookingDTO> getAcceptedBookingsForWorker(int workerId) throws SQLException {
    	Connection conn = null;
        ArrayList<BookingDTO> bookings = new ArrayList<>();

        try{
        	conn = DBConnection.getConnection();
        	String query = "SELECT b.id, s.name AS service_name, b.date_requested, b.time_requested, " +
                      "b.address, b.postalCode, b.remark, " +
                      "p.name AS requester_name, b.phNumber AS requester_phone " + 
                      "FROM Booking b " +
                      "JOIN Service s ON b.service_id = s.id " +
                      "JOIN Person p ON b.requester_id = p.id " +
                      "WHERE b.provider_id = ? AND b.status_id = 2";  
        	PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, workerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BookingDTO booking = new BookingDTO(
                    rs.getInt("id"),
                    rs.getString("service_name"),
                    rs.getDate("date_requested"),
                    rs.getTime("time_requested"),
                    rs.getString("address"),
                    rs.getString("postalCode"),
                    rs.getString("remark"),
                    rs.getString("requester_name"),  
                    rs.getString("requester_phone")  
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	conn.close();
        }
        return bookings;
    }
	
	
}
