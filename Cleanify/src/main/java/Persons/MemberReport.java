package Persons;

public class MemberReport {
    private int id;
    private String postalCode;
    private String name;
    private int totalBookings;
    private String location;

    // Constructors
    public MemberReport(String postalCode, String name) {
        this.postalCode = postalCode;
        this.name = name;
    }

    public MemberReport(int id, String name, int totalBookings) {
        this.id = id;
        this.name = name;
        this.totalBookings = totalBookings;
    }
    
    public MemberReport(String name, String postalCode, String location) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.location = location;
    }


    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalBookings() { return totalBookings; }
    public void setTotalBookings(int totalBookings) { this.totalBookings = totalBookings; }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

