package com.safespot.safespotfx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}

    git init & \
        git add . & \
        git commit -m "Initial commit" & \
        git branch -M main & \
        git remote add origin https://github.com/fahd01/safe-spot-fx.git & \
        git push -u origin main & \