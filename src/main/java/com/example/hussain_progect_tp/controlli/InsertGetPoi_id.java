package com.example.hussain_progect_tp.controlli;

import com.example.hussain_progect_tp.servizi.DBConnection;
import com.mysql.cj.exceptions.ConnectionIsClosedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetPoi_id {
    public int getpoi_id(long osm_id,String name,double lat, double lon,String type) {

        try (Connection conn = DBConnection.getConnection()){
            String q = "insert ignore into poi (osm_id,name,lat,lon,type) values (?,?,?,?,?)";
             try(PreparedStatement ps = conn.prepareStatement(q)) {
                 ps.setLong(1, osm_id);
                 ps.setString(2, name);
                 ps.setDouble(3, lat);
                 ps.setDouble(4, lon);
                 ps.setString(5, type);
                 ps.executeUpdate();
             }
            String getPoi = "select poi_id from poi where osm_id=?";
            try (PreparedStatement ps2 = conn.prepareStatement(getPoi)) {
                ps2.setLong(1, osm_id);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
