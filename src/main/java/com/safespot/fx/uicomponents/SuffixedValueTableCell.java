package com.safespot.fx.uicomponents;

import javafx.scene.control.TableCell;

public class SuffixedValueTableCell<S, T> extends TableCell<S, T> {

    private String unit;
    public SuffixedValueTableCell(String unit) {
        super();
        this.unit = unit;
    }

    @Override
    public void updateItem(T value, boolean empty) {
        super.updateItem(value, empty);
        if (empty || value == null)
            setText("");
        else
            setText(String.format("%s %s", value,  unit));
    }
}
