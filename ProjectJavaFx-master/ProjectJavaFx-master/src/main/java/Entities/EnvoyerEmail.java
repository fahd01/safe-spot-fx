/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 *
 * @author TECHN
 */
public class EnvoyerEmail {



    public static void envoyer(String destinataire, int code) throws MessagingException {

        String username = "moatazben1@gmail.com";
        String password ="ziziland123";
        System.out.println("Entrain d'envoyer un email de vérification !! ");
        // Etape 1 : Création de la session
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");

        Session session = Session.getInstance(props,new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);}
        });

        Message message = prepareMessage(session,username,destinataire,code);
        Transport.send(message);
        System.out.println("Message envoyé !!");
    }



    private static Message prepareMessage(Session session, String username,String destinataire, int code) throws MessagingException {

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinataire));
            message.setSubject("Code de confirmation de test");

            message.setText("Votre code de votre Test est le suivant: "+code+"\nVotre Tests n'est pas accessible sans ce code de vérification.");
            return message;
        } catch (AddressException ex) {
            Logger.getLogger(EnvoyerEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
