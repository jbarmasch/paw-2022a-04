package ar.edu.itba.paw.model;

import javax.persistence.*;

//@Entity
//@NamedNativeQuery(name = "eventStats", query = "SELECT eventid, bookings AS attended, expectedBooked AS booked, bookings / COALESCE(NULLIF(expectedBooked, 0), 1) " +
//        "AS attendance, bookings / COALESCE(NULLIF(qty, 0), 1) AS saleRatio, income, expectedIncome FROM (SELECT eventid, SUM(realqty) AS bookings, SUM(booked) " +
//        "AS expectedBooked, SUM(qty) AS qty, SUM(price * realqty) AS income, SUM(price * booked) AS expectedIncome FROM (SELECT booked, price, " +
//        "SUM(CASE WHEN confirmed THEN tb.qty ELSE 0 END) AS realqty, t.qty AS qty, e.eventid FROM events e LEFT JOIN tickets t ON e.eventId = t.eventId LEFT " +
//        "JOIN ticketbookings tb on t.ticketId = tb.ticketId JOIN eventbookings eb on tb.id = eb.id WHERE e.eventid = :eventid " +
//        "GROUP BY t.ticketid, e.eventid) AS aux GROUP BY eventid) AS general")
//@SqlResultSetMapping(name = "eventStats", columns = {@ColumnResult(name="attended"), @ColumnResult(name="booked"), @ColumnResult(name="attendance"), @ColumnResult(name="saleRatio"), @ColumnResult(name="income"), @ColumnResult(name="expectedIncome")})
public class EventStats {
//    @Transient
//    @Id
//    @OneToOne(mappedBy = "eventid", fetch = FetchType.EAGER, optional = false)
//    private Event event;
    private String eventName;
    private int attended;
    private int booked;
    private double attendance;
    private double saleRatio;
    private double income;
    private double expectedIncome;

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

//    public List<TicketStats> getTicketStats() {
//        return ticketStats;
//    }
}