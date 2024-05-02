package com.safespot.fx.controllers;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.interfaces.ILoanService;
import com.safespot.fx.interfaces.Status;
import com.safespot.fx.uicomponents.*;
import com.safespot.fx.services.BidService;
import com.safespot.fx.services.LoanService;
import com.safespot.fx.models.Loan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.function.Predicate;

public class LoanManagementController implements Initializable {
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
    private final IBidService bidService = new BidService();
    private final ILoanService loanService = new LoanService();
    ObservableList<Loan> observableList;
    FilteredList<Loan> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createLoanBtn.setOnAction(event -> {
            new CreateLoanDialog(Optional.empty()).showAndWait().ifPresent(loan -> observableList.add(loan));
        });
        filterTf.setOnKeyReleased(event -> filteredList.setPredicate(searchPredicate(filterTf.getText())));

        List<Loan> loans = new ArrayList<>();
        try { loans = loanService.findAll(); } catch (SQLException e) { e.printStackTrace(); }
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
            Button deleteButton = new Button();
            //deleteButton.getStyleClass().setAll("btn","btn-danger", "btn-sm");
            deleteButton.setGraphic(FontIcon.of(FontAwesome.TRASH, 16, BootstrapColors.DANGER.asColor()));
            deleteButton.setTooltip(new Tooltip("Delete this loan"));
            Button editButton = new Button();
            //editButton.getStyleClass().setAll("btn","btn-info");
            editButton.setTooltip(new Tooltip("Edit this loan"));
            editButton.setGraphic(FontIcon.of(FontAwesome.EDIT, 16, BootstrapColors.SUCCESS.asColor()));
            Button bidButton = new Button();
            //bidButton.getStyleClass().setAll("btn","btn-success");
            bidButton.setTooltip(new Tooltip("Place a bid on this loan"));
            bidButton.setGraphic(FontIcon.of(FontAwesome.PLUS_CIRCLE, 16, BootstrapColors.PRIMARY.asColor()));
            TableCell<Loan, Loan> cell = new ButtonGroupTableCell<>(bidButton, editButton, deleteButton);
            editButton.setOnAction(event ->
                new CreateLoanDialog(Optional.of(actions.getTableView().getItems().get(cell.getIndex())))
                        .showAndWait()
                        .ifPresent(
                            loan -> { if(observableList.contains(loan)) observableList.set(observableList.indexOf(loan), loan); else observableList.add(loan); }
                        )
            );
            deleteButton.setOnAction(e -> {
                Loan loan = actions.getTableView().getItems().get(cell.getIndex());
                deleteLoan(loan);
            });
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

    public void deleteLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this loan?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                loanService.delete(loan);
                observableList.remove(loan);
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
    // TODO move to service layer
    private Double fetchBiddingProgress(Loan loan){
        return bidService.findByLoanId(loan.getId()).stream().mapToDouble(bid -> bid.getAmount().doubleValue()).sum() / loan.getAmount().doubleValue();
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
