package Reporting;

public class MemberTrend {
    private String month;       // The month (e.g., "2025-01")
    private int activeMembers;  // The count of active members in that month

    // Constructor
    public MemberTrend(String month, int activeMembers) {
        this.month = month;
        this.activeMembers = activeMembers;
    }

    // Getters
    public String getMonth() {
        return month;
    }

    public int getActiveMembers() {
        return activeMembers;
    }
}
