package com.safespot.fx.uicomponents;

import javafx.scene.paint.Color;

public enum BootstrapFxColors {
    PRIMARY(Color.rgb(40, 96, 144)),
    DANGER(Color.rgb(198, 47, 43)),
    SUCCESS(Color.rgb(68, 151, 68));

    public final Color color;

    BootstrapFxColors(Color color) {
        this.color = color;
    }

}
