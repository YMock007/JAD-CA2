package Services;

public class ServiceReport extends Service {
    private double avgRating; // Store the computed rating

    // Constructor without unnecessary fields
    public ServiceReport(int id, String name, float price, int categoryId, double avgRating) {
        super(id, name, null, price, categoryId, null, 0); // Passing null/0 for removed fields
        this.avgRating = avgRating;
    }

    // Getters
    public double getAvgRating() {
        return avgRating;
    }
}
