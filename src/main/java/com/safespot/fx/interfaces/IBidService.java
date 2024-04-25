package com.safespot.fx.interfaces;

import com.safespot.fx.models.Bid;

import java.sql.SQLException;
import java.util.List;

public interface IBidService {
    List<Bid> findAll() throws SQLException;
    List<Bid> findByLoanId(int loanId);

    Bid persist(Bid bid);
}