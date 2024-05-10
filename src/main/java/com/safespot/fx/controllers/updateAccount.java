package com.safespot.fx.controllers;

import com.safespot.fx.models.User;
import com.safespot.fx.services.UserService;
import com.safespot.fx.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class updateAccount
{
    @javafx.fxml.FXML
    private Button updatePasswordbtn;
    @javafx.fxml.FXML
    private Button editbtn;
    @javafx.fxml.FXML
    private Button logoutbtn;
    @javafx.fxml.FXML
    private TextField adresse;
    @javafx.fxml.FXML
    private Text fullName;
    @javafx.fxml.FXML
    private Text adrMail;
    @javafx.fxml.FXML
    private TextField nom;
    @javafx.fxml.FXML
    private TextField prenom;
    @javafx.fxml.FXML
    private TextField numTel;

    User user;
    UserService userService = new UserService();
    @javafx.fxml.FXML
    public void initialize() {
        user = SessionManager.getInstance().getCurrentUser();
        nom.setText(user.getNom());
        prenom.setText(user.getPrenom());
        numTel.setText(user.getNumTlph());
        adrMail.setText(user.getEmail());
        adresse.setText(user.getAdresse());
        fullName.setText(user.getNom()+" "+user.getPrenom());
    }

    @javafx.fxml.FXML
    public void logout(ActionEvent actionEvent) throws IOException {
        SessionManager.getInstance().logoutUser();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/login.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setHeight(500);
        stage.setWidth(500);
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void updatePassword(ActionEvent actionEvent) {
    }

    @javafx.fxml.FXML
    public void edit(ActionEvent actionEvent) {
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
        user.setNom(nom.getText());
        user.setPrenom(prenom.getText());
        user.setAdresse(adresse.getText());
        user.setNumTlph(numTel.getText());
        userService.updateAccount(user);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Welcome");
        alert.setHeaderText("Account updated successfully.");
        alert.showAndWait();
    }
    public Map<String, String> verifyInputs() {
        Map<String, String> validationErrors = new HashMap<>();

        // Check if the name fields are empty
        if (nom.getText() == null || nom.getText().isEmpty()) {
            validationErrors.put("nom", "Please enter a last name.");
        }
        if (prenom.getText() == null || prenom.getText().isEmpty()) {
            validationErrors.put("prenom", "Please enter a first name.");
        }

        // Check if the address is empty
        if (adresse.getText() == null || adresse.getText().isEmpty()) {
            validationErrors.put("adresse", "Please enter an address.");
        }

        // Check if the telephone number is empty or invalid
        Pattern phonePattern = Pattern.compile("^\\d{8}$"); // Adjust regex to suit your needs
        if (numTel.getText() == null || numTel.getText().isEmpty()) {
            validationErrors.put("tel", "Please enter a telephone number.");
        } else if (!phonePattern.matcher(numTel.getText()).matches()) {
            validationErrors.put("tel", "Telephone number is invalid.");
        }

        return validationErrors;
    }
}