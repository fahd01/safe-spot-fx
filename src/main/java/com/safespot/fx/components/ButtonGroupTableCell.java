package com.safespot.fx.components;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;

public class ButtonGroupTableCell<T, S> extends TableCell<T, S> {
    private Button[] buttons;
    public ButtonGroupTableCell(Button... buttons) {
        this.buttons = buttons;
    }

    @Override
    public void updateItem(S item, boolean empty) {
        super.updateItem(item, empty);
        HBox buttonGroup = new HBox(buttons);
        buttonGroup.setSpacing(2.5);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(buttonGroup);
        }
    }
}
