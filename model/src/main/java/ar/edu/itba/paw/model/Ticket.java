package ar.edu.itba.paw.model;

public class Ticket {
    private Integer id;
    private String ticketName;
    private Double price;
    private Integer qty;

    public Ticket() {}

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

    public Ticket(Integer id, String name) {
        this.id = id;
        this.ticketName = name;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
