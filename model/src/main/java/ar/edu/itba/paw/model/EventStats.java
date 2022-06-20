package ar.edu.itba.paw.model;

public class EventStats {
    private final String eventName;
    private final int attended;
    private final int booked;
    private final double attendance;
    private final double saleRatio;
    private final double income;
    private final double expectedIncome;

    public EventStats(String eventName, int attended, int booked, double attendance, double saleRatio, double income, double expectedIncome) {
        this.eventName = eventName;
        this.attended = attended;
        this.booked = booked;
        this.attendance = attendance;
        this.saleRatio = saleRatio;
        this.income = income;
        this.expectedIncome = expectedIncome;
    }

    public String getEventName() {
        return eventName;
    }

    public int getAttended() {
        return attended;
    }

    public int getBooked() {
        return booked;
    }

    public double getAttendance() {
        return attendance;
    }

    public double getSaleRatio() {
        return saleRatio;
    }

    public double getIncome() {
        return income;
    }

    public double getExpectedIncome() {
        return expectedIncome;
    }
}