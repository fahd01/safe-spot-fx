package com.safespot.fx.services;

import com.safespot.fx.interfaces.IBidService;
import com.safespot.fx.models.Bid;
import com.safespot.fx.models.BidStatus;
import com.safespot.fx.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BidService implements IBidService {
    @Override
    public List<Bid> findAll() throws SQLException {
        List<Bid> bids = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement("select * from bid");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Bid b = new Bid(
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

    @Override
    public List<Bid> findByLoanId(int loanId) {
        List<Bid> bids = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM bid WHERE loan_id =?");) {
            ps.setInt(1, loanId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bid bid = new Bid(
                    rs.getInt("id"),
                    rs.getBigDecimal("amount"),
                    BidStatus.valueOf(rs.getString("status")),
                    rs.getInt("bidder_id"),
                    rs.getInt("loan_id"),
                    rs.getInt("automation_id")
                );
                bids.add(bid);
            }
            return bids;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Bid persist(Bid bid) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO bid (amount, status, bidder_id, loan_id, automation_id) VALUES (?, ?, ?, ?, ?)",
                        PreparedStatement.RETURN_GENERATED_KEYS
                )
        ) {
            preparedStatement.setBigDecimal(1, bid.getAmount());
            preparedStatement.setString(2, bid.getStatus().toString());
            preparedStatement.setInt(3, bid.getBidderId());
            preparedStatement.setInt(4, bid.getLoanId());
            // Using setObject instead of setInt as automationId could be null
            // and setInt does not support null values
            preparedStatement.setObject(5, bid.getAutomationId(), java.sql.Types.INTEGER);
            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            rs.next();
            int lastInsertId = rs.getInt(1);
            bid.setId(lastInsertId);
            return bid;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Bid bid) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM bid WHERE id = ?");

        preparedStatement.setInt(1, bid.getId());
        preparedStatement.executeUpdate();

    }

    @Override
    public void update(Bid bid) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE bid SET amount=?, status=? WHERE id =?"
                )
        ) {
            preparedStatement.setBigDecimal(1, bid.getAmount());
            preparedStatement.setString(2, bid.getStatus().toString());
            preparedStatement.setInt(3, bid.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void acceptBid(int id) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE bid SET status=? WHERE id =?"
                )
        ) {
            preparedStatement.setString(1, BidStatus.APPROVED.toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void activateBid(int id) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE bid SET status=? WHERE id =?"
                )
        ) {
            preparedStatement.setString(1, BidStatus.ACTIVE.toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rejectBid(int id) {
        Connection connection = DatabaseConnection.getConnection();
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE bid SET status=? WHERE id =?"
                )
        ) {
            preparedStatement.setString(1, BidStatus.REJECTED.toString());
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
