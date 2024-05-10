package com.safespot.fx.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;

import java.io.IOException;

public class RatingController {

    @FXML
    private Rating RecRating;

    @FXML
    private Button submiter;

    @FXML
    void subrec(MouseEvent event) {


        Alert alertt = new Alert(Alert.AlertType.INFORMATION);
        alertt.setTitle("Merci pour votre avis");
        alertt.setHeaderText("Avis Pris en considération.Tu vas retourner vers Réclamation");
        alertt.showAndWait();


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/safespot/fx/Reclamation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submiter.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



















}