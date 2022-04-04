package ar.edu.itba.paw.service;

public interface MailService {
    void sendMail(String to, String subject, String text);
}
