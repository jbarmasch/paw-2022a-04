package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
    @Size(min = 6, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    @NotEmpty
    private String username;
    
    @Size(min = 6, max = 100)
    @NotEmpty
    private String password;

    @Size(min = 6, max = 100)
    @NotEmpty
    private String repeatPassword;

    @Email
    @NotEmpty
    private String mail;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
