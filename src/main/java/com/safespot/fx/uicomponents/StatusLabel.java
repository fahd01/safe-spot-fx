package com.safespot.fx.uicomponents;


import com.safespot.fx.interfaces.Status;
import javafx.scene.control.Label;

public class StatusLabel extends Label {

    public StatusLabel(Status status){
        super(status.toString());
        this.getStyleClass().addAll("lbl", "lbl-" + status.getThemeStyle());
    }
}
