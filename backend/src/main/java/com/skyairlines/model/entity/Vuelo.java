package com.skyairlines.model.entity;

import java.time.OffsetDateTime;

public class Vuelo {
    private Integer id;
    private String codigoVuelo;
    private Integer idRuta;
    private Integer idAeronave;
    private OffsetDateTime fechaSalidaProgramada;
    private OffsetDateTime fechaLlegadaProgramada;
    private String estado;

    private transient Ruta ruta;
    private transient Aeronave aeronave;

    public Vuelo() {
    }

    public Vuelo(Integer id, String codigoVuelo, Integer idRuta, Integer idAeronave,
                 OffsetDateTime fechaSalidaProgramada, OffsetDateTime fechaLlegadaProgramada, String estado) {
        this.id = id;
        this.codigoVuelo = codigoVuelo;
        this.idRuta = idRuta;
        this.idAeronave = idAeronave;
        this.fechaSalidaProgramada = fechaSalidaProgramada;
        this.fechaLlegadaProgramada = fechaLlegadaProgramada;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoVuelo() {
        return codigoVuelo;
    }

    public void setCodigoVuelo(String codigoVuelo) {
        this.codigoVuelo = codigoVuelo;
    }

    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Integer getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(Integer idAeronave) {
        this.idAeronave = idAeronave;
    }

    public OffsetDateTime getFechaSalidaProgramada() {
        return fechaSalidaProgramada;
    }

    public void setFechaSalidaProgramada(OffsetDateTime fechaSalidaProgramada) {
        this.fechaSalidaProgramada = fechaSalidaProgramada;
    }

    public OffsetDateTime getFechaLlegadaProgramada() {
        return fechaLlegadaProgramada;
    }

    public void setFechaLlegadaProgramada(OffsetDateTime fechaLlegadaProgramada) {
        this.fechaLlegadaProgramada = fechaLlegadaProgramada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Aeronave getAeronave() {
        return aeronave;
    }

    public void setAeronave(Aeronave aeronave) {
        this.aeronave = aeronave;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vuelo that = (Vuelo) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Vuelo{" +
                "id=" + id +
                ", codigoVuelo='" + codigoVuelo + '\'' +
                ", idRuta=" + idRuta +
                ", idAeronave=" + idAeronave +
                ", fechaSalidaProgramada=" + fechaSalidaProgramada +
                ", fechaLlegadaProgramada=" + fechaLlegadaProgramada +
                ", estado='" + estado + '\'' +
                '}';
    }
}
