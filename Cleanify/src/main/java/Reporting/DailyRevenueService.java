package Reporting;

public class DailyRevenueService {
    private String day;
    private double revenue;

    public DailyRevenueService(String day, double revenue) {
        this.day = day;
        this.revenue = revenue;
    }

    public String getDay() {
        return day;
    }

    public double getRevenue() {
        return revenue;
    }
}

