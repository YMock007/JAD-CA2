package cleanify_ws.cleanify_ws.dto;



public class CompletedBookingDTO {
    private int id;
    private String dateRequested;
    private String timeRequested;
    private String address;
    private CompletedServiceDTO service; // ✅ Using CompletedServiceDTO

    // ✅ Constructor Fix - Add Missing Constructor
    public CompletedBookingDTO(int id, String dateRequested, String timeRequested, String address, CompletedServiceDTO service) {
        this.id = id;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.service = service;
    }

    public int getId() { return id; }
    public String getDateRequested() { return dateRequested; }
    public String getTimeRequested() { return timeRequested; }
    public String getAddress() { return address; }
    public CompletedServiceDTO getService() { return service; }
}
