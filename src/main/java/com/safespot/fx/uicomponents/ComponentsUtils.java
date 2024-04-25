package com.safespot.fx.uicomponents;

import javafx.scene.control.TextField;

public class ComponentsUtils {

    public static void restrictNumbersOnly(TextField textField) {
        textField.textProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (!newValue.matches("|[-\\+]?|[-\\+]?\\d+\\.?|[-\\+]?\\d+\\.?\\d+")){
                        textField.setText(oldValue);
                    }
            }
        );
    }

}
