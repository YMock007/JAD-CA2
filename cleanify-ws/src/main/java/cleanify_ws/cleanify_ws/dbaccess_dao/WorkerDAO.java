package cleanify_ws.cleanify_ws.dbaccess_dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cleanify_ws.cleanify_ws.dbaccess_common.DBConnection;
import cleanify_ws.cleanify_ws.dto.WorkerDTO;

public class WorkerDAO {


    // Method to check if worker exists and insert if not
    public boolean registerWorker(WorkerDTO workerDTO) throws SQLException {
    	Connection conn = null;
        

        try {
        	conn = DBConnection.getConnection();
        	String checkQuery = "SELECT COUNT(*) FROM Worker WHERE provider_id = ?";
            String insertQuery = "INSERT INTO Worker (provider_id, name) VALUES (?, ?)";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery) ;
            // Check if worker already exists
            checkStmt.setInt(1, workerDTO.getProvider_id());
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Worker already exists
            }

            // Insert new worker
            insertStmt.setInt(1, workerDTO.getProvider_id());
            insertStmt.setString(2, workerDTO.getName());
            insertStmt.executeUpdate();

            return true; // Worker registered successfully

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Registration failed due to database error
        }finally {
        	conn.close();
        }
    }
}
