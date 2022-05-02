package ar.edu.itba.paw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private TemplateEngine htmlTemplateEngine;

    private void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("botpass@zohomail.com");
            message.setTo(to);
            message.setBcc("botpass@zohomail.com");
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Mail exception: " + e.getMessage());
        }
    }

    public void sendSimpleMail(final String recipientName, final String recipientEmail) {
        final Context ctx = new Context();
        ctx.setVariable("name", recipientName);

        try {
            final MimeMessage mimeMessage = emailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject("Example HTML email (simple)");
            message.setFrom("botpass@zohomail.com");
            message.setTo(recipientEmail);
            final String htmlContent = htmlTemplateEngine.process("mail", ctx);
            message.setText(htmlContent, true);
            emailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Mail exception: " + e.getMessage());
        }
    }

    @Async
    @Override
    public void sendBookMail(String userMail, String username, String eventMail, String eventName, int qty) {
//        sendSimpleMail(eventMail, userMail);
        sendMail(eventMail, "BotPass - Reserva " + eventName, "El usuario " + username + " ha reservado " + qty + " entradas para " + eventName + ".");
        sendMail(userMail, "Reserva recibida", "Se ha recibido una reserva de " + qty + " entradas a nombre de " + username + " para " + eventName + ".");
    }

    @Async
    @Override
    public void sendCancelMail(String userMail, String username, String eventMail, String eventName, int qty) {
        sendMail(userMail, "BotPass - Cancelación " + eventName, "El usuario " + username + " ha cancelado " + qty + " entradas para " + eventName + ".");
        sendMail(eventMail, "Cancelación de reserva", "Se ha cancelado una reserva de " + qty + " entradas a nombre de " + username + " para " + eventName + ".");
    }

    @Async
    @Override
    public void sendErrorMail(String userMail, String eventName) {
        sendMail(userMail, "Error reserva", "Ha habido un error al reservar entradas para " + eventName + ".");
    }
}
