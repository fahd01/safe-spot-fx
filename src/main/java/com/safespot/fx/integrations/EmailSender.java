package com.safespot.fx.integrations;

import com.safespot.fx.models.Bid;
import com.safespot.fx.models.Loan;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/*
 * Create SMTP session once and reuse it therefore we used Singleton pattern to avoid creating new sessions
 */
public class EmailSender {
    private static final String NOTIFICATION_EMAIL_ADDRESS = "notifications.safe.spot@gmail.com";
    private static final String NOTIFICATION_EMAIL_PASSWORD = "safespotemailpassword";
    private static final String NOTIFICATION_EMAIL_APP_TOKEN = "vqws lotr pjtd xzao";

    private Map<String, String> smtpSessionProperties = Map.of(
            "mail.smtp.host", "smtp.gmail.com",
            "mail.smtp.port", "465",
            "mail.smtp.ssl.enable", "true",
            "mail.smtp.auth", "true"
            );

    private static EmailSender instance;
    private Session session;
    private String placedBidEmailBody;

    private EmailSender() {
        Properties properties = System.getProperties();
        properties.putAll(smtpSessionProperties);
        this.session = Session.getInstance(properties, new Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(NOTIFICATION_EMAIL_ADDRESS, NOTIFICATION_EMAIL_APP_TOKEN);
            }
        });
        try {
            this.placedBidEmailBody = Files.readString(Paths.get(getClass().getResource("bid-placed-email-body.html").toURI()));
                    //new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (URISyntaxException | IOException e) { throw new RuntimeException(e);}
    }

    public static EmailSender getInstance() {
        if(Objects.isNull(instance))
            instance = new EmailSender();
        return instance;
    }

    public void send(MimeMessage message) throws MessagingException {
        Transport.send(message);
    }

    public void send(String toEmail, String subject, String plainBody) {
        try {
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(NOTIFICATION_EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(plainBody);
            this.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendHtml(String toEmail, String subject, String htmlBody) {
        try {
            MimeMessage message = new MimeMessage(this.session);
            message.setFrom(new InternetAddress(NOTIFICATION_EMAIL_ADDRESS));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=utf-8");
            this.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPlacedBidEmail(String toEmail, Bid bid, Loan loan){
        String htmlBody = placedBidEmailBody
            .replaceAll("\\$bidAmount", bid.getAmount().toString())
            .replaceAll("\\$loanAmount", loan.getAmount().toString())
            .replaceAll("\\$loanInterest", loan.getInterest().toString())
            .replaceAll("\\$loanTerm", String.valueOf(loan.getTerm()))
            .replaceAll("\\$loanPurpose", loan.getPurpose());
        this.sendHtml(toEmail, "New Bid Placed On Your Loan", htmlBody);
    }
}