package com.skyairlines.model.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class Compra {
    private Integer id;
    private Integer idCliente;
    private String codigoTransaccion;
    private BigDecimal montoTotal;
    private String estadoPago;
    private OffsetDateTime fechaCompra;

    public Compra() {
    }

    public Compra(Integer id, Integer idCliente, String codigoTransaccion, BigDecimal montoTotal, String estadoPago, OffsetDateTime fechaCompra) {
        this.id = id;
        this.idCliente = idCliente;
        this.codigoTransaccion = codigoTransaccion;
        this.montoTotal = montoTotal;
        this.estadoPago = estadoPago;
        this.fechaCompra = fechaCompra;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getCodigoTransaccion() {
        return codigoTransaccion;
    }

    public void setCodigoTransaccion(String codigoTransaccion) {
        this.codigoTransaccion = codigoTransaccion;
    }

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public OffsetDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(OffsetDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Compra that = (Compra) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", idCliente=" + idCliente +
                ", codigoTransaccion='" + codigoTransaccion + '\'' +
                ", montoTotal=" + montoTotal +
                ", estadoPago='" + estadoPago + '\'' +
                ", fechaCompra=" + fechaCompra +
                '}';
    }
}
