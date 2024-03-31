package com.safespot.fx;

import com.safespot.fx.dao.BidDaoImpl;
import com.safespot.fx.dao.LoanDao;
import com.safespot.fx.dao.LoanDaoImpl;
import com.safespot.fx.model.Bid;
import com.safespot.fx.model.Loan;
import com.safespot.fx.model.LoanStatus;
import com.safespot.fx.utils.ButtonGroupTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoanManagementController implements Initializable {
    @FXML private Button createLoanBtn;
    @FXML private TableView<Loan> table;
    @FXML private TableColumn<Loan, Integer> id;
    @FXML private TableColumn<Loan, BigDecimal> amount;
    @FXML private TableColumn<Loan, BigDecimal> interest;
    @FXML private TableColumn<Loan, Integer> term;
    @FXML private TableColumn<Loan, String> purpose;
    @FXML private TableColumn<Loan, LoanStatus> status;
    @FXML private TableColumn<Loan, Loan> actions;

    ObservableList<Loan> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createLoanBtn.setOnAction(event -> {
            new CreateLoanDialog(Optional.empty()).showAndWait().ifPresent(loan -> list.add(loan));
        });

        List<Loan> loans = new ArrayList<>();
        try { loans = new LoanDaoImpl().findAll(); } catch (SQLException e) { e.printStackTrace(); }
        list = FXCollections.observableArrayList(loans);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        actions.setCellFactory(col -> {
            Button deleteButton = new Button("Delete");
            Button editButton = new Button("Edit");
            Button bidButton = new Button("Bid");
            TableCell<Loan, Loan> cell = new ButtonGroupTableCell<>(bidButton, editButton, deleteButton);
            editButton.setOnAction(event -> {
                new CreateLoanDialog(Optional.of(actions.getTableView().getItems().get(cell.getIndex()))).showAndWait().ifPresent(loan -> list.add(loan));
            });
            deleteButton.setOnAction(e -> {
                Loan loan = actions.getTableView().getItems().get(cell.getIndex());
                deleteLoan(loan);
            });
            return cell ;
        });

        table.setItems(list);

        /****/
        initializeBidsManagementTab();
    }

    public void deleteLoan(Loan loan) {
        new LoanDaoImpl().delete(loan);
        list.remove(loan);
    }

/******************* bids management section move to separate fxml / controller *******************/
    @FXML private TableView<Bid> bidsTable;
    @FXML private TableColumn<Bid, Integer> bidId;
    @FXML private TableColumn<Bid, BigDecimal> bidAmount;
    @FXML private TableColumn<Bid, LoanStatus> bidStatus;
    @FXML private TableColumn<Bid, Integer> bidLoan;
    @FXML private TableColumn<Bid, Integer> bidBidder;
    @FXML private TableColumn<Bid, Bid> bidActions;

    ObservableList<Bid> bidsList;

    public void initializeBidsManagementTab() {
        List<Bid> bids = new ArrayList<>();
        try { bids = new BidDaoImpl().findAll(); } catch (SQLException e) { e.printStackTrace(); }
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
