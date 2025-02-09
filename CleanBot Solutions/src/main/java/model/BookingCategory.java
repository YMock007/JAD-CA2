package model;

import java.util.List;

public class BookingCategory {
    private int id;
    private String name;
    private List<Booking> bookings;

    public BookingCategory() {}

    public BookingCategory(int id, String name, List<Booking> bookings) {
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

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}
