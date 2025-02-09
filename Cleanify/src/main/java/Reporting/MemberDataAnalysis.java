package Reporting;

import Persons.MemberReport;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Utils.PostalCodeLookup;
public class MemberDataAnalysis {


    public static List<MemberReport> getMembersByPostalCode() {
        List<MemberReport> membersByPostal = new ArrayList<>();
        String query = "SELECT postalCode, name FROM Person WHERE role_id = 2 ORDER BY postalCode";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String postalCode = rs.getString("postalCode");
                String location = PostalCodeLookup.getLocationDetails(postalCode); // Adjust country code as needed
                membersByPostal.add(new MemberReport(
                		rs.getString("name"),
                        postalCode,
                        location
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return membersByPostal;
    }

    // 📊 Most Active Members (Top 5)
    public static List<MemberReport> getMostActiveMembers() {
        List<MemberReport> mostActiveMembers = new ArrayList<>();
        String query = "SELECT p.id, p.name, COUNT(b.id) AS total_bookings " +
                       "FROM Person p " +
                       "JOIN Booking b ON p.id = b.requester_id " +
                       "WHERE p.role_id = 2 " +
                       "GROUP BY p.id ORDER BY total_bookings DESC LIMIT 5";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                mostActiveMembers.add(new MemberReport(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("total_bookings")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mostActiveMembers;
    }
}
