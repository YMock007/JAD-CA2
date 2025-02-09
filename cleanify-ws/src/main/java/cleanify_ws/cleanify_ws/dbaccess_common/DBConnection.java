package cleanify_ws.cleanify_ws.dbaccess_common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.ConfigLoader;

public class DBConnection {
	public static Connection getConnection() {
		String dbUrl = ConfigLoader.get("DATABASE_URL");
		String dbUser =ConfigLoader.get("DATABASE_USER");;
		String dbPassword = ConfigLoader.get("DATABASE_PASSWORD");;
		String dbClass = "com.mysql.jdbc.Driver";
		
		Connection connection = null;
		
		try {
			Class.forName(dbClass);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			connection = DriverManager.getConnection(dbUrl,dbUser,dbPassword);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return connection;
		
	}
}