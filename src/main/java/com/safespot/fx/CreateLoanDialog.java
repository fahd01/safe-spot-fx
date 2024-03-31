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

public class CreateLoanDialog extends Dialog<Loan> {

    @FXML
    private TextField amountTextField;
    @FXML private TextField interestTextField;
    @FXML private TextField termTextField;
    @FXML private TextArea purposeTextArea;
    @FXML
    private ButtonType createLoanSubmitBtn;

    private Loan loan;

    public CreateLoanDialog() {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/safespot/fx/create-loan.fxml"));
            loader.setController(this);

            DialogPane dialogPane = null;
            try { dialogPane = loader.load(); } catch (IOException e) { throw new RuntimeException(e); }
            dialogPane.lookupButton(createLoanSubmitBtn).addEventFilter(ActionEvent.ANY, this::onConnect);

            //initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);

            setResizable(true);
            setTitle("Create New Loan Request");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> loan);
            setOnShowing(dialogEvent -> Platform.runLater(() -> amountTextField.requestFocus()));
    }

    @FXML
    private void initialize() {
        ComponentsUtils.restrictNumbersOnly(amountTextField);
        ComponentsUtils.restrictNumbersOnly(interestTextField);
        ComponentsUtils.restrictNumbersOnly(termTextField);
    }

    @FXML
    private void onConnect(ActionEvent event) {
        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountTextField.getText()));
        BigDecimal interest = BigDecimal.valueOf(Double.valueOf(interestTextField.getText()));
        Integer term = Integer.valueOf(termTextField.getText());
        String purpose = purposeTextArea.getText();
        Loan loan = new Loan(amount, interest, term, purpose);
        this.loan = new LoanDaoImpl().persist(loan);
    }
}