package com.safespot.fx.services;

import com.safespot.fx.interfaces.ILoanService;
import com.safespot.fx.models.Loan;
import com.safespot.fx.models.LoanStatus;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanService implements ILoanService {
    @Override
    public List<Loan> findAll() throws SQLException {
        List<Loan> loans = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from loan");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Loan l = new Loan(
                    rs.getInt("id"),
                    rs.getBigDecimal("amount"),
                    rs.getBigDecimal("interest"),
                    rs.getInt("term"),
                    rs.getString("purpose"),
                    LoanStatus.valueOf(rs.getString("status")),
                    rs.getInt("borrower_id")
            );
            loans.add(l);
        }

        return loans;
    }

    @Override
    public List<Loan> findByBorrowerId(int borrowerId) {
        List<Loan> loans = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM loan WHERE borrower_id =?");) {
            ps.setInt(1, borrowerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Loan l = new Loan(
                        rs.getInt("id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("interest"),
                        rs.getInt("term"),
                        rs.getString("purpose"),
                        LoanStatus.valueOf(rs.getString("status")),
                        rs.getInt("borrower_id")
                );
                loans.add(l);
            }
            return loans;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Loan persist(Loan loan) {
        Connection connection = DatabaseConnection.getConnection();
        try (
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO loan (amount, interest, term, purpose, status, borrower_id) VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )
        ) {
            preparedStatement.setBigDecimal(1, loan.getAmount());
            preparedStatement.setBigDecimal(2, loan.getInterest());
            preparedStatement.setInt(3, loan.getTerm());
            preparedStatement.setString(4, loan.getPurpose());
            preparedStatement.setString(5, loan.getStatus().toString());
            preparedStatement.setInt(6, loan.getBorrowerId());
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int lastInsertId = rs.getInt(1);
            loan.setId(lastInsertId);
            return loan;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Loan update(Loan loan) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE loan SET amount=?, interest=?, term=?, purpose=? WHERE id =?"
                )
        ) {
            preparedStatement.setBigDecimal(1, loan.getAmount());
            preparedStatement.setBigDecimal(2, loan.getInterest());
            preparedStatement.setInt(3, loan.getTerm());
            preparedStatement.setString(4, loan.getPurpose());
            preparedStatement.setInt(5, loan.getId());
            preparedStatement.executeUpdate();
            return loan;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Loan loan) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM loan WHERE id = ?");

        preparedStatement.setInt(1, loan.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void activate(int id) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE loan SET status=? WHERE id =?"
                )
        ) {
            preparedStatement.setString(1, LoanStatus.ACTIVE.toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
