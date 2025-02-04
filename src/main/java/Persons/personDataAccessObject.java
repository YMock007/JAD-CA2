package Persons;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.DatabaseConnection;
import handlingPassword.passwordHashing;

public class personDataAccessObject {
	
	 public static void createPerson(Person person) throws SQLException {
	        String query = "INSERT INTO person (name, password, email, phnumber, address, postalcode, role_id, is_google_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        try (Connection connection = DatabaseConnection.getConnection(); 
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, person.getEmail());
	            statement.setString(2, person.getPassword());
	            statement.setString(3, person.getName());
	            statement.setString(4, person.getPhNumber());
	            statement.setString(5, person.getAddress());
	            statement.setString(6, person.getPostalCode());
	            statement.setInt(7, person.getRoleId());
	            statement.setBoolean(8, person.getIsGoogleUser());

	            statement.executeUpdate();
	        }
	    }
	 
	 
	 public static Person validatePerson(String email, String plainPassword) throws SQLException {
		    String query = "SELECT * FROM person WHERE email = ?";
		    try (Connection connection = DatabaseConnection.getConnection();
			         PreparedStatement statement = connection.prepareStatement(query)) {

			        statement.setString(1, email);

			        try (ResultSet resultSet = statement.executeQuery()) {
			            if (resultSet.next()) {
			            	return new Person(
			            		    resultSet.getInt("id"),
			            		    resultSet.getString("name"),
			            		    resultSet.getString("password"), 
			            		    resultSet.getString("email"),
			            		    resultSet.getString("phnumber"),
			            		    resultSet.getString("address"),
			            		    resultSet.getString("postalcode"),
			            		    resultSet.getInt("role_id"),
			            		    resultSet.getBoolean("is_google_user")
			            		);
			            }
			        }
			    }
		    return null; 
		}

	 
	 
	 public static void updatePerson(int id, String name, String phoneNumber, String address, String postalCode, String password) throws SQLException {
		    String query = "UPDATE person SET name = ?, phnumber = ?, address = ?, postalcode = ?, password = ? WHERE id = ?";
		    try (
		    	Connection connection = DatabaseConnection.getConnection();
		        PreparedStatement statement = connection.prepareStatement(query)) {
		        statement.setString(1, name);
		        statement.setString(2, phoneNumber);
		        statement.setString(3, address);
		        statement.setString(4, postalCode);
		        statement.setString(5, password);
		        statement.setInt(6, id);

		        statement.executeUpdate();
		    }
		}
	 
	 
	 public static void deletePerson(int id) throws SQLException {
		    String query = "DELETE FROM person WHERE id = ?";
		    try (Connection connection = DatabaseConnection.getConnection();
		         PreparedStatement statement = connection.prepareStatement(query)) {
		        statement.setInt(1, id);
		        statement.executeUpdate();
		    }
		}
	 
	 
	 public static boolean isEmailRegistered(String email) throws SQLException {
	        String query = "SELECT COUNT(*) FROM person WHERE email = ?";
	        try (Connection connection = DatabaseConnection.getConnection(); 
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, email);

	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    return resultSet.getInt(1) > 0; 
	                }
	            }
	        }
	        return false;
	    }


	 public static Person getPersonByEmail(String email) throws SQLException {
		    String query = "SELECT * FROM person WHERE email = ?";
		    try (Connection connection = DatabaseConnection.getConnection();
			         PreparedStatement statement = connection.prepareStatement(query)) {

			        statement.setString(1, email);

			        try (ResultSet resultSet = statement.executeQuery()) {
			            if (resultSet.next()) {
			            	return new Person(
			            		    resultSet.getInt("id"),
			            		    resultSet.getString("name"),
			            		    resultSet.getString("password"), 
			            		    resultSet.getString("email"),
			            		    resultSet.getString("phnumber"),
			            		    resultSet.getString("address"),
			            		    resultSet.getString("postalcode"),
			            		    resultSet.getInt("role_id"),
			            		    resultSet.getBoolean("is_google_user")
			            		);
			            }
			        }
			    }
		    return null;
		}

	 
	 
	 
	 public static boolean updatePerson(Person person) throws SQLException {
		    String sql = "UPDATE person SET name = ?, phnumber = ?, address = ?, postalCode = ? WHERE email = ?";
		    
		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {
		        
		        stmt.setString(1, person.getName());
		        stmt.setString(2, person.getPhNumber());
		        stmt.setString(3, person.getAddress());
		        stmt.setString(4, person.getPostalCode());
		        stmt.setString(5, person.getEmail());

		        return stmt.executeUpdate() > 0;
		    }
		}



	 public static boolean checkPassword(String email, String oldPassword) throws SQLException {
		    String sql = "SELECT password FROM person WHERE email = ?";
		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setString(1, email);
		        try (ResultSet rs = stmt.executeQuery()) {
		            if (rs.next()) {
		                String storedPassword = rs.getString("password");
		                return passwordHashing.checkPassword(oldPassword, storedPassword);
		            }
		        }
		    }
		    return false;
		}

	 public static boolean updatePassword(String email, String hashedPassword) throws SQLException {
		    String sql = "UPDATE person SET password = ? WHERE email = ?";
		    try (Connection conn = DatabaseConnection.getConnection();
		         PreparedStatement stmt = conn.prepareStatement(sql)) {
		        stmt.setString(1, hashedPassword);
		        stmt.setString(2, email);
		        return stmt.executeUpdate() > 0;
		    }
		}

	 
	 

}
