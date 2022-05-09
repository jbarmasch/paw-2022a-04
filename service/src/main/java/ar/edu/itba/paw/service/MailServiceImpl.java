package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
import java.util.List;
import java.util.Locale;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private TemplateEngine htmlTemplateEngine;
    @Autowired
    private MessageSource messageSource;

    public void sendMail(String templateName, String subject, String recipientEmail, Context ctx) {
        try {
            final MimeMessage mimeMessage = emailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setSubject(subject);
            message.setFrom("botpass@zohomail.com");
            message.setTo(recipientEmail);
            final String htmlContent = htmlTemplateEngine.process(templateName, ctx);
            message.setText(htmlContent, true);
            emailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Mail exception: " + e.getMessage());
        }
    }

    public void sendBookEventMail(String username, String recipientEmail, String subject, String organizerName, String eventName, List<Booking> bookings, String ticketQty) {
        final Context ctx = new Context();
        ctx.setVariable("orgName", organizerName);
        ctx.setVariable("username", username);
        ctx.setVariable("eventName", eventName);
        ctx.setVariable("qty", ticketQty);
        ctx.setVariable("bookings", bookings);
        sendMail("bookEventMail", subject, recipientEmail, ctx);
    }


    public void sendBookUserMail(String recipientName, String recipientEmail, String subject, String eventName, List<Booking> bookings, String ticketQty) {
        final Context ctx = new Context();
        ctx.setVariable("name", recipientName);
        ctx.setVariable("eventName", eventName);
        ctx.setVariable("qty", ticketQty);
        ctx.setVariable("bookings", bookings);
        sendMail("bookUserMail", subject, recipientEmail, ctx);
    }


    public void sendCancelEventMail(String username, String recipientEmail, String subject, String organizerName, String eventName, List<Booking> bookings, String ticketQty) {
        final Context ctx = new Context();
        ctx.setVariable("orgName", organizerName);
        ctx.setVariable("username", username);
        ctx.setVariable("eventName", eventName);
        ctx.setVariable("qty", ticketQty);
        ctx.setVariable("bookings", bookings);
        sendMail("cancelEventMail", subject, recipientEmail, ctx);
    }


    public void sendCancelUserMail(String recipientName, String recipientEmail, String subject, String eventName, List<Booking> bookings, String ticketQty) {
        final Context ctx = new Context();
        ctx.setVariable("name", recipientName);
        ctx.setVariable("eventName", eventName);
        ctx.setVariable("qty", ticketQty);
        ctx.setVariable("bookings", bookings);
        sendMail("cancelUserMail", subject, recipientEmail, ctx);
    }

    @Async
    @Override
    public void sendBookMail(String userMail, String username, String eventMail, String organizerName, String eventName, List<Booking> bookings, int qty, Locale locale) {
        sendBookUserMail(username, userMail, messageSource.getMessage("mail.subjectBookingUser", null, locale), eventName, bookings, qty + "");
        sendBookEventMail(username, eventMail, messageSource.getMessage("mail.subjectBookingOrganizer", null, locale), organizerName, eventName, bookings, qty + "");
    }

    @Async
    @Override
    public void sendCancelMail(String userMail, String username, String eventMail, String organizerName, String eventName, List<Booking> bookings, int qty, Locale locale) {
        sendCancelUserMail(username, userMail, messageSource.getMessage("mail.subjectCancelUser", null, locale), eventName, bookings, qty + "");
        sendCancelEventMail(username, eventMail, messageSource.getMessage("mail.subjectCancelOrganizer", null, locale), organizerName, eventName, bookings, qty + "");
    }

    @Async
    @Override
    public void sendErrorMail(String userMail, String eventName) {
//        sendMail(userMail, "Error reserva", "Ha habido un error al reservar entradas para " + eventName + ".");
    }
}
