package com.safespot.fx.interfaces;

import com.safespot.fx.models.Loan;

import java.sql.SQLException;
import java.util.List;

public interface ILoanService {
    List<Loan> findAll() throws SQLException;
    List<Loan> findByBorrowerId(int borrowerId);
    Loan persist(Loan loan);
    Loan update(Loan loan);
    void delete(Loan loan) throws SQLException;
}
