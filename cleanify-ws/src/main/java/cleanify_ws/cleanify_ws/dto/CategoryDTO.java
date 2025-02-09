package cleanify_ws.cleanify_ws.dto;

import java.util.List;

public class CategoryDTO {
    private int id;
    private String name;
    private List<BookingDTO> bookings;

    public CategoryDTO(int id, String name, List<BookingDTO> bookings) {
        this.id = id;
        this.name = name;
        this.bookings = bookings;
    }

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
		this.name = name;
	}

	public List<BookingDTO> getBookings() {
		return bookings;
	}

	public void setBookings(List<BookingDTO> bookings) {
		this.bookings = bookings;
	}


}
