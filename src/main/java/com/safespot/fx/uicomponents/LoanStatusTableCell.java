package com.safespot.fx.uicomponents;

import com.safespot.fx.models.Loan;
import com.safespot.fx.models.LoanStatus;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class LoanStatusTableCell extends TableCell<Loan, LoanStatus> {

    @Override
    protected void updateItem(LoanStatus status, boolean empty) {
        super.updateItem(status, empty);
        this.setAlignment(Pos.CENTER);
        if (!empty) {
            Label label = new Label(status.toString());

            if (status.equals(LoanStatus.IN_BIDDING)){
                label.getStyleClass().addAll("lbl", "lbl-info");
            } else if (status.equals(LoanStatus.ACTIVE)) {
                label.getStyleClass().addAll("lbl", "lbl-warning");
            } else
                label.getStyleClass().addAll("lbl", "lbl-success");

            setGraphic(label);
        }

    }
}