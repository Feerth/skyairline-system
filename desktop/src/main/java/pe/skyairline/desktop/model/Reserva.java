package pe.skyairline.desktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Reserva {
    private Long id;
    private Pasajero pasajero;
    private Vuelo vuelo;
    private LocalDate fechaReserva;
    private int numPasajes;
    private EstadoReserva estado;
    private BigDecimal total;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Pasajero getPasajero() { return pasajero; }
    public void setPasajero(Pasajero pasajero) { this.pasajero = pasajero; }
    public Vuelo getVuelo() { return vuelo; }
    public void setVuelo(Vuelo vuelo) { this.vuelo = vuelo; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }
    public int getNumPasajes() { return numPasajes; }
    public void setNumPasajes(int numPasajes) { this.numPasajes = numPasajes; }
    public EstadoReserva getEstado() { return estado; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
}
