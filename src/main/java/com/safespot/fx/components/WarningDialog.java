package com.safespot.fx.components;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class WarningDialog {
    public WarningDialog(String warning) {

        Alert alert = new Alert(Alert.AlertType.WARNING, warning, ButtonType.CLOSE);
        alert.showAndWait();

    }





}