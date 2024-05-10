package com.safespot.fx.controllers;

import com.safespot.fx.models.User;
import com.safespot.fx.services.ResetPasswordService;
import com.safespot.fx.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class resetPW
{
    @javafx.fxml.FXML
    private Button loginBtn;
    @javafx.fxml.FXML
    private Text signUpbtn;
    @javafx.fxml.FXML
    private TextField emailInput;
    @javafx.fxml.FXML
    private Button mailBtn;

    ResetPasswordService resetPasswordService = new ResetPasswordService();
    UserService userService = new UserService();
    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void sendMail(ActionEvent actionEvent) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create authenticator
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("ploutos.connecte@gmail.com", "eprfxkbgketkgezf");            }
        };

        // Create session
        Session session = Session.getDefaultInstance(properties, authenticator);

        try {
            // Create a new MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("your-email@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailInput.getText()));
            message.setSubject("Password Reset Request");
            User u = userService.getUserByEmail(emailInput.getText());
            if (u!=null){
                String token = resetPasswordService.saveToken(u.getId());
                // Load the HTML template
                String htmlContent = loadHtmlTemplate(u.getNom(), token);  // Implement this method to load and replace placeholders in your HTML template
                message.setContent(htmlContent, "text/html");

                // Send the message
                Transport.send(message);
                System.out.println("Mail sent successfully.");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String loadHtmlTemplate(String username, String token) {
        // Normally, you might load this from a file or other source
        String template = "<html><body><h2>Password Reset Request</h2>"
                + "<p>Hello " + username + ",</p>"
                + "<p>You have requested to reset your password. Please use the following token to reset your password:</p>"
                + "<p><strong>Token: " + token + "</strong></p>"
                + "<p>If you did not request this, please ignore this email.</p></body></html>";
        return template;
    }

    @javafx.fxml.FXML
    public void gotoLogin(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/login.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void signUp(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/signup.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}