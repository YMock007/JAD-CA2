package cleanify_ws.cleanify_ws.dto;

public class BookingDTO {
    private int id;
    private String requesterName;
    private String requesterPhone;
    private String dateRequested;
    private String timeRequested;
    private String address;
    private String postalCode;
    private String remark;
    private ServiceDTO service;

    public BookingDTO(int id, String requesterName, String requesterPhone, String dateRequested, 
                      String timeRequested, String address, String postalCode, String remark, ServiceDTO service) {
        this.id = id;
        this.requesterName = requesterName;
        this.requesterPhone = requesterPhone;
        this.dateRequested = dateRequested;
        this.timeRequested = timeRequested;
        this.address = address;
        this.postalCode = postalCode;
        this.remark = remark;
        this.service = service;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public ServiceDTO getService() {
		return service;
	}

	public void setService(ServiceDTO service) {
		this.service = service;
	}



}
