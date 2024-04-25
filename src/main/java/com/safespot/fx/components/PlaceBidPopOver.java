package com.safespot.fx.components;

import com.safespot.fx.dao.BidDao;
import com.safespot.fx.dao.BidDaoImpl;
import com.safespot.fx.model.Bid;
import com.safespot.fx.model.Loan;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.math.BigDecimal;

public class PlaceBidPopOver extends PopOver {

    private BidDao bidDao = new BidDaoImpl();

    public PlaceBidPopOver (Loan loan) {
        super();
        this.setTitle("Place a bid");
        this.setDetachable(Boolean.TRUE);
        this.setDetached(Boolean.FALSE);
        this.setHeaderAlwaysVisible(Boolean.TRUE);
        this.setAnimated(Boolean.TRUE);
        this.setCloseButtonEnabled(Boolean.TRUE);
        this.setContentNode(placeBidForm(loan));
    }

    private Node placeBidForm(Loan loan) {
        TextField amountTf = new TextField();
        amountTf.setMaxWidth(130);
        amountTf.setPromptText("Enter bid amount");
        ComponentsUtils.restrictNumbersOnly(amountTf);
        Button submitBtn = new Button("Place");
        submitBtn.setOnAction( event -> {
            BigDecimal amount = BigDecimal.valueOf(Double.valueOf(amountTf.getText()));
            // TODO use current user instead
            int currentUserId = 1;
            // TODO handle persist exceptions into an ErrorDialog
            bidDao.persist(new Bid(amount, currentUserId, loan.getId()));
            loan.setBiddingProgress(
                    bidDao.findByLoanId(loan.getId()).stream().mapToDouble(bid -> bid.getAmount().doubleValue()).sum() / loan.getAmount().doubleValue()
            );
            this.hide();
        });
        VBox vBox = new VBox(amountTf, submitBtn);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }
}
