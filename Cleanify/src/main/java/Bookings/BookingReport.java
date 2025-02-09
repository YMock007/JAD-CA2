package Bookings;

public class BookingReport {
    private String label;
    private int count;
    private double value;

    // Constructor for date or service count
    public BookingReport(String label, int count) {
        this.label = label;
        this.count = count;
    }

    // Constructor for top customers
    public BookingReport(String label, double value) {
        this.label = label;
        this.value = value;
    }

    // Getters and Setters
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
