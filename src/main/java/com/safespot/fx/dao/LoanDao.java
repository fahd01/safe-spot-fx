package com.safespot.fx.dao;

import com.safespot.fx.model.Loan;

import java.sql.SQLException;
import java.util.List;

public interface LoanDao {
    public List<Loan> findAll() throws SQLException;
    public Loan persist(Loan loan);
    public Loan update(Loan loan);
    public void delete(Loan loan) throws SQLException;
}
