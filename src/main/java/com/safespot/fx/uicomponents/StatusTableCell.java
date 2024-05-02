package com.safespot.fx.uicomponents;

import com.safespot.fx.interfaces.Status;
import com.safespot.fx.models.Loan;
import com.safespot.fx.models.LoanStatus;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;

public class StatusTableCell<T> extends TableCell<T, Status> {

    @Override
    protected void updateItem(Status status, boolean empty) {
        super.updateItem(status, empty);
        this.setAlignment(Pos.CENTER);
        if (!empty) {
            Label label = new Label(status.toString());
            label.getStyleClass().addAll("lbl", "lbl-" + status.getThemeStyle());
            setGraphic(label);
        } else {
            //setText(null);
            setGraphic(null);
        }

    }
}