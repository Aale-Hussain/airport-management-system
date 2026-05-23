package com.example.hussain_progect_tp.logica;

import com.example.hussain_progect_tp.servizi.DBConnection;
import com.example.hussain_progect_tp.servizi.PasswordHash;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterLogica {
    public boolean register(String username,String email,String nome,String cognome,String password){

        try{
           Connection connection = DBConnection.getConnection();
            String pass = PasswordHash.hash(password);
           String query = "INSERT INTO users (username, email, nome, cognome, password_hash) " +
                   "VALUES (?, ?, ?, ?, ?) ";
            PreparedStatement ps = connection.prepareStatement(query);
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
