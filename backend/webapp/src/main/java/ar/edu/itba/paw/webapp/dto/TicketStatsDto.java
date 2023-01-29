package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.TicketStats;
import ar.edu.itba.paw.model.Tag;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TicketStatsDto {
    private String ticketName;
    private double attendance;
    private double saleRatio;
    private double price;
    private int realQty;
    private int qty;
    private double income;
    private int booked;

    public static TicketStatsDto fromTicketStats(final UriInfo uriInfo, final TicketStats ticketStats) {
        final TicketStatsDto dto = new TicketStatsDto();

        dto.ticketName = ticketStats.getTicketName();
        dto.attendance = ticketStats.getAttendance();
        dto.saleRatio = ticketStats.getSaleRatio();
        dto.price = ticketStats.getPrice();
        dto.realQty = ticketStats.getRealQty();
        dto.qty = ticketStats.getQty();
        dto.income = ticketStats.getIncome();
        dto.booked = ticketStats.getBooked();

        return dto;
    }


    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public double getAttendance() {
        return attendance;
    }

    public void setAttendance(double attendance) {
        this.attendance = attendance;
    }

    public double getSaleRatio() {
        return saleRatio;
    }

    public void setSaleRatio(double saleRatio) {
        this.saleRatio = saleRatio;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRealQty() {
        return realQty;
    }

    public void setRealQty(int realQty) {
        this.realQty = realQty;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public int getBooked() {
        return booked;
    }

    public void setBooked(int booked) {
        this.booked = booked;
    }    
}
