package com.safespot.fx.controllers;

import com.safespot.fx.models.Don;
import com.safespot.fx.models.Investissement;
import com.safespot.fx.services.DonService;
import com.safespot.fx.services.InvestissementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class backDonForm {
    @javafx.fxml.FXML
    private RadioButton etatFalseRadioButton;
    @javafx.fxml.FXML
    private TextField montantTextField;
    @javafx.fxml.FXML
    private TextField fullnameTextField;
    @javafx.fxml.FXML
    private ComboBox investissementsComboBox;
    @javafx.fxml.FXML
    private RadioButton etatTrueRadioButton;
    @javafx.fxml.FXML
    private ToggleGroup etatToggleGroup;
    @javafx.fxml.FXML
    private TextField tauxTextField;
    Don don;
    DonService donService = new DonService();
    InvestissementService investissementService = new InvestissementService();
    @javafx.fxml.FXML
    private Button btnSave;

    @javafx.fxml.FXML
    public void initialize() {
        ObservableList<Investissement> investissements = FXCollections.observableArrayList(investissementService.liste());
        investissementsComboBox.setItems(investissements);
    }
    public void setObjectToSend(Don obj) {
        this.don = obj;
        tauxTextField.setText(String.valueOf(don.getTaux()));
        fullnameTextField.setText(don.getFullname());
        montantTextField.setText(String.valueOf(don.getMontant()));
        investissementsComboBox.setValue(investissementService.recherche(don.getId()));
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

        don.setFullname(fullnameTextField.getText());
        don.setInvestissementsId(((Investissement) investissementsComboBox.getValue()).getId());
        don.setMontant(Double.parseDouble(montantTextField.getText()));
        don.setTaux(Double.parseDouble(tauxTextField.getText()));
        don.setEtat(etatToggleGroup.getSelectedToggle() == etatTrueRadioButton);
        donService.modifier(don);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Operation Completed");
        infoAlert.setHeaderText("Donation saved successfully.");
        infoAlert.showAndWait();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/application.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.show();
    }
    public Map<String, String> verifyInputs() {
        Map<String, String> validationErrors = new HashMap<>();

        // Check if the fullname is empty
        if (fullnameTextField.getText() == null || fullnameTextField.getText().trim().isEmpty()) {
            validationErrors.put("fullname", "Please enter the full name.");
        }

        // Check if an investment is selected
        if (investissementsComboBox.getSelectionModel().isEmpty()) {
            validationErrors.put("investissementsId", "Please select an investment.");
        }

        // Check if the montant (amount) is a valid number
        try {
            double montant = Double.parseDouble(montantTextField.getText());
            if (montant <= 0) {
                validationErrors.put("montant", "The amount must be a positive number.");
            }
        } catch (NumberFormatException e) {
            validationErrors.put("montant", "The amount is invalid. Please enter a number.");
        }
        try {
            double taux = Double.parseDouble(tauxTextField.getText());
            // Assuming taux is a percentage, it should be between 0 and 100
            if (taux < 0 || taux > 100) {
                validationErrors.put("taux", "The rate must be between 0 and 100.");
            }
        } catch (NumberFormatException e) {
            validationErrors.put("taux", "The rate is invalid. Please enter a decimal number.");
        }

        // Check if the state is selected and which one
        if (etatToggleGroup.getSelectedToggle() == null) {
            validationErrors.put("etat", "Please select the state.");
        }
        return validationErrors;
    }
}