package com.skyairlines.model.entity;

import com.skyairlines.model.enums.EstadoAsientoVuelo;
import java.math.BigDecimal;

public class VueloAsiento {
    private Integer id;
    private Integer idVuelo;
    private Integer idAsientoAeronave;
    private EstadoAsientoVuelo estado;
    private BigDecimal precio;
    private Integer version;

    public VueloAsiento() {
    }

    public VueloAsiento(Integer id, Integer idVuelo, Integer idAsientoAeronave, EstadoAsientoVuelo estado, BigDecimal precio, Integer version) {
        this.id = id;
        this.idVuelo = idVuelo;
        this.idAsientoAeronave = idAsientoAeronave;
        this.estado = estado;
        this.precio = precio;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdVuelo() {
        return idVuelo;
    }

    public void setIdVuelo(Integer idVuelo) {
        this.idVuelo = idVuelo;
    }

    public Integer getIdAsientoAeronave() {
        return idAsientoAeronave;
    }

    public void setIdAsientoAeronave(Integer idAsientoAeronave) {
        this.idAsientoAeronave = idAsientoAeronave;
    }

    public EstadoAsientoVuelo getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsientoVuelo estado) {
        this.estado = estado;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VueloAsiento that = (VueloAsiento) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "VueloAsiento{" +
                "id=" + id +
                ", idVuelo=" + idVuelo +
                ", idAsientoAeronave=" + idAsientoAeronave +
                ", estado=" + estado +
                ", precio=" + precio +
                ", version=" + version +
                '}';
    }
}
