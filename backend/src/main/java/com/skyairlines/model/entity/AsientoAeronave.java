package com.skyairlines.model.entity;

public class AsientoAeronave {
    private Integer id;
    private Integer idAeronave;
    private String codigoAsiento;
    private String clase;
    private Boolean esEmergencia;

    public AsientoAeronave() {
    }

    public AsientoAeronave(Integer id, Integer idAeronave, String codigoAsiento, String clase, Boolean esEmergencia) {
        this.id = id;
        this.idAeronave = idAeronave;
        this.codigoAsiento = codigoAsiento;
        this.clase = clase;
        this.esEmergencia = esEmergencia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(Integer idAeronave) {
        this.idAeronave = idAeronave;
    }

    public String getCodigoAsiento() {
        return codigoAsiento;
    }

    public void setCodigoAsiento(String codigoAsiento) {
        this.codigoAsiento = codigoAsiento;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public Boolean getEsEmergencia() {
        return esEmergencia;
    }

    public void setEsEmergencia(Boolean esEmergencia) {
        this.esEmergencia = esEmergencia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AsientoAeronave that = (AsientoAeronave) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "AsientoAeronave{" +
                "id=" + id +
                ", idAeronave=" + idAeronave +
                ", codigoAsiento='" + codigoAsiento + '\'' +
                ", clase='" + clase + '\'' +
                ", esEmergencia=" + esEmergencia +
                '}';
    }
}
