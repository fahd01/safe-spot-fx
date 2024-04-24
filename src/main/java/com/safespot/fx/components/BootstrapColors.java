package com.safespot.fx.components;

import javafx.scene.paint.Color;

public enum BootstrapColors {
    PRIMARY("#337ab7"),
    INFO("#d9edf7"),
    DANGER("#f2dede"),
    WARNING("#fcf8e3"),
    SUCCESS("#dff0d8");

    public final String hexValue;

    BootstrapColors(String hexValue) {
        this.hexValue = hexValue;
    }

    public Color asColor(){
        return Color.valueOf(this.hexValue);
    }
}
