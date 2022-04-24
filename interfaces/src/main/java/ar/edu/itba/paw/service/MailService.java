package ar.edu.itba.paw.service;

public interface MailService {
    void sendBookMail(String userMail, String username, String eventMail, String eventName, int qty);
    void sendCancelMail(String userMail, String username, String eventMail, String eventName, int qty);
    void sendErrorMail(String userMail, String eventName);
}
