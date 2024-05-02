package com.safespot.fx.models;

import com.safespot.fx.interfaces.Status;

public enum LoanStatus implements Status {
      IN_BIDDING, ACTIVE, PAID;

      @Override
      public String getThemeStyle() {
            if (this.equals(LoanStatus.IN_BIDDING))
                  return "info";
            if (this.equals(LoanStatus.ACTIVE))
                  return "warning";
            return "success";
      }
}
