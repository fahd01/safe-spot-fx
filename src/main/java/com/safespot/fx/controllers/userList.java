package com.safespot.fx.controllers;

import com.gluonhq.charm.glisten.control.CardPane;
import com.safespot.fx.models.User;
import com.safespot.fx.services.UserService;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class userList {
    @FXML
    private CardPane listUsers;
    UserService userService = new UserService();
    @FXML
    private TextField searchText;
    @FXML
    private Button tri;
    @FXML
    private Button triPrenom;
    @FXML
    private Button triNom;
    @FXML
    private Button triTel;

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
        Label prenomLabel = new Label("Prenom: " + user.getPrenom());
        Label telLabel = new Label("Tel: " + user.getNumTlph());
        Label emailLabel = new Label("Email: " + user.getEmail());

        userContainer.getChildren().addAll(nameLabel, prenomLabel, emailLabel, telLabel);
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

    @FXML
    public void sortPrenom(ActionEvent actionEvent) {
        List<User> users = userService.getAllUsers();
        users.sort(Comparator.comparing(User::getPrenom, Comparator.nullsFirst(String::compareTo)));
        listUsers.getItems().clear();
        for (User user : users) {
            listUsers.getItems().add(createUserCard(user));
        }
    }

    @FXML
    public void sortNom(ActionEvent actionEvent) {
        List<User> users = userService.getAllUsers();
        users.sort(Comparator.comparing(User::getNom, Comparator.nullsFirst(String::compareTo)));
        listUsers.getItems().clear();
        for (User user : users) {
            listUsers.getItems().add(createUserCard(user));
        }
    }

    @FXML
    public void sortTel(ActionEvent actionEvent) {
        List<User> users = userService.getAllUsers();
        users.sort(Comparator.comparing(User::getNumTlph, Comparator.nullsFirst(String::compareTo)));
        listUsers.getItems().clear();
        for (User user : users) {
            listUsers.getItems().add(createUserCard(user));
        }
    }
}