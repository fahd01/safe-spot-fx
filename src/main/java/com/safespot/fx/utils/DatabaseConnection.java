package com.safespot.fx.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static final String DB_USERNAME = "root";
    public static final String DB_PASSWORD = "null";
    private static Connection connection;
    private DatabaseConnection() {
        String url = "jdbc:mysql://localhost:3306/safe-spot";
        String user = "root";//System.getenv(DB_USERNAME);
        String password = null;//System.getenv(DB_PASSWORD);
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, user, password);

        } catch (/*ClassNotFoundException |*/ SQLException e) {
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
