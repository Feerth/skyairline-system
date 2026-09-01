package com.skyairlines.model.entity;

import java.time.ZoneId;

public class Aeropuerto {
    private Integer id;
    private String codigoIata;
    private String nombre;
    private String ciudad;
    private String pais;
    private String zonaHoraria;

    public Aeropuerto() {
    }

    public Aeropuerto(Integer id, String codigoIata, String nombre, String ciudad, String pais, String zonaHoraria) {
        this.id = id;
        this.codigoIata = codigoIata;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.pais = pais;
        this.zonaHoraria = zonaHoraria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoIata() {
        return codigoIata;
    }

    public void setCodigoIata(String codigoIata) {
        this.codigoIata = codigoIata;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public void setZonaHoraria(String zonaHoraria) {
        this.zonaHoraria = zonaHoraria;
    }

    public ZoneId getZoneId() {
        return ZoneId.of(zonaHoraria != null ? zonaHoraria : "America/Lima");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aeropuerto that = (Aeropuerto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Aeropuerto{" +
                "id=" + id +
                ", codigoIata='" + codigoIata + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", pais='" + pais + '\'' +
                '}';
    }
}