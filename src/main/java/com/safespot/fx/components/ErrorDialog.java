package com.safespot.fx.components;

import javafx.scene.control.*;

public class ErrorDialog {
    public ErrorDialog(String errorMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.CLOSE);
        alert.showAndWait();

    }





}