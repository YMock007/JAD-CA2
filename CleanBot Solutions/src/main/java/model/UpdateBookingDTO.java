package model;

public class UpdateBookingDTO {
	private int booking_id;
    private int provider_id;
    private int status_id;   
    
    public int getBookingId() {
        return booking_id;
    }

    public void setBookingId(int booking_id) {
        this.booking_id = booking_id;
    }


    public int getProviderId() {
        return provider_id;
    }

    public void setProviderId(int provider_id) {
        this.provider_id = provider_id;
    }

    public int getStatusId() {
        return status_id;
    }

    public void setStatusId(int status_id) {
        this.status_id = status_id;
    }
}
