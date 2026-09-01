package com.skyairlines.model.entity;

public class Aeronave {
    private Integer id;
    private String matricula;
    private String modelo;
    private Integer capacidadPasajeros;
    private String estado;

    public Aeronave() {
    }

    public Aeronave(Integer id, String matricula, String modelo, Integer capacidadPasajeros, String estado) {
        this.id = id;
        this.matricula = matricula;
        this.modelo = modelo;
        this.capacidadPasajeros = capacidadPasajeros;
        this.estado = estado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidadPasajeros() {
        return capacidadPasajeros;
    }

    public void setCapacidadPasajeros(Integer capacidadPasajeros) {
        this.capacidadPasajeros = capacidadPasajeros;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Aeronave that = (Aeronave) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Aeronave{" +
                "id=" + id +
                ", matricula='" + matricula + '\'' +
                ", modelo='" + modelo + '\'' +
                ", capacidadPasajeros=" + capacidadPasajeros +
                ", estado='" + estado + '\'' +
                '}';
    }
}
