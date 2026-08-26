package pe.skyairline.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pasajero_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "La reserva debe tener un pasajero")
    private Pasajero pasajero;

    @ManyToOne
    @JoinColumn(name = "vuelo_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotNull(message = "La reserva debe tener un vuelo")
    private Vuelo vuelo;

    private LocalDate fechaReserva = LocalDate.now();

    @Positive(message = "El numero de pasajes debe ser mayor a 0")
    private int numPasajes;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estado = EstadoReserva.PENDIENTE;

    private BigDecimal total;

    public Reserva() {}

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
