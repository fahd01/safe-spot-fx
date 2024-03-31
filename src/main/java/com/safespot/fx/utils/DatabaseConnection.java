package com.safespot.fx.utils;

import javafx.scene.chart.PieChart;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection connection;
    private DatabaseConnection() {
        String url = "jdbc:mysql://localhost:3306/safe-spot";
        String user = null;
        String password = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static Connection getConnection(){
    if (connection==null){
        new DatabaseConnection();
    }
        return connection;
    }

}
