package com.safespot.fx.controllers;

import com.safespot.fx.integrations.ExcelExporter;
import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.interfaces.ILoanService;
import com.safespot.fx.models.*;
import com.safespot.fx.services.BidService;
import com.safespot.fx.services.LoanService;
import com.safespot.fx.uicomponents.*;
import com.safespot.fx.utils.BiddingUtils;
import com.safespot.fx.utils.SecurityUtils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class MyLoansController implements Initializable {

    private static final String LOAN_ID_PROPERTY_NAME = "loandId";

    @FXML
    private StackPane myLoansStackPane;
    @FXML
    private TilePane myLoansTilePane;
    @FXML
    private Button reportButton;

    private final ILoanService loanService = new LoanService();
    private final IBidService bidService = new BidService();

    private ObservableList<Loan> observableList;
    protected ObservableList<Node> observableTiles;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        render();

        reportButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            File file = fileChooser.showSaveDialog(myLoansStackPane.getScene().getWindow());
            if (file != null) {
                try {
                    ExcelExporter.exportToExcel("Loans Report", file.getAbsolutePath());
                } catch (IOException e) {
                    new ErrorDialog("An error occured while generating the financial report for your loans");
                    throw new RuntimeException(e);
                }
            }
        });


        /*
        User currentUser = SecurityUtils.getCurrentUser();
        List<Loan> loans = loanService.findByBorrowerId(currentUser.getId());
        observableList = FXCollections.observableArrayList(loans);
        observableList.addListener(
                (ListChangeListener<? super Loan>) event -> {
                    myLoansTilePane.getChildren().clear();
                    myLoansTilePane.getChildren().addAll(FXCollections.observableArrayList(loans.stream().map(this::renderLoanDetails).toList()));
                }
        );
        List<Node> tiles = loans.stream().map(this::renderLoanDetails).toList();
        observableTiles = FXCollections.observableArrayList(tiles);
        myLoansTilePane.getChildren().addAll(tiles);
         */
    }

    private void render(){
        User currentUser = SecurityUtils.getCurrentUser();
        List<Loan> loans = loanService.findByBorrowerId(currentUser.getId());
        List<Node> tiles = loans.stream().map(this::renderLoanDetails).toList();
        myLoansTilePane.getChildren().clear();
        myLoansTilePane.getChildren().addAll(tiles);
    }

    private Node loanManagementButtons(Loan loan) {
        Button edit = new Button();
        edit.setGraphic(FontIcon.of(FontAwesome.EDIT, 16, BootstrapFxColors.SUCCESS.color));
        edit.setTooltip(new Tooltip("Edit this laon"));
        Button delete = new Button();
        delete.setGraphic(FontIcon.of(FontAwesome.TRASH, 16, BootstrapFxColors.DANGER.color));
        delete.setTooltip(new Tooltip("Delete this loan"));
        HBox buttons = new HBox(5, edit, delete);
        buttons.setAlignment(Pos.TOP_RIGHT);
        delete.setOnAction( event -> deleteLoan(loan));
        edit.setOnAction(event ->
                new CreateLoanDialog(Optional.of(loan))
                        .showAndWait()
                        .ifPresent(
                                updatedLoan -> render()//{ if(observableList.contains(updatedLoan)) observableList.set(observableList.indexOf(loan), updatedLoan); else observableList.add(updatedLoan); }
                        )
        );
        if (!loan.getStatus().equals(LoanStatus.IN_BIDDING)){
            edit.setDisable(true);
            delete.setDisable(true);
        }
        return buttons;
    }

    private Node renderLoanDetails(Loan loan) {
        VBox loanView = new VBox();
        loanView.getChildren().add(loanManagementButtons(loan));
        loanView.getChildren().add(renderKeyValue("Amount", loan.getAmount().toString() + " TND"));
        loanView.getChildren().add(renderKeyValue("Interest", loan.getInterest().toString() + " %"));
        loanView.getChildren().add(renderKeyValue("Term", loan.getTerm() + " months"));
        loanView.getChildren().add(new HBox(5, new Label("Status:"), new StatusLabel(loan.getStatus())));

        double biddingProgress = BiddingUtils.calculateBiddingProgress(loan);
        LoanProgressBar progressBar = new LoanProgressBar();
        progressBar.updateProgress(biddingProgress);
        progressBar.setPrefWidth(200);
        loanView.getChildren().add(new HBox(5, new Label("Progress:"), progressBar));

        loanView.getChildren().add(renderKeyValue("Purpose", loan.getPurpose()));
        List<Bid> bids = bidService.findByLoanId(loan.getId());
        // TODO render bids as listView or GridView: decide
        //loanView.getChildren().add(renderLoanBids(bids));
        loanView.getChildren().add(renderLoanBidsAsGrid(loan, bids));

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
        loanView.getProperties().put(LOAN_ID_PROPERTY_NAME, loan.getId());
        return loanView;
    }

    private Node renderKeyValue(String name, String value) {
        HBox hBox = new HBox(5);
        hBox.getChildren().add(new Label(name+": "));
        hBox.getChildren().add(new Label(value));
        return hBox;
    }

    private Node renderLoanBidsAsGrid(Loan loan, List<Bid> bids){
        GridPane gridView = new GridPane();
        gridView.setHgap(5);
        AtomicInteger row = new AtomicInteger();
        bids.forEach( bid -> {
            gridView.add(new Label(bid.getAmount().toString() + " TND"), 0, row.get());
            gridView.add(new StatusLabel(bid.getStatus()), 1, row.get());
            Button accept = new Button();
            accept.setGraphic(FontIcon.of(FontAwesome.CHECK, 16, BootstrapFxColors.SUCCESS.color));
            accept.setTooltip(new Tooltip("Accept this bid"));
            Button reject = new Button();
            reject.setGraphic(FontIcon.of(FontAwesome.TRASH, 16, BootstrapFxColors.DANGER.color));
            reject.setTooltip(new Tooltip("Reject this bid"));
            HBox buttons = new HBox(5, accept, reject);
            gridView.add(buttons, 2, row.get());
            row.getAndIncrement();

            if (loan.getStatus().equals(LoanStatus.ACTIVE) || loan.getStatus().equals(LoanStatus.PAID)) {
                accept.setDisable(true);
                reject.setDisable(true);
            } else {
                if (bid.getStatus().equals(BidStatus.ACTIVE) || bid.getStatus().equals(BidStatus.PAID)) {
                    accept.setDisable(true);
                    reject.setDisable(true);
                } else if (bid.getStatus().equals(BidStatus.REJECTED)) {
                    reject.setDisable(true);
                } else if (bid.getStatus().equals(BidStatus.APPROVED)) {
                    accept.setDisable(true);
                }
            }
            accept.setOnAction(event -> {
                double remainingOnBids = BiddingUtils.calculateRemainingBids(loan);
                if (remainingOnBids <= bid.getAmount().doubleValue())
                    bid.setAmount(BigDecimal.valueOf(remainingOnBids));
                bid.setStatus(BidStatus.APPROVED);
                bidService.update(bid);

                if (BiddingUtils.calculateRemainingBids(loan) == 0) {
                    loan.setStatus(LoanStatus.ACTIVE);
                    loanService.activate(loan.getId());
                    bidService.findByLoanId(loan.getId())
                            .stream()
                            .filter(item -> item.getStatus().equals(BidStatus.APPROVED))
                            .forEach(item -> bidService.activateBid(item.getId()));
                }
                render();
            });
            reject.setOnAction(event -> {
                bidService.rejectBid(bid.getId());
                render();
            });
        });

        TitledPane titledPane = new TitledPane("Offered Bids", gridView);
        return titledPane;
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


    // TODO remove duplicated code
    public void deleteLoan(Loan loan) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this loan?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                loanService.delete(loan);
                render();
                //observableList.remove(loan);
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
}
