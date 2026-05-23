package com.example.hussain_progect_tp.servizi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private  static  final String url = "jdbc:mysql://localhost:3306/hussain";
    private  static  final String user = "root";
    private  static  final String password = "";
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url,user,password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;

    }
}
