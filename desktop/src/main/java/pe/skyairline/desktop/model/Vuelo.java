package pe.skyairline.desktop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Vuelo {
    private Long id;
    private String numeroVuelo;
    private Aeropuerto origen;
    private Aeropuerto destino;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private LocalTime horaLlegada;
    private BigDecimal precio;
    private int asientosDisponibles;
    private EstadoVuelo estado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroVuelo() { return numeroVuelo; }
    public void setNumeroVuelo(String numeroVuelo) { this.numeroVuelo = numeroVuelo; }
    public Aeropuerto getOrigen() { return origen; }
    public void setOrigen(Aeropuerto origen) { this.origen = origen; }
    public Aeropuerto getDestino() { return destino; }
    public void setDestino(Aeropuerto destino) { this.destino = destino; }
    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }
    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }
    public LocalTime getHoraLlegada() { return horaLlegada; }
    public void setHoraLlegada(LocalTime horaLlegada) { this.horaLlegada = horaLlegada; }
    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public int getAsientosDisponibles() { return asientosDisponibles; }
    public void setAsientosDisponibles(int asientosDisponibles) { this.asientosDisponibles = asientosDisponibles; }
    public EstadoVuelo getEstado() { return estado; }
    public void setEstado(EstadoVuelo estado) { this.estado = estado; }

    @Override
    public String toString() {
        return (numeroVuelo == null ? "" : numeroVuelo) + " (" +
                (origen == null ? "?" : origen.getCodigoIata()) + " -> " +
                (destino == null ? "?" : destino.getCodigoIata()) + ")";
    }
}
