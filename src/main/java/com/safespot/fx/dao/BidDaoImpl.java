package com.safespot.fx.dao;

import com.safespot.fx.model.Bid;
import com.safespot.fx.model.BidStatus;
import com.safespot.fx.model.Loan;
import com.safespot.fx.model.LoanStatus;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDaoImpl implements BidDao{
    @Override
    public List<Bid> findAll() throws SQLException {
        List<Bid> bids=new ArrayList<>();
            Connection connection= DatabaseConnection.getConnection();
            PreparedStatement ps =connection.prepareStatement("select * from bid");
            ResultSet rs=ps.executeQuery();
            while (rs.next()){
              Bid b= new Bid(
                        rs.getInt("id"),
                        rs.getBigDecimal("amount"),
                        BidStatus.valueOf(rs.getString("status")),
                        rs.getInt("bidder_id"),
                        rs.getInt("loan_id"),
                        rs.getInt("automation_id")
                      );
            bids.add(b);
            }

        return bids;
    }
}
