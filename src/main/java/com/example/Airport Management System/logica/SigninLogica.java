package com.example.hussain_progect_tp.logica;

import com.example.hussain_progect_tp.servizi.DBConnection;
import com.example.hussain_progect_tp.servizi.PasswordHash;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SigninLogica {
    public boolean insert_user(String username,String email,String nome,String cognome,String password){
        String query = "INSERT INTO users (username, email, nome, cognome, password_hash) " +
                "VALUES (?, ?, ?, ?, ?) ";
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){
            if(password == null){
                return false;
            }

            String pass = PasswordHash.hash(password);

            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, nome);
            ps.setString(4, cognome);
            ps.setString(5, pass);

            int rows = ps.executeUpdate();
            if(rows < 1){
                return false;
            }
            return true;



        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
