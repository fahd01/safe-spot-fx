package com.safespot.fx.dao;

import com.safespot.fx.model.Loan;
import com.safespot.fx.model.LoanStatus;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDaoImpl implements LoanDao{
    @Override
    public List<Loan> findAll() throws SQLException {
        List<Loan> loans=new ArrayList<>();
            Connection connection= DatabaseConnection.getConnection();
            PreparedStatement ps =connection.prepareStatement("select * from loan");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
              Loan l= new Loan(
                        rs.getInt("id"),
                        rs.getBigDecimal("amount"),
                        rs.getBigDecimal("interest"),
                        rs.getInt("term"),
                        rs.getString("purpose"),
                        LoanStatus.valueOf(rs.getString("status"))
                        );
            loans.add(l);
            }

        return loans;
    }
}
