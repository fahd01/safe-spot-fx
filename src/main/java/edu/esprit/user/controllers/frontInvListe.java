package edu.esprit.user.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import edu.esprit.user.entities.Don;
import edu.esprit.user.entities.Investissement;
import edu.esprit.user.entities.User;
import edu.esprit.user.services.InvestissementService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class frontInvListe
{
    @javafx.fxml.FXML
    private ColorPicker colorInput;
    @javafx.fxml.FXML
    private CardPane listDons;
    @javafx.fxml.FXML
    private TextField imageInput;
    @javafx.fxml.FXML
    private TextField searchTextField;
    @javafx.fxml.FXML
    private TextArea descriptionInput;
    @javafx.fxml.FXML
    private TextField dureeInput;
    @javafx.fxml.FXML
    private TextField nameInput;
    @javafx.fxml.FXML
    private DatePicker dateInput;
    @javafx.fxml.FXML
    private TextField emailInput;
    @javafx.fxml.FXML
    private Button btnSave;
    @javafx.fxml.FXML
    private Button btnListeInv;
    @javafx.fxml.FXML
    private TextField prixInput;
    InvestissementService investissementService = new InvestissementService();
    @javafx.fxml.FXML
    public void initialize() {
        List<Investissement> invs = investissementService.liste();
        for (Investissement inv : invs) {
            listDons.getItems().add(createInvCard(inv));
        }
    }
    private Node createInvCard(Investissement inv) {
        VBox donContainer = new VBox(10);
        donContainer.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

        Label nameLabel = new Label("User: " + inv.getName());
        Label emailLabel = new Label("Description: " + inv.getDescription());

        donContainer.getChildren().addAll(nameLabel, emailLabel);
        return donContainer;
    }
    @javafx.fxml.FXML
    public void save(ActionEvent actionEvent) throws IOException {
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

        Date date = Date.valueOf(dateInput.getValue());
        int duree = Integer.parseInt(dureeInput.getText());
        double prix = Double.parseDouble(prixInput.getText());
        String description = descriptionInput.getText();
        String email = emailInput.getText();
        String name = nameInput.getText();
        String image = imageInput.getText();
        String color = "#" + colorInput.getValue().toString().substring(2, 8);

        // Create the Investissement object
        Investissement newInvestissement = new Investissement(date, duree, prix, description, email, name, image, color);

        investissementService.ajout(newInvestissement);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Operation Completed");
        infoAlert.setHeaderText("Investissement saved successfully.");
        infoAlert.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/frontInvListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @javafx.fxml.FXML
    public void gotoDons(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/frontDonListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public Map<String, String> verifyInputs() {
        Map<String, String> validationErrors = new HashMap<>();

        // Validate the date (assuming a date must be selected)
        if (dateInput.getValue() == null) {
            validationErrors.put("date", "Please select a date.");
        }

        // Validate the duration (assuming it should be a non-negative integer)
        if (dureeInput.getText() == null || dureeInput.getText().isEmpty()) {
            validationErrors.put("duree", "Please enter a duration.");
        } else {
            try {
                int duree = Integer.parseInt(dureeInput.getText());
                if (duree < 0) {
                    validationErrors.put("duree", "Duration cannot be negative.");
                }
            } catch (NumberFormatException e) {
                validationErrors.put("duree", "Duration must be an integer.");
            }
        }

        // Validate the price (assuming it should be a non-negative decimal)
        if (prixInput.getText() == null || prixInput.getText().isEmpty()) {
            validationErrors.put("prix", "Please enter a price.");
        } else {
            try {
                double prix = Double.parseDouble(prixInput.getText());
                if (prix < 0) {
                    validationErrors.put("prix", "Price cannot be negative.");
                }
            } catch (NumberFormatException e) {
                validationErrors.put("prix", "Price must be a decimal.");
            }
        }

        // Validate the description (assuming it cannot be empty)
        if (descriptionInput.getText() == null || descriptionInput.getText().isEmpty()) {
            validationErrors.put("description", "Please enter a description.");
        }

        // Validate the email (assuming it should be a valid email format)
        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (emailInput.getText() == null || emailInput.getText().isEmpty()) {
            validationErrors.put("email", "Please enter an email address.");
        } else if (!emailPattern.matcher(emailInput.getText()).matches()) {
            validationErrors.put("email", "Email address is invalid.");
        }

        // Validate the name (assuming it cannot be empty)
        if (nameInput.getText() == null || nameInput.getText().isEmpty()) {
            validationErrors.put("name", "Please enter a name.");
        }

        // Validate the image URL (if required, otherwise this can be omitted or modified)
        // Assuming the image URL should not be empty
        if (imageInput.getText() == null || imageInput.getText().isEmpty()) {
            validationErrors.put("image", "Please enter an image URL.");
        }

        // Validate the color (assuming it must be selected, can be omitted if not needed)
        // No need to validate color as ColorPicker cannot be 'empty'

        return validationErrors;
    }

    @javafx.fxml.FXML
    public void search(Event event) {
        String searchTerm = searchTextField.getText().trim().toLowerCase();
        List<Investissement> investissements = investissementService.liste();
        List<Investissement> searchRes = new ArrayList<>();
        for (Investissement i:investissements){
            if (i.getName().toLowerCase().contains(searchTerm))
                searchRes.add(i);
        }
        listDons.getItems().clear();
        for (Investissement i : searchRes) {
            listDons.getItems().add(createInvCard(i));
        }
    }
}