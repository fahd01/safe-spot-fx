package edu.esprit.user.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import edu.esprit.user.entities.User;
import edu.esprit.user.services.UserService;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Objects;

public class userList {
    @javafx.fxml.FXML
    private CardPane listUsers;
    UserService userService = new UserService();

    @FXML
    public void initialize() {
        displayUsers();
    }

    private void displayUsers() {
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            listUsers.getItems().add(createUserCard(user));
        }
    }

    private Node createUserCard(User user) {
        HBox card = new HBox(10);
        Button actionButton;
        if (Objects.equals(user.getEtat(), "activer")) {
            actionButton = new Button("Ban");
            actionButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            actionButton.setOnAction(e -> userService.banUser(user.getId()));
        } else {
            actionButton = new Button("Actiate");
            actionButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
            actionButton.setOnAction(e -> userService.activateUser(user.getId()));
        }
        VBox userContainer = new VBox(10);
        userContainer.setStyle("-fx-padding: 10; -fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

        Label nameLabel = new Label("Name: " + user.getNom());
        Label emailLabel = new Label("Email: " + user.getEmail());

        userContainer.getChildren().addAll(nameLabel, emailLabel);
        card.getChildren().addAll(userContainer,actionButton);
        return card;
    }
}