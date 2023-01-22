package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.UniqueMail;
import ar.edu.itba.paw.webapp.validations.UniqueUsername;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
    @Size(min = 8, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    @NotEmpty
    @UniqueUsername
    private String username;

    @Size(min = 8, max = 100)
    @NotEmpty
    private String password;

    @Size(min = 8, max = 100)
    @NotEmpty
    private String repeatPassword;

    @Email
    @NotEmpty
    @UniqueMail
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
