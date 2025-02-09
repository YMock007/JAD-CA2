package model;




public class CompletedBookingDTO {
    private int id;
    private String dateRequested;
    private String timeRequested;
    private String address;
    private CompletedServiceDTO service;

    // ✅ Add a No-Arg Constructor (Required for JSON Deserialization)
    public CompletedBookingDTO() {}

    public CompletedBookingDTO(int id, String dateRequested, String timeRequested, String address, CompletedServiceDTO service) {
        this.id = id;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.service = service;
    }

    // ✅ Add Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDateRequested() { return dateRequested; }
    public void setDateRequested(String dateRequested) { this.dateRequested = dateRequested; }

    public String getTimeRequested() { return timeRequested; }
    public void setTimeRequested(String timeRequested) { this.timeRequested = timeRequested; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public CompletedServiceDTO getService() { return service; }
    public void setService(CompletedServiceDTO service) { this.service = service; }
}
