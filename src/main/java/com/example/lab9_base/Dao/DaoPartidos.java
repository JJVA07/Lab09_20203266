package com.example.lab9_base.Dao;

import com.example.lab9_base.Bean.Arbitro;
import com.example.lab9_base.Bean.Estadio;
import com.example.lab9_base.Bean.Partido;
import com.example.lab9_base.Bean.Seleccion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoPartidos extends DaoBase{
    public ArrayList<Partido> listaDePartidos() {
        String sql = "SELECT p.idPartido, p.numeroJornada, p.fecha, s1.idSeleccion AS idLocal, s1.nombre AS nombreLocal, s2.idSeleccion AS idVisitante, s2.nombre AS nombreVisitante, \n" +
                "e.nombre AS nombreEstadio, a.idArbitro, a.nombre AS nombreArbitro FROM partido p JOIN seleccion s1 ON p.seleccionLocal = s1.idSeleccion \n" +
                "JOIN seleccion s2 ON p.seleccionVisitante = s2.idSeleccion JOIN arbitro a ON p.arbitro = a.idArbitro JOIN estadio e ON s1.estadio_idEstadio = e.idEstadio";

        ArrayList<Partido> partidos = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Partido partido = new Partido();
                partido.setIdPartido(rs.getInt("idPartido"));
                partido.setNumeroJornada(rs.getInt("numeroJornada"));
                partido.setFecha(rs.getString("fecha"));

                Seleccion seleccionLocal = new Seleccion();
                seleccionLocal.setIdSeleccion(rs.getInt("idLocal"));
                seleccionLocal.setNombre(rs.getString("nombreLocal"));
                partido.setSeleccionLocal(seleccionLocal);

                Seleccion seleccionVisitante = new Seleccion();
                seleccionVisitante.setIdSeleccion(rs.getInt("idVisitante"));
                seleccionVisitante.setNombre(rs.getString("nombreVisitante"));
                partido.setSeleccionVisitante(seleccionVisitante);

                Estadio estadio = new Estadio();
                estadio.setNombre(rs.getString("nombreEstadio"));
                seleccionLocal.setEstadio(estadio);

                Arbitro arbitro = new Arbitro();
                arbitro.setIdArbitro(rs.getInt("idArbitro"));
                arbitro.setNombre(rs.getString("nombreArbitro"));
                partido.setArbitro(arbitro);

                partidos.add(partido);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return partidos;
    }


    public void crearPartido(Partido partido) {
        String sql = "INSERT INTO partido (numeroJornada, fecha, seleccionLocal, seleccionVisitante, arbitro) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer los valores en la consulta SQL
            stmt.setInt(1, partido.getNumeroJornada());
            stmt.setString(2, partido.getFecha());
            stmt.setInt(3, partido.getSeleccionLocal().getIdSeleccion());
            stmt.setInt(4, partido.getSeleccionVisitante().getIdSeleccion());
            stmt.setInt(5, partido.getArbitro().getIdArbitro());

            // Ejecutar la inserción
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
