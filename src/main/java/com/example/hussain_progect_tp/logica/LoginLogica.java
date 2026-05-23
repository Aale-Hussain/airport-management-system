package com.example.hussain_progect_tp.logica;

import com.example.hussain_progect_tp.classes.User;
import com.example.hussain_progect_tp.servizi.DBConnection;
import com.example.hussain_progect_tp.servizi.PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginLogica {
    public User login(String email, String password) {
        try{

            Connection conn = DBConnection.getConnection();

            String query =  "SELECT user_id, username, email, nome, cognome, google_id, password_hash " +
                    "FROM users " +
                    "WHERE email = ?";
            PreparedStatement ps  = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String pass = rs.getString("password_hash");
                if(PasswordHash.controllo_pass(password, pass)){
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("google_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
