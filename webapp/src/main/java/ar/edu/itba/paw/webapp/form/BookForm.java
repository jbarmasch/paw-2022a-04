package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;

public class BookForm {
    @Size(max = 100)
    @NotEmpty
    private String name;

    @Size(max = 100)
    @NotEmpty
    private String surname;

    @Size(max = 100)
    @NotEmpty
    @Email
    private String mail;

    @Min(1)
    @NotNull
    private Integer qty;

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
