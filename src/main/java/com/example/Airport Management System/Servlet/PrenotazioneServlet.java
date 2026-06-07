package com.example.hussain_progect_tp.Servlet;


import com.example.hussain_progect_tp.classes.User;
import com.example.hussain_progect_tp.controlli.InsertGetPoi_id;
import com.example.hussain_progect_tp.controlli.SendEmail;
import com.example.hussain_progect_tp.servizi.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet(name = "prenotazioneServlet" , value = "/PrenotazioneServlet")
public class PrenotazioneServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        User user = (User)session.getAttribute("user");
        if(user==null){
            resp.getWriter().write("sessione scaduta");
            return;
        }
        long par_osm_id = Long.parseLong(req.getParameter("par_osm_id"));
        String par_airporto =  req.getParameter("par_airporto");
        double par_lat = Double.parseDouble(req.getParameter("par_lat"));
        double par_lon = Double.parseDouble(req.getParameter("par_lon"));
        long arr_osm_id = Long.parseLong(req.getParameter("arr_osm_id"));
        String arr_airporto = req.getParameter("arr_airporto");
        double arr_lat = Double.parseDouble(req.getParameter("arr_lat"));
        double arr_lon = Double.parseDouble(req.getParameter("arr_lon"));
        String date_str =  req.getParameter("data");
        java.time.LocalDate data = java.time.LocalDate.parse(date_str);
        if(data.isBefore(LocalDate.now())){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("data invalida");
            return;
        }
        int prenotazione_id_par = new InsertGetPoi_id().insertgetpoi_id(par_osm_id,par_airporto,par_lat,par_lon,"airporto");
        int prenotazione_id_arr = new InsertGetPoi_id().insertgetpoi_id(arr_osm_id,arr_airporto,arr_lat,arr_lon,"airporto");
        if ( prenotazione_id_par == -1 || prenotazione_id_arr == -1) {
            resp.getWriter().write("airporto non trovato");
            return;
        }
        String q = "insert into prenotazione (user_id,partenza_poi_id, arrival_poi_id,data_prenotazione)" +
                "values (?,?,?,?)";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(q)) {

            ps.setInt(1, user.getUser_id());
            ps.setInt(2, prenotazione_id_par);
            ps.setInt(3, prenotazione_id_arr);
            ps.setDate(4, java.sql.Date.valueOf(data));
            ps.executeUpdate();

            new SendEmail().sendEmail(
                    user.getEmail(),
                    "prenotazione confermata",
                    user.getUsername()+", prenotazione confermata: partenza: "+
                            par_airporto + " arrivo: "+arr_airporto + " al "+date_str
            );
            resp.getWriter().write("prenotazione confermata");

        } catch (SQLException e) {
            e.printStackTrace();
            if(e.getErrorCode() == 1062) {
                resp.getWriter().write("Hai gia prenotato su stessi dati");
            }else {
                resp.getWriter().write("errore database: " + e.getMessage());
            }
        }
    }
}
