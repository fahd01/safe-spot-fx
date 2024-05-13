package com.safespot.fx.controllers;

import com.safespot.fx.models.User;
import com.safespot.fx.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class signup
{
    @javafx.fxml.FXML
    private TextField nomInput;
    @javafx.fxml.FXML
    private DatePicker birthInput;
    @javafx.fxml.FXML
    private Text loginBtn;
    @javafx.fxml.FXML
    private TextField adresseInput;
    @javafx.fxml.FXML
    private Button signUpbtn;
    @javafx.fxml.FXML
    private CheckBox agreeCheckBox;
    @javafx.fxml.FXML
    private PasswordField passwordInput;
    @javafx.fxml.FXML
    private TextField telInput;
    @javafx.fxml.FXML
    private TextField emailInput;
    @javafx.fxml.FXML
    private TextField prenomInput;
    UserService userService = new UserService();

    @javafx.fxml.FXML
    public void initialize() {
    }

    @javafx.fxml.FXML
    public void login(Event event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/login.fxml"));
        Parent root = loader.load();
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void signUp(ActionEvent actionEvent) {
        Map<String, String> validationErrors = verifyInputs();

        if (!validationErrors.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (String error : validationErrors.values()) {
                errorMessage.append(error).append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Please correct the following errors:");
            alert.setContentText(errorMessage.toString());
            alert.showAndWait();
            return;
        }

        User user = new User(emailInput.getText(),passwordInput.getText(),nomInput.getText(),prenomInput.getText(), Date.valueOf(birthInput.getValue()),telInput.getText(),adresseInput.getText());
        userService.signup(user);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText("Account created successfully.");
        alert.showAndWait();
    }

    public Map<String, String> verifyInputs() {
        Map<String, String> validationErrors = new HashMap<>();

        // Check if the name fields are empty
        if (nomInput.getText() == null || nomInput.getText().isEmpty()) {
            validationErrors.put("nom", "Please enter a last name.");
        }
        if (prenomInput.getText() == null || prenomInput.getText().isEmpty()) {
            validationErrors.put("prenom", "Please enter a first name.");
        }

        // Check if the address is empty
        if (adresseInput.getText() == null || adresseInput.getText().isEmpty()) {
            validationErrors.put("adresse", "Please enter an address.");
        }

        // Check if the email is valid
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (emailInput.getText() == null || emailInput.getText().isEmpty()) {
            validationErrors.put("email", "Please enter an email address.");
        } else if (!emailPattern.matcher(emailInput.getText()).matches()) {
            validationErrors.put("email", "Email address is invalid.");
        }

        // Check if the telephone number is empty or invalid
        Pattern phonePattern = Pattern.compile("^\\d{8}$"); // Adjust regex to suit your needs
        if (telInput.getText() == null || telInput.getText().isEmpty()) {
            validationErrors.put("tel", "Please enter a telephone number.");
        } else if (!phonePattern.matcher(telInput.getText()).matches()) {
            validationErrors.put("tel", "Telephone number is invalid.");
        }

        // Check if the password is empty
        if (passwordInput.getText() == null || passwordInput.getText().isEmpty()) {
            validationErrors.put("password", "Please enter a password.");
        }

        // Validate the birth date
        if (birthInput.getValue() == null) {
            validationErrors.put("birthDate", "Please select a birth date.");
        } else if (!phonePattern.matcher(telInput.getText()).matches()) {
            validationErrors.put("tel", "Telephone number is invalid.");
        }

        // Check if the terms and conditions checkbox is checked
        if (!agreeCheckBox.isSelected()) {
            validationErrors.put("terms", "You must agree to the terms and conditions.");
        }

        return validationErrors;
    }
}