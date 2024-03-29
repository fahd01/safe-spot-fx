package com.safespot.fx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {

        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
