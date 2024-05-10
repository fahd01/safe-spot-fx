package com.safespot.fx.controllers;

import com.safespot.fx.utils.SecurityUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab investmentAreaTab;
    @FXML
    private Tab lendingAreaTab;
    @FXML
    private Tab reclamationAreaTab;
    @FXML
    private Tab userManagementAreaTab;
    @FXML
    private Tab profileAreaTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (SecurityUtils.getCurrentUser().isAdmin() == false) {
            mainTabPane.getTabs().remove(userManagementAreaTab);
        }
    }
}
