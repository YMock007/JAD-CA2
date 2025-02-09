package model;

public class Booking {
    private int id;
    private String dateRequested;
    private String timeRequested;
    private String address;
    private String postalCode;
    private String remark;
    private ServiceDetails service;
    private String requesterName;
    private String requesterPhone;
    private boolean accepted;  // ✅ Track if the worker accepted the job

    public Booking() {}

    public Booking(int id, String dateRequested, String timeRequested, String address, String postalCode, String remark, ServiceDetails service, String requesterName, String requesterPhone) {
        this.id = id;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.postalCode = postalCode;
        this.remark = remark;
        this.service = service;
        this.requesterName = requesterName;
        this.requesterPhone = requesterPhone;
        this.accepted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(String dateRequested) {
        this.dateRequested = dateRequested;
    }

    public String getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(String timeRequested) {
        this.timeRequested = timeRequested;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ServiceDetails getService() {
        return service;
    }

    public void setService(ServiceDetails service) {
        this.service = service;
    }

    public String getRequesterName() {
        return accepted ? requesterName : "Hidden";  // ✅ Show only if accepted
    }

    public String getRequesterPhone() {
        return accepted ? requesterPhone : "Hidden";  // ✅ Show only if accepted
    }

    public void acceptBooking() {
        this.accepted = true;
    }
}
