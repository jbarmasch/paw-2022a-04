package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.Date;
import ar.edu.itba.paw.webapp.validations.DateOrder;
import ar.edu.itba.paw.webapp.validations.Future;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@DateOrder
public class TicketForm {
    private int id;
    
    @NotEmpty(message = "{NotEmpty.ticketForm.ticketName}")
    private String ticketName;

    @NotNull(message = "{NotNull.ticketForm.price}")
    @DecimalMin(value = "0.00", message = "{DecimalMin.ticketForm.price}")
    private Double price;

    @Min(value = 1, message = "{Min.ticketForm.qty}")
    private Integer qty;

    @Date
    private String starting;

    @Future
    @Date
    private String until;

    @Min(value = 1, message = "{Min.ticketForm.maxPerUser}")
    @Max(value = 10, message = "{Max.ticketForm.maxPerUser}")
    @NotNull(message = "{NotNull.ticketForm.maxPerUser}")
    private Integer maxPerUser;

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Integer getMaxPerUser() {
        return maxPerUser;
    }

    public void setMaxPerUser(Integer maxPerUser) {
        this.maxPerUser = maxPerUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
