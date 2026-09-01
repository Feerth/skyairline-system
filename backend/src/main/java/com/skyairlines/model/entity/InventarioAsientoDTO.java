package com.skyairlines.model.entity;

public class InventarioAsientoDTO {
    private String categoria;
    private int total;
    private int vendidos;
    private int disponibles;
    private int cancelados;
    private double ocupacion;

    public InventarioAsientoDTO() {
    }

    public InventarioAsientoDTO(String categoria, int total, int vendidos, int disponibles, int cancelados) {
        this.categoria = categoria;
        this.total = total;
        this.vendidos = vendidos;
        this.disponibles = disponibles;
        this.cancelados = cancelados;
        this.ocupacion = (total > 0) ? (vendidos * 100.0 / total) : 0;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getVendidos() {
        return vendidos;
    }

    public void setVendidos(int vendidos) {
        this.vendidos = vendidos;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getCancelados() {
        return cancelados;
    }

    public void setCancelados(int cancelados) {
        this.cancelados = cancelados;
    }

    public double getOcupacion() {
        return ocupacion;
    }

    public void setOcupacion(double ocupacion) {
        this.ocupacion = ocupacion;
    }

    @Override
    public String toString() {
        return "InventarioAsientoDTO{" +
                "categoria='" + categoria + '\'' +
                ", total=" + total +
                ", vendidos=" + vendidos +
                ", disponibles=" + disponibles +
                ", cancelados=" + cancelados +
                ", ocupacion=" + String.format("%.2f%%", ocupacion) +
                '}';
    }
}
