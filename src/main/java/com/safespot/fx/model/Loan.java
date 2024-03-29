package com.safespot.fx.model;

import java.math.BigDecimal;

public class Loan {
    private int id ;
   private BigDecimal amount;
    private BigDecimal interest;
    private int term;
    private String purpose;
    private LoanStatus status = LoanStatus.IN_BIDDING;

public Loan(){}
    public Loan(int id, BigDecimal amount, BigDecimal interest, int term, String purpose, LoanStatus status) {
        this.id = id;
        this.amount = amount;
        this.interest = interest;
        this.term = term;
        this.purpose = purpose;
        this.status = status;
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

    @Override
    public String toString() {
        return "Loan{" +
                "id=" + id +
                ", amount=" + amount +
                ", interest=" + interest +
                ", term=" + term +
                ", purpose='" + purpose + '\'' +
                ", status=" + status +
                '}';
    }
}
