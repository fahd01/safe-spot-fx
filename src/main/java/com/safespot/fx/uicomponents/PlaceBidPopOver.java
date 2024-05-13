package com.safespot.fx.uicomponents;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.interfaces.IUserService;
import com.safespot.fx.services.BidService;
import com.safespot.fx.services.UserService;
import com.safespot.fx.models.Bid;
import com.safespot.fx.models.Loan;
import com.safespot.fx.models.User;
import com.safespot.fx.integrations.EmailSender;
import com.safespot.fx.utils.SecurityUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.controlsfx.control.PopOver;

import java.math.BigDecimal;

public class PlaceBidPopOver extends PopOver {

    private IBidService bidService = new BidService();
    private IUserService userService = new UserService();

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
            int currentUserId = SecurityUtils.getCurrentUser().getId();
            // TODO handle persist exceptions into an ErrorDialog
            Bid newBid = new Bid(amount, currentUserId, loan.getId());
            bidService.persist(newBid);
            this.hide();
            User loanOwner = userService.findById(loan.getBorrowerId());
            // TODO sending email blocks the main thread for a second, send async
            EmailSender.getInstance().sendPlacedBidEmail(loanOwner.getEmail(), newBid, loan);

        });
        VBox vBox = new VBox(amountTf, submitBtn);
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(5, 5, 5, 5));
        vBox.setAlignment(Pos.TOP_CENTER);
        return vBox;
    }
}
