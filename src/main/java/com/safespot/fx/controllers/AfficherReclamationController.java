package com.safespot.fx.controllers;

import com.safespot.fx.models.Reclamation;
import com.safespot.fx.services.ReclamationService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class AfficherReclamationController {

    @FXML
    private VBox vboxReclamation;

    private final ReclamationService reclamationService = new ReclamationService();

    @FXML
    void initialize() {
        loadReclamations();
    }

    private void loadReclamations() {
        List<Reclamation> reclamations = reclamationService.afficherReclamations();
        vboxReclamation.getChildren().clear(); // Clear the VBox before adding new reclamations
        for (int i = 0; i < reclamations.size(); i += 2) {
            Reclamation reclamation1 = reclamations.get(i);
            Reclamation reclamation2 = (i + 1 < reclamations.size()) ? reclamations.get(i + 1) : null;
            HBox cardRow = createReclamationCardRow(reclamation1, reclamation2);
            vboxReclamation.getChildren().add(cardRow);
        }
    }

    private HBox createReclamationCardRow(Reclamation reclamation1, Reclamation reclamation2) {
        HBox cardRow = new HBox(20); // Spacing between cards
        cardRow.setAlignment(Pos.CENTER);
        cardRow.setPadding(new Insets(10));

        if (reclamation1 != null) {
            VBox card1 = createReclamationCard(reclamation1);
            cardRow.getChildren().add(card1);
        }
        if (reclamation2 != null) {
            VBox card2 = createReclamationCard(reclamation2);
            cardRow.getChildren().add(card2);
        }

        return cardRow;
    }

    private VBox createReclamationCard(Reclamation reclamation) {
        VBox card = new VBox(10); // Spacing between elements
        card.setStyle("-fx-padding: 15; -fx-background-color: #1679AB; -fx-background-radius: 12px;");
        card.setAlignment(Pos.CENTER); // Center the content vertically

        Label sujetLabel = new Label("Sujet: " + reclamation.getSujet());
        sujetLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 18px;");

        Label descriptionLabel = new Label("Description: " + reclamation.getDescription());
        descriptionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Label dateLabel = new Label("Date: " + reclamation.getDt());
        dateLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Label verifiedLabel = new Label("Verified: " + (reclamation.getVerified() ? "Yes" : "No"));
        verifiedLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10px;");
        deleteButton.setOnAction(event -> {
            deleteReclamation(reclamation.getId());
            loadReclamations(); // Reload the display after deletion
        });

        card.getChildren().addAll(sujetLabel, descriptionLabel, dateLabel, verifiedLabel, deleteButton);
        return card;
    }

    private void deleteReclamation(int id) {
        // Call the ReclamationService to delete the reclamation by ID
        reclamationService.supprimerReclamation(new Reclamation(id, "", "", null, false));
    }



    @FXML
    void redirectto(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/safespot/fx/Reclamation.fxml"));
        Scene scene = new Scene(root);

        // Obtenez la fenêtre actuelle
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Définissez la nouvelle scène
        stage.setScene(scene);
        stage.show();

    }

}
