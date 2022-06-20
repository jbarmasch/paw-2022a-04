package ar.edu.itba.paw.model;

public class TicketStats {
    private final String ticketName;
    private final double attendance;
    private final double saleRatio;
    private final double price;
    private final int realQty;
    private final int qty;
    private final double income;
    private final int booked;

    public TicketStats(String ticketName, double attendance, double saleRatio, double price, int realQty, int qty, double income, int booked) {
        this.ticketName = ticketName;
        this.attendance = attendance;
        this.saleRatio = saleRatio;
        this.price = price;
        this.realQty = realQty;
        this.qty = qty;
        this.income = income;
        this.booked = booked;
    }

    public String getTicketName() {
        return ticketName;
    }

    public double getAttendance() {
        return attendance;
    }

    public double getSaleRatio() {
        return saleRatio;
    }

    public double getPrice() {
        return price;
    }

    public int getRealQty() {
        return realQty;
    }

    public int getQty() {
        return qty;
    }

    public double getIncome() {
        return income;
    }

    public int getBooked() {
        return booked;
    }
}