package Reviews;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import db.DatabaseConnection;

public class ReviewList {
    // Read reviews by userId (via bookingId)
    public static List<Review> getReviewsByUserId(int userId) throws SQLException {
        List<Review> reviews = new ArrayList<>();
        String query = """
                SELECT r.*
                FROM Review r
                INNER JOIN Booking b ON r.booking_id = b.id
                WHERE b.requester_id = ?
                ORDER BY r.date_created DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("content"),
                        rs.getTimestamp("date_created"),
                        rs.getInt("booking_id")
                ));
            }
        }

        return reviews;
    }
    
    public static boolean isReviewPresentForBooking(int bookingId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Review WHERE booking_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        }
        return false;
    }

    
 // Create a new review with a check for existing bookingId
    public static boolean createReview(Review review) throws SQLException {
        String checkQuery = """
            SELECT COUNT(*) AS count
            FROM Review
            WHERE booking_id = ?
        """;

        String insertQuery = """
            INSERT INTO Review (rating, content, date_created, booking_id)
            VALUES (?, ?, CURRENT_TIMESTAMP, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Check if a review already exists for the same booking
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, review.getBookingId());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt("count") > 0) {
                    return false;
                }
            }

            // Insert the review if no existing review found
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, review.getRating());
                insertStmt.setString(2, review.getContent());
                insertStmt.setInt(3, review.getBookingId());
                return insertStmt.executeUpdate() > 0; 
            }
        }
    }


    // Read a single review by ID
    public static Review getReviewById(int reviewId) throws SQLException {
        String query = "SELECT * FROM Review WHERE id = ?";
        Review review = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reviewId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                review = new Review(
                        rs.getInt("id"),
                        rs.getInt("rating"),
                        rs.getString("content"),
                        rs.getTimestamp("date_created"),
                        rs.getInt("booking_id")
                );
            }
        }

        return review;
    }

    // Update a review
    public static boolean updateReview(Review review) throws SQLException {
        String query = "UPDATE Review SET rating = ?, content = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, review.getRating());
            stmt.setString(2, review.getContent());
            stmt.setInt(3, review.getId());

            return stmt.executeUpdate() > 0;
        }
    }

    // Delete a review by ID
    public static boolean deleteReview(int reviewId) throws SQLException {
        String query = "DELETE FROM Review WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, reviewId);
            return stmt.executeUpdate() > 0;
        }
    }
}
