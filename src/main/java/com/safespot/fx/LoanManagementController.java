package com.safespot.fx;

import com.safespot.fx.dao.LoanDao;
import com.safespot.fx.dao.LoanDaoImpl;
import com.safespot.fx.model.Loan;
import com.safespot.fx.model.LoanStatus;
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
            new CreateLoanDialog().showAndWait().ifPresent(loan -> list.add(loan));
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
            TableCell<Loan, Loan> cell = new TableCell<Loan, Loan>() {
                @Override
                public void updateItem(Loan loan, boolean empty) {
                    super.updateItem(loan, empty);
                    HBox buttonGroup = new HBox(deleteButton, editButton);
                    buttonGroup.setSpacing(2.5);
                    if (empty) { setGraphic(null); } else { setGraphic(buttonGroup);}
                }
            };
            deleteButton.setOnAction(e -> {
                Loan loan = actions.getTableView().getItems().get(cell.getIndex());
                deleteLoan(loan);
            });
            return cell ;
        });

        table.setItems(list);
    }

    public void deleteLoan(Loan loan) {
        new LoanDaoImpl().delete(loan);
        list.remove(loan);
    }

    public void editLoan(Loan loan) {

    }
}
