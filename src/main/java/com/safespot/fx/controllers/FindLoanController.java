package com.safespot.fx.controllers;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.interfaces.ILoanService;
import com.safespot.fx.interfaces.Status;
import com.safespot.fx.models.Loan;
import com.safespot.fx.services.BidService;
import com.safespot.fx.services.LoanService;
import com.safespot.fx.uicomponents.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class FindLoanController implements Initializable {
    @FXML private Button createLoanBtn;
    @FXML private TextField filterTf;
    @FXML private TableView<Loan> table;
    @FXML private TableColumn<Loan, Integer> id;
    @FXML private TableColumn<Loan, BigDecimal> amount;
    @FXML private TableColumn<Loan, BigDecimal> interest;
    @FXML private TableColumn<Loan, Integer> term;
    @FXML private TableColumn<Loan, String> purpose;
    @FXML private TableColumn<Loan, Status> status;
    @FXML private TableColumn<Loan, Double> biddingProgress;

    @FXML private TableColumn<Loan, Loan> actions;
    private final IBidService bidDao = new BidService();
    private final ILoanService loanDao = new LoanService();
    ObservableList<Loan> observableList;
    FilteredList<Loan> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createLoanBtn.setOnAction(event -> {
            new CreateLoanDialog(Optional.empty()).showAndWait().ifPresent(loan -> observableList.add(loan));
        });
        filterTf.setOnKeyReleased(event -> filteredList.setPredicate(searchPredicate(filterTf.getText())));

        List<Loan> loans = new ArrayList<>();
        try { loans = loanDao.findAll(); } catch (SQLException e) { e.printStackTrace(); }
        observableList = FXCollections.observableArrayList(loans);
        filteredList = new FilteredList<>(observableList);


        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amount.setCellFactory(col -> new SuffixedValueTableCell<>("TND"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        interest.setCellFactory(col -> new SuffixedValueTableCell<>("%"));
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        term.setCellFactory(col -> new SuffixedValueTableCell<>("months"));
        biddingProgress.setCellValueFactory(new PropertyValueFactory<>("biddingProgress"));
        purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        status.setCellFactory(col -> new StatusTableCell<>());

        actions.setCellFactory(col -> {
            Button bidButton = new Button("Place Bid");
            bidButton.getStyleClass().setAll("btn", "btn-primary", "btn-sm");
            bidButton.setTooltip(new Tooltip("Place a bid on this loan"));
            //bidButton.setGraphic(FontIcon.of(FontAwesome.PLUS_CIRCLE, 16, BootstrapColors.PRIMARY.asColor()));
            TableCell<Loan, Loan> cell = new ButtonGroupTableCell<>(bidButton);
            bidButton.setOnAction(e -> {
                Loan loan = actions.getTableView().getItems().get(cell.getIndex());
                PlaceBidPopOver popOver = new PlaceBidPopOver(filteredList.get(filteredList.indexOf(loan)));
                popOver.show(bidButton);
                // TODO updated the loan bidding progress then had to refresh table
                // bind data properly, getting a reference to the Observable loan passed to the table
                // should be enough to reflect POJO changes into the table view
                popOver.setOnHidden(v->table.refresh());
            });
            return cell ;
        });

        // TODO set biddingProgress when fetching the loan
        // TODO or use DAO within the model to provide a function that calculates the progress
        loans.forEach(
                loan -> loan.setBiddingProgress(fetchBiddingProgress(loan))
        );

        biddingProgress.setCellFactory( LoanProgressTableCell.forTableColumn());

        table.setItems(filteredList);
    }


    // TODO move to service layer
    private Double fetchBiddingProgress(Loan loan){
        return bidDao.findByLoanId(loan.getId()).stream().mapToDouble(bid -> bid.getAmount().doubleValue()).sum() / loan.getAmount().doubleValue();
    }

    // TODO DB filtering ??
    private Predicate<Loan> searchPredicate(String value) {
        return loan ->
                loan.getAmount().toString().contains(value) ||
                loan.getInterest().toString().contains(value) ||
                String.valueOf(loan.getTerm()).contains(value) ||
                loan.getPurpose().contains(value) ||
                loan.getStatus().toString().contains(value);
    }

}
