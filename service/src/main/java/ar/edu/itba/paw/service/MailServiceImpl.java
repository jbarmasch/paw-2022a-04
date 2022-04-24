package ar.edu.itba.paw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;

    private void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@bottler.com");
            message.setTo(to);
            message.setBcc("santilococo.01@gmail.com");
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Mail exception: " + e.getMessage());
        }
    }

    public void sendBookMail(String userMail, String username, String eventMail, String eventName, int qty) {
        sendMail(eventMail, "BotPass - Reserva " + eventName, "El usuario " + username + " ha reservado " + qty + " entradas para " + eventName + ".");
        sendMail(userMail, "Reserva recibida", "Se ha recibido una reserva de " + qty + " entradas a nombre de " + username + " para " + eventName + ".");
    }

    public void sendCancelMail(String userMail, String username, String eventMail, String eventName, int qty) {
        sendMail(userMail, "BotPass - Cancelación " + eventName, "El usuario " + username + " ha cancelado " + qty + " entradas para " + eventName + ".");
        sendMail(eventMail, "Cancelación de reserva", "Se ha cancelado una reserva de " + qty + " entradas a nombre de " + username + " para " + eventName + ".");
    }

    @Override
    public void sendErrorMail(String userMail, String eventName) {
        sendMail(userMail, "Error reserva", "Ha habido un error al reservar entradas para " + eventName + ".");
    }
}
