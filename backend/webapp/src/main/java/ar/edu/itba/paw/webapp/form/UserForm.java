package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validations.UniqueMail;
import ar.edu.itba.paw.webapp.validations.UniqueUsername;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
    @UniqueUsername
    @Size(min = 8, max = 100, message = "{Size.userForm.username}")
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "{Pattern.userForm.username}")
    @NotEmpty(message = "{NotEmpty.userForm.username}")
    private String username;

    @Size(min = 8, max = 100, message = "{Size.userForm.password}")
    @NotEmpty(message = "{NotEmpty.userForm.password}")
    private String password;

    @Size(min = 8, max = 100, message = "{Size.userForm.repeatPassword}")
    @NotEmpty(message = "{NotEmpty.userForm.repeatPassword}")
    private String repeatPassword;

    @UniqueMail
    @Email(message = "{Email.userForm.mail}")
    @NotEmpty(message = "{NotEmpty.userForm.mail}")
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
