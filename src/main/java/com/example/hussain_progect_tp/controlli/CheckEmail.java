package com.example.hussain_progect_tp.controlli;

import com.example.hussain_progect_tp.servizi.DBConnection;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class CheckEmail {
    public static boolean checkEmail(String email){
        String q = "select 1 from users where email = ?";
        try( Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(q)){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(!rs.next()){
                rs.close();
                return false;
            }
            rs.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }
}
