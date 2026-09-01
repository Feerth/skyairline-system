package com.skyairlines.model.entity;

import com.skyairlines.model.enums.EstadoReserva;
import java.time.OffsetDateTime;

public class Reserva {
    private Integer id;
    private Integer idCompra;
    private Integer idVueloAsiento;
    private Integer idPasajero;
    private OffsetDateTime expiraEn;
    private EstadoReserva estado;

    public Reserva() {
    }

    public Reserva(Integer id, Integer idCompra, Integer idVueloAsiento, Integer idPasajero, OffsetDateTime expiraEn, EstadoReserva estado) {
        this.id = id;
        this.idCompra = idCompra;
        this.idVueloAsiento = idVueloAsiento;
        this.idPasajero = idPasajero;
        this.expiraEn = expiraEn;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(Integer idCompra) {
        this.idCompra = idCompra;
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

    public OffsetDateTime getExpiraEn() {
        return expiraEn;
    }

    public void setExpiraEn(OffsetDateTime expiraEn) {
        this.expiraEn = expiraEn;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(EstadoReserva estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva that = (Reserva) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id=" + id +
                ", idCompra=" + idCompra +
                ", idVueloAsiento=" + idVueloAsiento +
                ", idPasajero=" + idPasajero +
                ", expiraEn=" + expiraEn +
                ", estado=" + estado +
                '}';
    }
}
