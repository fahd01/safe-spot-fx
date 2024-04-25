package com.safespot.fx.interfaces;

import com.safespot.fx.models.Loan;

import java.sql.SQLException;
import java.util.List;

public interface ILoanService {
    public List<Loan> findAll() throws SQLException;
    public Loan persist(Loan loan);
    public Loan update(Loan loan);
    public void delete(Loan loan) throws SQLException;
}
