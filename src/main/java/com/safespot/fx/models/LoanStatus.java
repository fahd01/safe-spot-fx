package com.safespot.fx.models;

import com.safespot.fx.interfaces.Themeable;

public enum LoanStatus implements Themeable {
      IN_BIDDING, ACTIVE, PAID;

      @Override
      public String getThemeStyle() {
            return null;
      }
}
