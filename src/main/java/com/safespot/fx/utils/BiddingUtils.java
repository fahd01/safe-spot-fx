package com.safespot.fx.utils;

import com.safespot.fx.models.Loan;
import com.safespot.fx.services.BidService;

public class BiddingUtils {

    public static double calculateBiddingProgress(Loan loan) {
        return calculateAcceptedBids(loan) / loan.getAmount().doubleValue();
    }

    public static double calculateAcceptedBids(Loan loan) {
        return new BidService().findByLoanId(loan.getId()).stream()
                .filter(bid -> bid.getStatus().isAccepted())
                .mapToDouble(bid -> bid.getAmount().doubleValue())
                .sum();
    }

    public static double calculateRemainingBids(Loan loan) {
        return loan.getAmount().doubleValue() - calculateAcceptedBids(loan);
    }

}
