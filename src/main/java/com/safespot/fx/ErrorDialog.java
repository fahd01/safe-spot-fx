package com.safespot.fx;

import javafx.scene.control.*;

public class ErrorDialog {

    public ErrorDialog(String errorMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.CLOSE);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {}
    }
}