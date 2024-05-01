package com.safespot.fx.controllers;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.models.Bid;
import com.safespot.fx.models.Loan;
import com.safespot.fx.models.LoanStatus;
import com.safespot.fx.services.BidService;
import com.safespot.fx.uicomponents.ButtonGroupTableCell;
import com.safespot.fx.uicomponents.ErrorDialog;
import com.safespot.fx.uicomponents.SuffixedValueTableCell;
import com.safespot.fx.uicomponents.WarningDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
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
    private IBidService bidService=new BidService();
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
        bidAmount.setCellFactory(col -> new SuffixedValueTableCell<>("TND"));
        bidStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        bidLoan.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        bidBidder.setCellValueFactory(new PropertyValueFactory<>("bidderId"));

        bidActions.setCellFactory(col -> {
            Button deleteButton = new Button("Delete");
            deleteButton.setTooltip(new Tooltip("Delete this Bid "));
            Button editButton = new Button("Edit");
            editButton.setTooltip(new Tooltip("Edit this Bid"));
            TableCell<Bid, Bid> cell = new ButtonGroupTableCell<>(editButton, deleteButton);
            deleteButton.setOnAction(e -> {

                Bid bid = bidActions.getTableView().getItems().get(cell.getIndex());
                deleteBid(bid);
            });

            return cell;
        });

        bidsTable.setItems(bidsList);
    }

    private void deleteBid(Bid bid) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this bid?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                bidService.delete(bid);
                bidsList.remove(bid);
            }

            catch (SQLException e) {
                new ErrorDialog(e.getMessage());
                throw new RuntimeException(e);
            }

        }
    }
}
