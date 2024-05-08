package edu.esprit.user.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import edu.esprit.user.entities.Don;
import edu.esprit.user.entities.Investissement;
import edu.esprit.user.services.DonService;
import edu.esprit.user.services.InvestissementService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class frontDonListe {
    @javafx.fxml.FXML
    private TextField searchTextField;
    @javafx.fxml.FXML
    private RadioButton etatFalseRadioButton;
    @javafx.fxml.FXML
    private CardPane listDons;
    @javafx.fxml.FXML
    private Button btnSave;
    @javafx.fxml.FXML
    private TextField montantTextField;
    @javafx.fxml.FXML
    private Button btnListeInv;
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
    DonService donService = new DonService();
    InvestissementService investissementService = new InvestissementService();

    @javafx.fxml.FXML
    public void initialize() {
        ObservableList<Investissement> investissements = FXCollections.observableArrayList(investissementService.liste());
        investissementsComboBox.setItems(investissements);
        List<Don> dons = donService.liste();
        for (Don don : dons) {
            listDons.getItems().add(createDonCard(don));
        }
    }

    private Node createDonCard(Don don) {
        VBox donContainer = new VBox(10);
        donContainer.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

        Label nameLabel = new Label("User: " + don.getFullname());
        Label emailLabel = new Label("Amount: " + don.getMontant());

        donContainer.getChildren().addAll(nameLabel, emailLabel);
        HBox donCard = new HBox(10);
        Button edit = new Button("Edit");
        edit.setOnAction(event -> {
            fullnameTextField.setText(don.getFullname());
            investissementsComboBox.setValue(investissementService.recherche(don.getInvestissementsId()));
            montantTextField.setText(String.valueOf(don.getMontant()));
            tauxTextField.setText(String.valueOf(don.getTaux()));
            btnSave.setText("Modifier");
            btnSave.setOnAction(event1 -> {
                try {
                    saveModif(event1, don);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        });
        donCard.getChildren().addAll(donContainer,edit);
        return donCard;
    }

    @javafx.fxml.FXML
    public void gotoListInv(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/frontInvListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    public void saveModif(ActionEvent actionEvent,Don don) throws IOException {
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

        String fullname = fullnameTextField.getText();
        Investissement investissement = (Investissement) investissementsComboBox.getValue();
        double montant = Double.parseDouble(montantTextField.getText());
        double taux = Double.parseDouble(tauxTextField.getText());
        boolean etat = etatToggleGroup.getSelectedToggle() == etatTrueRadioButton;
        Don newDon = new Don(fullname, investissement.getId(), taux, montant, etat);
        newDon.setId(don.getId());
        donService.modifier(newDon);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Operation Completed");
        infoAlert.setHeaderText("Donation modified successfully.");
        infoAlert.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/frontDonListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
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

        String fullname = fullnameTextField.getText();
        Investissement investissement = (Investissement) investissementsComboBox.getValue();
        double montant = Double.parseDouble(montantTextField.getText());
        double taux = Double.parseDouble(tauxTextField.getText());
        boolean etat = etatToggleGroup.getSelectedToggle() == etatTrueRadioButton;
        Don newDon = new Don(fullname, investissement.getId(), taux, montant, etat);
        donService.ajout(newDon);
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Operation Completed");
        infoAlert.setHeaderText("Donation saved successfully.");
        infoAlert.showAndWait();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/esprit/user/gui/frontDonListe.fxml"));
        Parent root = loader.load();
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.setScene(new Scene(root));
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
    @javafx.fxml.FXML
    public void search(Event event) {
        String searchTerm = searchTextField.getText().trim().toLowerCase();
        List<Don> investissements = donService.liste();
        List<Don> searchRes = new ArrayList<>();
        for (Don d:investissements){
            if (d.getFullname().toLowerCase().contains(searchTerm))
                searchRes.add(d);
        }
        listDons.getItems().clear();
        for (Don d : searchRes) {
            listDons.getItems().add(createDonCard(d));
        }
    }
}