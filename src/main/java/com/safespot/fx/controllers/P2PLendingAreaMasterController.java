package com.safespot.fx.controllers;

import com.safespot.fx.utils.SecurityUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.net.URL;
import java.util.ResourceBundle;

import static com.safespot.fx.models.Roles.ROLE_ADMIN;
import static com.safespot.fx.models.Roles.ROLE_USER;

public class P2PLendingAreaMasterController implements Initializable {
    @FXML
    private TabPane lendingAreaTabPane;
    @FXML
    private Tab loanManagementTab;
    @FXML
    private Tab bidsManagementTab;
    @FXML
    private Tab findLoanTab;
    @FXML
    private Tab myLoansTab;
    @FXML
    private Tab aiAssistantTab;
    @FXML
    private Tab myBidsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Sets visible tabs based on whether the current logged-in user is admin or user or both
        if (SecurityUtils.getCurrentUser().getRoles().contains(ROLE_ADMIN) == false) {
            lendingAreaTabPane.getTabs().remove(loanManagementTab);
            lendingAreaTabPane.getTabs().remove(bidsManagementTab);
        }

        if (SecurityUtils.getCurrentUser().getRoles().contains(ROLE_USER) == false) {
            lendingAreaTabPane.getTabs().remove(findLoanTab);
            lendingAreaTabPane.getTabs().remove(myLoansTab);
            lendingAreaTabPane.getTabs().remove(myBidsTab);
            lendingAreaTabPane.getTabs().remove(aiAssistantTab);
        }

        lendingAreaTabPane.getTabs().forEach(
                tab -> tab.setOnSelectionChanged( event -> {
                    Tab selectedTab = (Tab) event.getSource();
                    selectedTab.getTabPane().requestLayout();
                })
        );
    }
}
