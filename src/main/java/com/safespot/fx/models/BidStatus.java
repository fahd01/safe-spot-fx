package com.safespot.fx.models;

import com.safespot.fx.interfaces.Status;

public enum BidStatus implements Status {
    PENDING,APPROVED,REJECTED,ACTIVE,PAID;

    @Override
    public String getThemeStyle() {
        if (this.equals(PENDING))
            return "info";
        if (this.equals(APPROVED))
            return "success";
        if (this.equals(REJECTED))
            return "danger";
        if (this.equals(ACTIVE))
            return "warning";
        return "secondary";
    }
}
