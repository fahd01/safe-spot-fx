package com.safespot.fx.controllers;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.interfaces.ILoanService;
import com.safespot.fx.models.Bid;
import com.safespot.fx.models.BidStatus;
import com.safespot.fx.models.Loan;
import com.safespot.fx.models.User;
import com.safespot.fx.services.BidService;
import com.safespot.fx.services.LoanService;
import com.safespot.fx.uicomponents.BootstrapFxColors;
import com.safespot.fx.uicomponents.StatusLabel;
import com.safespot.fx.utils.SecurityUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyLoansController implements Initializable {

    @FXML
    private StackPane myLoansStackPane;
    @FXML
    private TilePane myLoansTilePane;

    private final ILoanService loanService = new LoanService();
    private final IBidService bidService = new BidService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User currentUser = SecurityUtils.getCurrentUser();
        List<Loan> loans = loanService.findByBorrowerId(currentUser.getId());
        List<Node> tiles = loans.stream().map(this::renderLoanDetails).toList();
        myLoansTilePane.getChildren().addAll(tiles);
    }

    private Node renderLoanDetails(Loan loan) {
        VBox loanView = new VBox();
        loanView.getChildren().add(renderKeyValue("Amount", loan.getAmount().toString() + " TND"));
        loanView.getChildren().add(renderKeyValue("Interest", loan.getInterest().toString() + " %"));
        loanView.getChildren().add(renderKeyValue("Term", loan.getTerm() + " months"));
        loanView.getChildren().add(new HBox(5, new Label("Status"), new StatusLabel(loan.getStatus())));
        loanView.getChildren().add(renderKeyValue("Purpose", loan.getPurpose()));
        List<Bid> bids = bidService.findByLoanId(loan.getId());
        loanView.getChildren().add(renderLoanBids(bids));

        loanView.setPadding(new Insets(5, 5, 5, 5));
        loanView.setBorder(
                new Border(
                        new BorderStroke(
                                Color.rgb(201, 201, 201),
                                BorderStrokeStyle.SOLID,
                                new CornerRadii(5),
                                BorderWidths.DEFAULT
                        )
                )
        );
        loanView.setBackground(
                new Background(
                        new BackgroundFill(
                                Color.WHITE,
                                CornerRadii.EMPTY,
                                Insets.EMPTY
                        )
                )
        );
        return loanView;
    }

    private Node renderKeyValue(String name, String value) {
        HBox hBox = new HBox(5);
        hBox.getChildren().add(new Label(name+": "));
        hBox.getChildren().add(new Label(value));
        return hBox;
    }

    private Node renderLoanBids(List<Bid> bids){
        ListView listView = new ListView<>();
        listView.setPrefHeight(300);
        List<Node> items = bids.stream().map(this::renderBidsListItem).toList();
        listView.getItems().addAll(items);
        TitledPane titledPane = new TitledPane("Offered Bids", listView);
        return titledPane;
    }

    private Node renderBidsListItem(Bid bid) {
        HBox hBox = new HBox(5);
        hBox.getChildren().add(new Label(bid.getAmount().toString() + " TND"));
        hBox.getChildren().add(new StatusLabel(bid.getStatus()));
        Button accept = new Button();
        accept.setGraphic(FontIcon.of(FontAwesome.CHECK, 16, BootstrapFxColors.SUCCESS.color));
        accept.setTooltip(new Tooltip("Accept this bid"));
        Button reject = new Button();
        reject.setGraphic(FontIcon.of(FontAwesome.TRASH, 16, BootstrapFxColors.DANGER.color));
        reject.setTooltip(new Tooltip("Reject this bid"));
        hBox.getChildren().add(accept);
        hBox.getChildren().add(reject);

        if (bid.getStatus().equals(BidStatus.ACTIVE) || bid.getStatus().equals(BidStatus.PAID)){
            accept.setDisable(true);
            reject.setDisable(true);
        } else if (bid.getStatus().equals(BidStatus.REJECTED)){
            reject.setDisable(true);
        } else if (bid.getStatus().equals(BidStatus.APPROVED)){
            accept.setDisable(true);
        }

        accept.setOnAction(event -> {

        });

        return hBox;
    }
}
