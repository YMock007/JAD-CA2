package dao;

import db.DatabaseConnection;
import model.Worker;
import handlingPassword.PasswordHashing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerDAO {
	
	public boolean isAlreadyRegistered(String email, String phone) throws SQLException {
		Connection conn = null;
        
        try{
        	conn = DatabaseConnection.getConnection();
        	String sql = "SELECT COUNT(*) FROM Worker WHERE email = ? OR phone = ?";
        	PreparedStatement pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, email);
            pstmt.setString(2, phone);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // If count > 0, worker already exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
	
	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
// handling worker sign up 
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public int insertWorker(Worker worker, int[] categoryIds) throws SQLException {
    	
    	Connection conn = null;
    	int workerId = -1;
        

        try {
        	
        	conn = DatabaseConnection.getConnection();
        	String sql = "INSERT INTO Worker (name, email, phone, password, role_id) VALUES (?, ?, ?, ?, 2) RETURNING id";
        	PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, worker.getName());
            pstmt.setString(2, worker.getEmail());
            pstmt.setString(3, worker.getPhone());
            pstmt.setString(4, PasswordHashing.hashPassword(worker.getPassword()));

            var rs = pstmt.executeQuery();
            if (rs.next()) {
                workerId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }finally {
        	conn.close();
        }

        // Insert categories if worker insertion was successful
        if (workerId != -1) {
            boolean success = insertWorkerCategories(workerId, categoryIds);
            return success ? workerId : -1;
        }
        
        return -1;
    }
    
    

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
 // handling worker sign up and worker categories
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private boolean insertWorkerCategories(int workerId, int[] categoryIds) throws SQLException {
    	
    	Connection conn = null;
        try{
        	
        	conn = DatabaseConnection.getConnection();
        	 String sql = "INSERT INTO WorkerCategory (worker_id, category_id) VALUES (?, ?)";
        	 PreparedStatement pstmt = conn.prepareStatement(sql);

            for (int categoryId : categoryIds) {
                pstmt.setInt(1, workerId);
                pstmt.setInt(2, categoryId);
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
        	conn.close();
        }
        return false;
    }
    
    
    
    
    
    
    

    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////\
///////// Worker log in
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Worker getWorkerByEmail(String email) throws SQLException {
        Connection conn = null;
        Worker worker = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT id, name, email, phone, password, role_id FROM Worker WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int workerId = rs.getInt("id");
                String name = rs.getString("name");
                String retrievedEmail = rs.getString("email");
                String phone = rs.getString("phone");
                String password = rs.getString("password");
                int roleId = rs.getInt("role_id");

                // ✅ Fetch categories separately
                int[] categoryIds = getCategoryIdsByWorkerId(workerId);

                // ✅ Use the existing Worker constructor
                worker = new Worker(workerId,name, retrievedEmail, phone, password, roleId, categoryIds);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }

        return worker;
    }
    
    private int[] getCategoryIdsByWorkerId(int workerId) throws SQLException {
        Connection conn = null;
        List<Integer> categoryList = new ArrayList<>();

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT category_id FROM WorkerCategory WHERE worker_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, workerId);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                categoryList.add(rs.getInt("category_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }

        // Convert List<Integer> to int[]
        return categoryList.stream().mapToInt(Integer::intValue).toArray();
    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public List<Worker> getAllWorkers() {
        // TODO: Implement worker retrieval
        return null;
    }

}

