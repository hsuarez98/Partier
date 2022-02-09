package com.example.partier.Clases;

public class Evento {

    private int id;
    private String user;
    private String userId;
    private String lugarFiesta;
    private String ciudadSalida;
    private String provincia;
    private String horaSalida;
    private int numPersonas;
    private int precioGasolina;

    public Evento() {}

    public Evento(int id, String user, String userId, String lugarFiesta, String ciudadSalida, String provincia, String horaSalida, int numPersonas, int precioGasolina) {
        this.id = id;
        this.user = user;
        this.userId = userId;
        this.lugarFiesta = lugarFiesta;
        this.ciudadSalida = ciudadSalida;
        this.provincia = provincia;
        this.horaSalida = horaSalida;
        this.numPersonas = numPersonas;
        this.precioGasolina = precioGasolina;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLugarFiesta() {
        return lugarFiesta;
    }

    public void setLugarFiesta(String lugarFiesta) {
        this.lugarFiesta = lugarFiesta;
    }

    public String getCiudadSalida() {
        return ciudadSalida;
    }

    public void setCiudadSalida(String ciudadSalida) {
        this.ciudadSalida = ciudadSalida;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(int numPersonas) {
        this.numPersonas = numPersonas;
    }

    public int getPrecioGasolina() {
        return precioGasolina;
    }

    public void setPrecioGasolina(int precioGasolina) {
        this.precioGasolina = precioGasolina;
    }
}
