package edu.esprit.user.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import edu.esprit.user.entities.User;
import edu.esprit.user.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class userList {
    @javafx.fxml.FXML
    private CardPane listUsers;
    UserService userService = new UserService();
    @FXML
    private TextField searchText;
    @FXML
    private Button tri;

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

    @FXML
    public void search(Event event) {
        String searchTerm = searchText.getText().trim().toLowerCase();
        List<User> users = userService.getAllUsers();
        List<User> searchRes = new ArrayList<>();
        for (User user:users){
            if (user.getNom().toLowerCase().contains(searchTerm))
                searchRes.add(user);
        }
        listUsers.getItems().clear();
        for (User user : searchRes) {
            listUsers.getItems().add(createUserCard(user));
        }
    }

    @FXML
    public void sort(ActionEvent actionEvent) {
        List<User> users = userService.getAllUsers();
        users.sort(Comparator.comparing(User::getEmail, Comparator.nullsFirst(String::compareTo)));
        listUsers.getItems().clear();
        for (User user : users) {
            listUsers.getItems().add(createUserCard(user));
        }
    }
}