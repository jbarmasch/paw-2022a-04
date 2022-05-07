package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TicketForm {
    private Integer id;
    @NotEmpty
    private String ticketName;
    @NotNull
    @DecimalMin("0.00")
    private Double price;
    @Min(1)
    private Integer qty;

    public TicketForm() {}

    public TicketForm(Integer id, String ticketName, Double price, Integer qty) {
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
}
