package com.safespot.fx;

import com.safespot.fx.dao.LoanDaoImpl;
import com.safespot.fx.model.Loan;
import com.safespot.fx.utils.ComponentsUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

public class ErrorDialog {
    public ErrorDialog(String errorMessage) {

        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage, ButtonType.CLOSE);
        alert.showAndWait();

    }





}