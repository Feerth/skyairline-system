package com.skyairlines.model.entity;

public class Ruta {
    private Integer id;
    private String codigoRuta;
    private Integer idAeropuertoOrigen;
    private Integer idAeropuertoDestino;
    private Integer duracionEstimadaMin;

    private Aeropuerto aeropuertoOrigen;
    private Aeropuerto aeropuertoDestino;

    public Ruta() {
    }

    public Ruta(Integer id, String codigoRuta, Integer idAeropuertoOrigen, Integer idAeropuertoDestino, Integer duracionEstimadaMin) {
        this.id = id;
        this.codigoRuta = codigoRuta;
        this.idAeropuertoOrigen = idAeropuertoOrigen;
        this.idAeropuertoDestino = idAeropuertoDestino;
        this.duracionEstimadaMin = duracionEstimadaMin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoRuta() {
        return codigoRuta;
    }

    public void setCodigoRuta(String codigoRuta) {
        this.codigoRuta = codigoRuta;
    }

    public Integer getIdAeropuertoOrigen() {
        return idAeropuertoOrigen;
    }

    public void setIdAeropuertoOrigen(Integer idAeropuertoOrigen) {
        this.idAeropuertoOrigen = idAeropuertoOrigen;
    }

    public Integer getIdAeropuertoDestino() {
        return idAeropuertoDestino;
    }

    public void setIdAeropuertoDestino(Integer idAeropuertoDestino) {
        this.idAeropuertoDestino = idAeropuertoDestino;
    }

    public Integer getDuracionEstimadaMin() {
        return duracionEstimadaMin;
    }

    public void setDuracionEstimadaMin(Integer duracionEstimadaMin) {
        this.duracionEstimadaMin = duracionEstimadaMin;
    }

    public Aeropuerto getAeropuertoOrigen() {
        return aeropuertoOrigen;
    }

    public void setAeropuertoOrigen(Aeropuerto aeropuertoOrigen) {
        this.aeropuertoOrigen = aeropuertoOrigen;
    }

    public Aeropuerto getAeropuertoDestino() {
        return aeropuertoDestino;
    }

    public void setAeropuertoDestino(Aeropuerto aeropuertoDestino) {
        this.aeropuertoDestino = aeropuertoDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ruta ruta = (Ruta) o;
        return id != null && id.equals(ruta.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return codigoRuta != null ? codigoRuta : "Ruta#" + id;
    }
}