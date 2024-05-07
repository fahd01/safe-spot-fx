package com.safespot.fx.uicomponents;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tooltip;

public class LoanProgressBar extends ProgressBar {

    public LoanProgressBar() {
        super();
    }

    public LoanProgressBar(double progress) {
        super(progress);
        this.updateStyle();

        this.progressProperty().addListener((observable, oldValue, newValue) -> this.updateStyle());
    }

    public void updateProgress(double progress) {
        super.setProgress(progress);
        this.updateStyle();
    }

    public void updateStyle() {
        double progress = this.getProgress();
        String color = BootstrapColors.SUCCESS.hexValue;
        if(progress < 0.4){
            color = BootstrapColors.DANGER.hexValue;
        }else if(progress < 0.8){
            color = BootstrapColors.WARNING.hexValue;
        }
        this.setStyle(String.format("-fx-accent: %s;", color));
        this.setTooltip(new Tooltip(String.format("%.2f %%", progress * 100)));
    }
}
