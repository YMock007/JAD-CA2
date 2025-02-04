package Persons;

import Saved.Saved;
import Saved.SavedList;
import db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Person {
    private int id;
    private String name;
    private String password;
    private String email;
    private String phNumber;
    private String address;
    private String postalCode;
    private int roleId;
    private Boolean isGoogleUser;
    private List<Saved> SavedItems;
    private boolean hasUpdate = true;
    private boolean isLoaded = false;
    
    // Constructor for creating a new person (without ID)
    public Person(String name, String password, String email, String phNumber, String address, String postalCode, int roleId, Boolean isGoogleUser) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phNumber = phNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.roleId = roleId;
        this.isGoogleUser = isGoogleUser;
        this.SavedItems = new ArrayList<>();
    }
    
    // Constructor for an existing person (with ID)
    public Person(int id, String name, String password, String email, String phNumber, String address, String postalCode, int roleId, Boolean isGoogleUser) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phNumber = phNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.roleId = roleId;
        this.isGoogleUser = isGoogleUser;
        this.SavedItems = new ArrayList<>();
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.length() < 2 || name.length() > 50) {
            throw new IllegalArgumentException("Name must be between 2 and 50 characters.");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email;
    }

    public String getPhNumber() {
        return phNumber;
    }

    public void setPhNumber(String phNumber) {
        this.phNumber = phNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Boolean getIsGoogleUser() {
        return isGoogleUser;
    }

    public void setIsGoogleUser(Boolean isGoogleUser) {
        this.isGoogleUser = isGoogleUser;
    }
    public List<Saved> getSavedItems() {
        if(!isLoaded || hasUpdate) {
        	SavedList.getAllSavedItems(this);
            updateAndLoad();
        }
        return this.SavedItems;
    }
    
    public void addSavedItem(Saved savedItem){
    	this.SavedItems.add(savedItem);
    }
    
    public void clearSavedItems() {
        if (SavedItems != null) {
            SavedItems.clear();
        } else {
            SavedItems = new ArrayList<>();
        }
    }

    
    public String getRoleName() {
        String roleName = null;
        String query = "SELECT name FROM Role WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roleName = rs.getString("name");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roleName != null ? roleName : "Unknown Role";
    }
    public void updateAndLoad(){
    	isLoaded = true;
        hasUpdate = false;
    }
    
    public void hasUpdate() {
    	isLoaded = false;
        hasUpdate = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person person = (Person) obj;
        return id == person.id &&
               roleId == person.roleId &&
               Objects.equals(name, person.name) &&
               Objects.equals(email, person.email) &&
               Objects.equals(phNumber, person.phNumber) &&
               Objects.equals(address, person.address) &&
               Objects.equals(postalCode, person.postalCode);
    }
}
