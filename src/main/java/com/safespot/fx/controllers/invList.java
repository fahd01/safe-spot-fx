package com.safespot.fx.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import com.safespot.fx.models.Investissement;
import com.safespot.fx.services.InvestissementService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class invList
{
    @javafx.fxml.FXML
    private Button add;
    @javafx.fxml.FXML
    private CardPane listInv;

    InvestissementService investissementService = new InvestissementService();
    @javafx.fxml.FXML
    public void initialize() {
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
    public void gotoAdd(ActionEvent actionEvent) {
    }
}