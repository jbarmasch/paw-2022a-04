package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.EventBooking;
import ar.edu.itba.paw.model.TicketBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.MailException;
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
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private CodeService codeService;

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

    public void sendMailWithImage(String templateName, String subject, String recipientEmail, String code, Context ctx) {
        try {
            final MimeMessage mimeMessage = emailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true,"UTF-8");
            message.setSubject(subject);
            message.setFrom("botpass@zohomail.com");
            message.setTo(recipientEmail);
            final String htmlContent = htmlTemplateEngine.process(templateName, ctx);
            message.setText(htmlContent, true);

            final InputStreamSource imageSource = new ByteArrayResource(codeService.createQr(code));
            message.addInline("image", imageSource, "image/png");

            emailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            throw new RuntimeException("Mail exception: " + e.getMessage());
        }
    }

    public void sendBookEventMail(String recipientEmail, String subject, EventBooking booking) {
        final Context ctx = new Context();
        ctx.setVariable("booking", booking);
        sendMail("bookEventMail", subject, recipientEmail, ctx);
    }


    public void sendBookUserMail(String recipientEmail, String subject, String bookingURL, EventBooking booking) {
        final Context ctx = new Context();
        ctx.setVariable("booking", booking);
        ctx.setVariable("imageResourceName", "image");
        ctx.setVariable("bookingURL", bookingURL);
        sendMailWithImage("bookUserMail", subject, recipientEmail, bookingURL, ctx);
    }

    public void sendCancelEventMail(String recipientEmail, String subject, EventBooking booking) {
        final Context ctx = new Context();
        ctx.setVariable("booking", booking);
        sendMail("cancelEventMail", subject, recipientEmail, ctx);
    }

    public void sendCancelUserMail(String recipientEmail, String subject, EventBooking booking) {
        final Context ctx = new Context();
        ctx.setVariable("booking", booking);
        sendMail("cancelUserMail", subject, recipientEmail, ctx);
    }

    public void sendCancelUserTicketMail(String recipientEmail, String subject, TicketBooking ticketBooking) {
        final Context ctx = new Context();
        ctx.setVariable("ticketbooking", ticketBooking);
        sendMail("cancelUserTicketMail", subject, recipientEmail, ctx);
    }

    @Async
    @Override
    public void sendBookMail(String bookingURL, EventBooking booking, Locale locale) {
        sendBookUserMail(booking.getUser().getMail(), messageSource.getMessage("mail.subjectBookingUser", null, locale), bookingURL, booking);
        sendBookEventMail(booking.getEvent().getOrganizer().getMail(), messageSource.getMessage("mail.subjectBookingOrganizer", null, locale), booking);
    }

    @Async
    @Override
    public void sendCancelMail(EventBooking booking, Locale locale) {
        sendCancelUserMail(booking.getUser().getMail(), messageSource.getMessage("mail.subjectCancelUser", null, locale), booking);
        sendCancelEventMail(booking.getEvent().getOrganizer().getMail(), messageSource.getMessage("mail.subjectCancelOrganizer", null, locale), booking);
    }

    @Override
    public void sendCancelTicketMail(TicketBooking ticketBooking, Locale locale) {
        sendCancelUserTicketMail(ticketBooking.getEventBooking().getUser().getMail(), messageSource.getMessage("mail.subjectCancelTicketUser", null, locale), ticketBooking);
    }

    @Async
    @Override
    public void sendErrorMail(String userMail) {
//        sendMail(userMail, "Error reserva", "Ha habido un error al reservar entradas para " + eventName + ".");
    }
}
