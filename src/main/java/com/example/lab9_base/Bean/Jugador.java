package com.example.lab9_base.Bean;

public class Jugador {
    private int idJugadores;
    private String nombre;
    private int edad;
    private String posicion;
    private String club;
    private int sn_idSeleccion;

    public int getIdJugadores() {
        return idJugadores;
    }

    public void setIdJugadores(int idJugadores) {
        this.idJugadores = idJugadores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }

    public int getSn_idSeleccion() {
        return sn_idSeleccion;
    }

    public void setSn_idSeleccion(int sn_idSeleccion) {
        this.sn_idSeleccion = sn_idSeleccion;
    }
}
