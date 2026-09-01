package com.skyairlines.model.entity;

import com.skyairlines.model.enums.EstadoEquipaje;
import java.time.OffsetDateTime;

public class HistorialEquipaje {
    private Integer id;
    private Integer idEquipaje;
    private EstadoEquipaje estado;
    private Integer ubicacionAeropuertoId;
    private String cintaCarrusel;
    private String observaciones;
    private Integer idEmpleado;
    private OffsetDateTime fechaRegistro;

    public HistorialEquipaje() {
    }

    public HistorialEquipaje(Integer id, Integer idEquipaje, EstadoEquipaje estado, Integer ubicacionAeropuertoId, String cintaCarrusel, String observaciones, Integer idEmpleado, OffsetDateTime fechaRegistro) {
        this.id = id;
        this.idEquipaje = idEquipaje;
        this.estado = estado;
        this.ubicacionAeropuertoId = ubicacionAeropuertoId;
        this.cintaCarrusel = cintaCarrusel;
        this.observaciones = observaciones;
        this.idEmpleado = idEmpleado;
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEquipaje() {
        return idEquipaje;
    }

    public void setIdEquipaje(Integer idEquipaje) {
        this.idEquipaje = idEquipaje;
    }

    public EstadoEquipaje getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipaje estado) {
        this.estado = estado;
    }

    public Integer getUbicacionAeropuertoId() {
        return ubicacionAeropuertoId;
    }

    public void setUbicacionAeropuertoId(Integer ubicacionAeropuertoId) {
        this.ubicacionAeropuertoId = ubicacionAeropuertoId;
    }

    public String getCintaCarrusel() {
        return cintaCarrusel;
    }

    public void setCintaCarrusel(String cintaCarrusel) {
        this.cintaCarrusel = cintaCarrusel;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(OffsetDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistorialEquipaje that = (HistorialEquipaje) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "HistorialEquipaje{" +
                "id=" + id +
                ", idEquipaje=" + idEquipaje +
                ", estado=" + estado +
                ", ubicacionAeropuertoId=" + ubicacionAeropuertoId +
                ", cintaCarrusel='" + cintaCarrusel + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", idEmpleado=" + idEmpleado +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
