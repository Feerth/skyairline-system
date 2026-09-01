package com.skyairlines.model.entity;

import com.skyairlines.model.enums.CategoriaEquipaje;
import com.skyairlines.model.enums.EstadoEquipaje;
import java.math.BigDecimal;

public class Equipaje {
    private Integer id;
    private Integer idBoleto;
    private String codigoEtiquetaBag;
    private CategoriaEquipaje categoriaPeso;
    private BigDecimal pesoKg;
    private EstadoEquipaje estadoActual;
    private String cintaCarruselActual;

    public Equipaje() {
    }

    public Equipaje(Integer id, Integer idBoleto, String codigoEtiquetaBag, CategoriaEquipaje categoriaPeso, BigDecimal pesoKg, EstadoEquipaje estadoActual, String cintaCarruselActual) {
        this.id = id;
        this.idBoleto = idBoleto;
        this.codigoEtiquetaBag = codigoEtiquetaBag;
        this.categoriaPeso = categoriaPeso;
        this.pesoKg = pesoKg;
        this.estadoActual = estadoActual;
        this.cintaCarruselActual = cintaCarruselActual;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Integer idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getCodigoEtiquetaBag() {
        return codigoEtiquetaBag;
    }

    public void setCodigoEtiquetaBag(String codigoEtiquetaBag) {
        this.codigoEtiquetaBag = codigoEtiquetaBag;
    }

    public CategoriaEquipaje getCategoriaPeso() {
        return categoriaPeso;
    }

    public void setCategoriaPeso(CategoriaEquipaje categoriaPeso) {
        this.categoriaPeso = categoriaPeso;
    }

    public BigDecimal getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(BigDecimal pesoKg) {
        this.pesoKg = pesoKg;
    }

    public EstadoEquipaje getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(EstadoEquipaje estadoActual) {
        this.estadoActual = estadoActual;
    }

    public String getCintaCarruselActual() {
        return cintaCarruselActual;
    }

    public void setCintaCarruselActual(String cintaCarruselActual) {
        this.cintaCarruselActual = cintaCarruselActual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipaje that = (Equipaje) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Equipaje{" +
                "id=" + id +
                ", idBoleto=" + idBoleto +
                ", codigoEtiquetaBag='" + codigoEtiquetaBag + '\'' +
                ", categoriaPeso=" + categoriaPeso +
                ", pesoKg=" + pesoKg +
                ", estadoActual=" + estadoActual +
                ", cintaCarruselActual='" + cintaCarruselActual + '\'' +
                '}';
    }
}
