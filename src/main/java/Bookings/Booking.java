package Bookings;

import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

public class Booking {
    private int id;
    private int requesterId;
    
 // Constructor with all fields
    public Booking(int id, int requesterId, int providerId, int serviceId, int statusId, Date date,
                   Time timeRequested, String phNumber, String address, String postalCode, String remark) {
        this.id = id;
        this.requesterId = requesterId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.statusId = statusId;
        this.dateRequested = date;
        this.timeRequested = timeRequested;
        this.phNumber = phNumber;
        this.address = address;
        this.postalCode = postalCode;
        this.remark = remark;
    }
    
    public Booking(int requesterId, int serviceId, int statusId, Date dateRequested,
			Time timeRequested, String phNumber, String address, String postalCode, String remark) {
		this.requesterId = requesterId;
		this.serviceId = serviceId;
		this.statusId = statusId;
		this.dateRequested = dateRequested;
		this.timeRequested = timeRequested;
		this.phNumber = phNumber;
		this.address = address;
		this.postalCode = postalCode;
		this.remark = remark;
	}

	private int providerId;
    private int serviceId;
    private int statusId;
    private Date dateRequested;
    private Time timeRequested;
    private String phNumber;
    private String address;
    private String postalCode;
    private String remark;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(int requesterId) {
        this.requesterId = requesterId;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderId(int providerId) {
        this.providerId = providerId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
