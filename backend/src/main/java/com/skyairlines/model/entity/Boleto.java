package com.skyairlines.model.entity;

import java.time.OffsetDateTime;

public class Boleto {
    private Integer id;
    private String codigoEticket;
    private Integer idReserva;
    private Integer idVueloAsiento;
    private Integer idPasajero;
    private String estado;
    private OffsetDateTime fechaEmision;

    public Boleto() {
    }

    public Boleto(Integer id, String codigoEticket, Integer idReserva, Integer idVueloAsiento, Integer idPasajero, String estado, OffsetDateTime fechaEmision) {
        this.id = id;
        this.codigoEticket = codigoEticket;
        this.idReserva = idReserva;
        this.idVueloAsiento = idVueloAsiento;
        this.idPasajero = idPasajero;
        this.estado = estado;
        this.fechaEmision = fechaEmision;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoEticket() {
        return codigoEticket;
    }

    public void setCodigoEticket(String codigoEticket) {
        this.codigoEticket = codigoEticket;
    }

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdVueloAsiento() {
        return idVueloAsiento;
    }

    public void setIdVueloAsiento(Integer idVueloAsiento) {
        this.idVueloAsiento = idVueloAsiento;
    }

    public Integer getIdPasajero() {
        return idPasajero;
    }

    public void setIdPasajero(Integer idPasajero) {
        this.idPasajero = idPasajero;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public OffsetDateTime getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(OffsetDateTime fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Boleto that = (Boleto) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Boleto{" +
                "id=" + id +
                ", codigoEticket='" + codigoEticket + '\'' +
                ", idReserva=" + idReserva +
                ", idVueloAsiento=" + idVueloAsiento +
                ", idPasajero=" + idPasajero +
                ", estado='" + estado + '\'' +
                ", fechaEmision=" + fechaEmision +
                '}';
    }
}
