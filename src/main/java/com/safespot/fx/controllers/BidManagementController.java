package com.safespot.fx.controllers;

import com.safespot.fx.models.Bid;
import com.safespot.fx.models.LoanStatus;
import com.safespot.fx.services.BidService;
import com.safespot.fx.uicomponents.ButtonGroupTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BidManagementController implements Initializable {

    @FXML
    private TableView<Bid> bidsTable;
    @FXML private TableColumn<Bid, Integer> bidId;
    @FXML private TableColumn<Bid, BigDecimal> bidAmount;
    @FXML private TableColumn<Bid, LoanStatus> bidStatus;
    @FXML private TableColumn<Bid, Integer> bidLoan;
    @FXML private TableColumn<Bid, Integer> bidBidder;
    @FXML private TableColumn<Bid, Bid> bidActions;

    ObservableList<Bid> bidsList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeBidsManagementTab();
    }

    public void initializeBidsManagementTab() {
        List<Bid> bids = new ArrayList<>();
        try { bids = new BidService().findAll(); } catch (SQLException e) { e.printStackTrace(); }
        bidsList = FXCollections.observableArrayList(bids);

        bidId.setCellValueFactory(new PropertyValueFactory<>("id"));
        bidAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        bidStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        bidLoan.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        bidBidder.setCellValueFactory(new PropertyValueFactory<>("bidderId"));

        bidActions.setCellFactory(col -> {
            Button deleteButton = new Button("Delete");
            Button editButton = new Button("Edit");
            TableCell<Bid, Bid> cell = new ButtonGroupTableCell<>(editButton, deleteButton);
            return cell ;
        });

        bidsTable.setItems(bidsList);
    }
}
