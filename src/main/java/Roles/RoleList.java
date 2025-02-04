package Roles;

import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleList {
    private static List<Role> roles = new ArrayList<>();
    private static boolean isLoaded = false;

    public static List<Role> getRoles() {
        if (!isLoaded) {
            loadRolesFromDatabase();
        }
        return roles;
    }

    private static void loadRolesFromDatabase() {
        roles = new ArrayList<>();
        String query = "SELECT * FROM Role";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Role role = new Role(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                roles.add(role);
            }
            isLoaded = true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addRole(Role role) {
        String query = "INSERT INTO Role (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role.getName());
            stmt.executeUpdate();
            isLoaded = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRole(Role role) {
        String query = "UPDATE Role SET name = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, role.getName());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();
            isLoaded = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteRole(int roleId) {
        String query = "DELETE FROM Role WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roleId);
            stmt.executeUpdate();
            isLoaded = false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
