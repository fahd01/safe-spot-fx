package com.safespot.fx;

import com.safespot.fx.components.LoanProgressTableCell;
import com.safespot.fx.dao.BidDao;
import com.safespot.fx.dao.BidDaoImpl;
import com.safespot.fx.dao.LoanDao;
import com.safespot.fx.dao.LoanDaoImpl;
import com.safespot.fx.model.Bid;
import com.safespot.fx.model.Loan;
import com.safespot.fx.model.LoanStatus;
import com.safespot.fx.components.ButtonGroupTableCell;
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
    @FXML private TableColumn<Loan, Double> biddingProgress;

    @FXML private TableColumn<Loan, Loan> actions;
    private BidDao bidDao=new BidDaoImpl();
    private LoanDao loanDao= new LoanDaoImpl();
    ObservableList<Loan> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createLoanBtn.setOnAction(event -> {
            new CreateLoanDialog(Optional.empty()).showAndWait().ifPresent(loan -> list.add(loan));
        });

        List<Loan> loans = new ArrayList<>();
        try { loans = loanDao.findAll(); } catch (SQLException e) { e.printStackTrace(); }
        list = FXCollections.observableArrayList(loans);

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        interest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        term.setCellValueFactory(new PropertyValueFactory<>("term"));
        biddingProgress.setCellValueFactory(new PropertyValueFactory<>("biddingProgress"));
        purpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        actions.setCellFactory(col -> {
            Button deleteButton = new Button("Delete");
            deleteButton.getStyleClass().setAll("btn","btn-danger");
            /*img = image("src/code/media/logo.png")
            button_login.setGraphic ImageView.new(img)*/
            deleteButton.setTooltip(new Tooltip("Delete this loan"));
            Button editButton = new Button("Edit");
            editButton.getStyleClass().setAll("btn","btn-info");
            editButton.setTooltip(new Tooltip("Edit this loan"));
            Button bidButton = new Button("Bid");
            bidButton.getStyleClass().setAll("btn","btn-success");
            bidButton.setTooltip(new Tooltip("Place a bid on this loan"));
            TableCell<Loan, Loan> cell = new ButtonGroupTableCell<>(bidButton, editButton, deleteButton);
            editButton.setOnAction(event ->
                new CreateLoanDialog(Optional.of(actions.getTableView().getItems().get(cell.getIndex())))
                        .showAndWait()
                        .ifPresent(
                            loan -> { if(list.contains(loan)) list.set(list.indexOf(loan), loan); else list.add(loan); }
                        )
            );
            deleteButton.setOnAction(e -> {
                Loan loan = actions.getTableView().getItems().get(cell.getIndex());
                deleteLoan(loan);
            });
            return cell ;
        });

        // TODO set biddingProgress when fetching the loan
        // TODO or use DAO within the model to provide a function that calculates the progress
        loans.stream().forEach(
                loan -> loan.setBiddingProgress(
                        bidDao.findByLoanId(loan.getId()).stream().mapToDouble(bid -> bid.getAmount().doubleValue()).sum() / loan.getAmount().doubleValue()
                )
        );

        biddingProgress.setCellFactory( LoanProgressTableCell.forTableColumn());

        table.setItems(list);
        /****/
        initializeBidsManagementTab();

    }

    public void deleteLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this loan?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                loanDao.delete(loan);
                list.remove(loan);
            }
            catch (SQLIntegrityConstraintViolationException e) {
                new WarningDialog("You can not delete this loan, it already has assigned bids!");
                throw new RuntimeException(e);
            }

            catch (SQLException e) {
                new ErrorDialog(e.getMessage());
                throw new RuntimeException(e);
            }

        }
    }

/******************* bids management section move to separate fxml / controller *******************/
// TODO separate controller and FXML for bids (check the provided workshop for loading separate views)

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
