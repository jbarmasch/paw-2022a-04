package ar.edu.itba.paw.model;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
    private Integer id;
    private String ticketName;
    private Double price;
    private Integer qty;

    public Ticket(String ticketName, Double price, Integer qty) {
        this.ticketName = ticketName;
        this.price = price;
        this.qty = qty;
    }

    public Ticket(Integer id, String ticketName, Double price, Integer qty) {
        this.id = id;
        this.ticketName = ticketName;
        this.price = price;
        this.qty = qty;
    }

    public String getTicketName() {
        return ticketName;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    static public List<Ticket> getTickets(Array ids, Array names, Array prices, Array lefts) throws SQLException {
        List<Ticket> tickets = new ArrayList<>();
        Integer[] idsAux = (Integer[]) ids.getArray();
        String[] namesAux = (String[]) names.getArray();
        Double[] pricesAux = (Double[]) prices.getArray();
        Integer[] leftsAux = (Integer[]) lefts.getArray();
        if (idsAux[0] != null && namesAux[0] != null && pricesAux[0] != null && leftsAux[0] != null) {
            for (int i = 0; i < idsAux.length; i++) {
                tickets.add(new Ticket(idsAux[i], namesAux[i], pricesAux[i], leftsAux[i]));
            }
        }
        return tickets;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
