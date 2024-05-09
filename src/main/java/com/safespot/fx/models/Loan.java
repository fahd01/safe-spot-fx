package com.safespot.fx.models;

import java.math.BigDecimal;
import java.util.Objects;

public class Loan {
    private int id ;
   private BigDecimal amount;
    private BigDecimal interest;
    private int term;
    private String purpose;
    private LoanStatus status = LoanStatus.IN_BIDDING;
    private int borrowerId;

    // TODO set when fetching the loan from DB
    private Double biddingProgress;

    public Loan(){}

    public Loan(BigDecimal amount, BigDecimal interest, int term, String purpose) {
        this.amount = amount;
        this.interest = interest;
        this.term = term;
        this.purpose = purpose;
        this.status = LoanStatus.IN_BIDDING;
    }

    public Loan(int id, BigDecimal amount, BigDecimal interest, int term, String purpose, LoanStatus status, int borrowerId) {
        this.id = id;
        this.amount = amount;
        this.interest = interest;
        this.term = term;
        this.purpose = purpose;
        this.status = status;
        this.borrowerId = borrowerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public int getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(int borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Double getBiddingProgress() {
        return biddingProgress;
    }

    public void setBiddingProgress(Double biddingProgress) {
        this.biddingProgress = biddingProgress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return id == loan.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "loan: [ id=" + id +
                ", amount=" + amount + " TND" +
                ", interest=" + interest + " %" +
                ", term=" + term + " months" +
                ", purpose='" + purpose + '\'' +
                ", status=" + status +
                ']';
    }
    
}
