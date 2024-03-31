package com.safespot.fx.dao;

import com.safespot.fx.model.Bid;

import java.sql.SQLException;
import java.util.List;

public interface BidDao {
    public List<Bid> findAll() throws SQLException;
}
