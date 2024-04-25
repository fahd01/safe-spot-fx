package com.safespot.fx.model;

import java.math.BigDecimal;

public class Bid {

    private int id ;
    private BigDecimal amount ;
    private BidStatus status = BidStatus.PENDING;
    private int bidderId;
    private int loanId;
    private Integer automationId;

    public Bid() {}

    public Bid(int id, BigDecimal amount, BidStatus status, int bidderId, int loanId, int automationId) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.bidderId = bidderId;
        this.loanId = loanId;
        this.automationId = automationId;
    }

    public Bid(BigDecimal amount, int bidderId, int loanId) {
        this.amount = amount;
        this.status = BidStatus.PENDING;
        this.bidderId = bidderId;
        this.loanId = loanId;
        this.automationId = null;
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

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public int getBidderId() {
        return bidderId;
    }

    public void setBidderId(int bidderId) {
        this.bidderId = bidderId;
    }

    public int getLoanId() {
        return loanId;
    }

    public void setLoanId(int loanId) {
        this.loanId = loanId;
    }

    public Integer getAutomationId() {
        return automationId;
    }

    public void setAutomationId(Integer automationId) {
        this.automationId = automationId;
    }
}
