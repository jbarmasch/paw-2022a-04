package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.Date;
import ar.edu.itba.paw.webapp.validations.Future;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

public class TicketForm {
    private int id;
    @NotEmpty
    private String ticketName;
    @NotNull
    @DecimalMin("0.00")
    private Double price;
    @Min(1)
    private Integer qty;

    @Future
    @Date
    private String starting;

    @Future
    @Date
    private String until;

    public String getTicketName() {
        return ticketName;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQty() {
        return qty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getStarting() {
        return starting;
    }

    public void setStarting(String starting) {
        this.starting = starting;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public LocalDateTime getLocalDate(String date) {
        if (Objects.equals(date, ""))
            return null;
        return LocalDateTime.parse(date);
    }
}
