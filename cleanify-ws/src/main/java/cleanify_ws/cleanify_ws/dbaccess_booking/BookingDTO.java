package cleanify_ws.cleanify_ws.dbaccess_booking;

import java.sql.Date;
import java.sql.Time;

public class BookingDTO {
    private int id;
    private String serviceName;
    private Date dateRequested;
    private Time timeRequested;
    private String address;
    private String postalCode;
    private String remark;
    private String requesterName;  
    private String requesterPhone; 

    // Constructor for showing available bookings (without user info)
    public BookingDTO(int id, String serviceName, Date dateRequested, Time timeRequested, 
                      String address, String postalCode, String remark) {
        this.id = id;
        this.serviceName = serviceName;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.postalCode = postalCode;
        this.remark = remark;
    }

    // Constructor for showing accepted bookings (with user info)
    public BookingDTO(int id, String serviceName, Date dateRequested, Time timeRequested, 
                      String address, String postalCode, String remark, 
                      String requesterName, String requesterPhone) {
        this.id = id;
        this.serviceName = serviceName;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.postalCode = postalCode;
        this.remark = remark;
        this.requesterName = requesterName;
        this.requesterPhone = requesterPhone;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Date getDateRequested() {
        return dateRequested;
    }

    public void setDateRequested(Date dateRequested) {
        this.dateRequested = dateRequested;
    }

    public Time getTimeRequested() {
        return timeRequested;
    }

    public void setTimeRequested(Time timeRequested) {
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

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getRequesterPhone() {
        return requesterPhone;
    }

    public void setRequesterPhone(String requesterPhone) {
        this.requesterPhone = requesterPhone;
    }
}
