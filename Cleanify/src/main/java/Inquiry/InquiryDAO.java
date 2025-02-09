package Inquiry;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;

public class InquiryDAO {

	public InquiryDAO() {
		// TODO Auto-generated constructor stub
	}
	
	public static List<Inquiry> getAllInquiries() {
        List<Inquiry> inquiries = new ArrayList<>();
        String query = "SELECT id, username, email, title, description FROM Inquiry";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Inquiry inquiry = new Inquiry(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("title"),
                        rs.getString("description")
                );
                inquiry.setId(rs.getInt("id"));
                inquiries.add(inquiry);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return inquiries;
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


}
