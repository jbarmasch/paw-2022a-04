package ar.edu.itba.paw.webapp.form;

public class BouncerBookForm {
    private boolean confirmed;

    public BouncerBookForm() {}

    public BouncerBookForm(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
