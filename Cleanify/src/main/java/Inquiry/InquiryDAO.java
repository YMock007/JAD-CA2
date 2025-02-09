package Inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;

public class InquiryDAO {

	public InquiryDAO() {
		// TODO Auto-generated constructor stub
	}
	

    public static boolean updateInquiryStatus(int inquiryId, int status) {
        String updateQuery = "UPDATE Inquiry SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setInt(1, status);
            stmt.setInt(2, inquiryId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	public static boolean createInquiry(Inquiry reqInquiry) {
	    String insertQuery = "INSERT INTO INQUIRY(username, email, title, description) VALUES (?, ?, ?, ?)";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
	         
	        stmt.setString(1, reqInquiry.getName());
	        stmt.setString(2, reqInquiry.getEmail());
	        stmt.setString(3, reqInquiry.getTitle());
	        stmt.setString(4, reqInquiry.getDescription());

	        int rowEffected = stmt.executeUpdate();
	        return rowEffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	public static List<Inquiry> getInquiriesByEmail(String email) {
	    String selectQuery = "SELECT id, username, email, title, description, created_at FROM Inquiry WHERE email = ?";
	    List<Inquiry> inquiries = new ArrayList<>();

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
	         
	        stmt.setString(1, email);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Inquiry inquiry = new Inquiry(selectQuery, selectQuery, selectQuery);
	            inquiry.setTitle(rs.getString("title"));
	            inquiry.setDescription(rs.getString("description"));
	            inquiry.setCreatedAt((rs.getTimestamp("created_at")).toString());

	            inquiries.add(inquiry);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return inquiries;
	}
	
	public static List<Inquiry> getInquiriesAdmin() {
        String selectQuery = "SELECT id, username, email, title, description FROM Inquiry where status_id=1";
        List<Inquiry> inquiries = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectQuery);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Inquiry inquiry = new Inquiry(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("title"),
                    rs.getString("description")
                );

                inquiries.add(inquiry);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching inquiries for admin: " + e.getMessage());
            e.printStackTrace();
        }
        return inquiries;
    }
	
	public static boolean updateInquiry(int id) {
	    String updateQuery = "UPDATE Inquiry SET status_id = 2 WHERE id = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

	        stmt.setInt(1, id);
	        int rowsUpdated = stmt.executeUpdate();
	        return rowsUpdated > 0; 
	    } catch (SQLException e) {
	        System.err.println("❌ [ERROR] Failed to update inquiry: " + e.getMessage());
	        e.printStackTrace();
	        return false;
	    }
	}



}
