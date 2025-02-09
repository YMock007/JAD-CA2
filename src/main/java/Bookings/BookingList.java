package Bookings;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.DatabaseConnection;

public class BookingList {
    public static List<Booking> getBookingsByUser(int userId) throws SQLException {List<Booking> bookings = new ArrayList<>();
    String query = "SELECT * FROM Booking WHERE requester_id = ? ORDER BY date_requested DESC";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement stmt = connection.prepareStatement(query)) {

        stmt.setInt(1, userId);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Integer providerId = rs.getInt("provider_id");
            if (rs.wasNull()) {  
                providerId = 0;
            }

            Booking booking = new Booking(
                rs.getInt("id"),
                rs.getInt("requester_id"),
                providerId,  
                rs.getInt("service_id"),
                rs.getInt("status_id"),
                rs.getDate("date_requested"),
                rs.getTime("time_requested"),
                rs.getString("phNumber"),
                rs.getString("address"),
                rs.getString("postalCode"),
                rs.getString("remark")
            );
            bookings.add(booking);
        }
    }

    return bookings;
    }
    
	public static boolean createBooking(
	        int memberId, 
	        int serviceId, 
	        int statusId, 
	        Date dateRequested, 
	        Time timeRequested, 
	        String phoneNumber, 
	        String address, 
	        String postalCode, 
	        String specialRequest
	) throws SQLException {
	    String insertQuery = "INSERT INTO Booking (requester_id, service_id, status_id, date_requested, time_requested, phNumber, address, postalCode, remark) " +
	                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertQuery)) {

	        stmt.setInt(1, memberId); 
	        stmt.setInt(2, serviceId);
	        stmt.setInt(3, statusId);
	        stmt.setDate(4, dateRequested);
	        stmt.setTime(5, timeRequested);
	        stmt.setString(6, phoneNumber);
	        stmt.setString(7, address);
	        stmt.setString(8, postalCode);
	        stmt.setString(9, specialRequest);

	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static boolean createBookingWithPayment(
		    int memberId,
		    List<Integer> serviceIds,
		    int statusId,
		    Date dateRequested,
		    Time timeRequested,
		    String phoneNumber,
		    String address,
		    String postalCode,
		    String specialRequest,
		    double amount,
		    int paymentMethodId,
		    byte[] paymentDetails,
		    String billingAddress,
		    String billingPostalCode
		) throws SQLException {
		    Connection conn = null;
		    CallableStatement stmt = null;

		    try {
		        conn = DatabaseConnection.getConnection();
		        conn.setAutoCommit(false);

		        // ✅ Convert List<Integer> to SQL Array
		        Array servicesArray = conn.createArrayOf("INTEGER", serviceIds.toArray(new Integer[0]));

		        // ✅ Prepare Callable Statement (Explicit Casts for PostgreSQL)
		        stmt = conn.prepareCall("{ CALL create_bookings_and_payment(?, ?, ?, CAST(? AS DATE), CAST(? AS TIME), ?, ?, ?, ?, CAST(? AS NUMERIC), ?, ?, ?, ?, ?) }");

		        // ✅ Set Parameters Explicitly
		        stmt.setInt(1, memberId);
		        stmt.setArray(2, servicesArray);
		        stmt.setInt(3, statusId);
		        stmt.setDate(4, dateRequested);
		        stmt.setTime(5, timeRequested);
		        stmt.setString(6, phoneNumber);
		        stmt.setString(7, address);
		        stmt.setString(8, postalCode);
		        stmt.setString(9, specialRequest);
		        stmt.setDouble(10, amount);
		        stmt.setInt(11, paymentMethodId);
		        stmt.setBytes(12, paymentDetails);
		        stmt.setString(13, (paymentMethodId == 1) ? billingAddress : null);
		        stmt.setString(14, (paymentMethodId == 1) ? billingPostalCode : null);

		        // ✅ Register Output Parameter for Payment ID
		        stmt.registerOutParameter(15, Types.BIGINT);

		        // ✅ Execute Procedure
		        stmt.execute();
		        long paymentId = stmt.getLong(15);

		        // ✅ Commit and Return
		        if (paymentId > 0) {
		            conn.commit();
		            return true;
		        } else {
		            conn.rollback();
		            return false;
		        }

		    } catch (SQLException e) {
		        if (conn != null) conn.rollback();
		        e.printStackTrace();
		        return false;
		    } finally {
		        if (stmt != null) stmt.close();
		        if (conn != null) conn.close();
		    }
		}




}
