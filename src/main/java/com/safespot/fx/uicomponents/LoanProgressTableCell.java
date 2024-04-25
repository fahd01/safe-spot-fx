package com.safespot.fx.uicomponents;

import com.safespot.fx.models.Loan;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.util.Callback;

public class LoanProgressTableCell extends ProgressBarTableCell<Loan> {

    @Override
    public void updateItem(Double progress, boolean empty) {
        super.updateItem(progress, empty);
        if(progress != null && !empty){
            String color = BootstrapColors.SUCCESS.hexValue;
            if(progress < 0.4){
                color = BootstrapColors.DANGER.hexValue;
            }else if(progress < 0.8){
                color = BootstrapColors.WARNING.hexValue;
            }
            setStyle(String.format("-fx-accent: %s;", color));
            setTooltip(new Tooltip(String.format("%.2f %%", progress * 100)));
        }
    }

    public static Callback<TableColumn<Loan, Double>, TableCell<Loan, Double>> forTableColumn() {
        return param -> new LoanProgressTableCell();
    }
}
