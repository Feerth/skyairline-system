package pe.skyairline.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "vuelos")
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El numero de vuelo es obligatorio")
    private String numeroVuelo;

    @ManyToOne
    @JoinColumn(name = "origen_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "El vuelo debe tener un aeropuerto de origen")
    private Aeropuerto origen;

    @ManyToOne
    @JoinColumn(name = "destino_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "El vuelo debe tener un aeropuerto de destino")
    private Aeropuerto destino;

    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDate fechaSalida;

    private LocalTime horaSalida;

    private LocalTime horaLlegada;

    @NotNull
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Positive(message = "Los asientos disponibles deben ser mayor a 0")
    private int asientosDisponibles;

    @Enumerated(EnumType.STRING)
    private EstadoVuelo estado = EstadoVuelo.PROGRAMADO;

    public Vuelo() {}

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
}
