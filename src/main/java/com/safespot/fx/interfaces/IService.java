package com.safespot.fx.interfaces;

import com.safespot.fx.models.Bid;
import com.safespot.fx.models.Loan;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {

    List<T> findAll() throws SQLException;

    T persist(T model);
    public T update(T model);
    public void delete(T model) throws SQLException;

}
