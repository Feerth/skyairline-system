package pe.skyairline.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "aeropuertos")
public class Aeropuerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El codigo IATA es obligatorio")
    @Size(min = 3, max = 3, message = "El codigo IATA debe tener 3 letras")
    @Column(unique = true, length = 3)
    private String codigoIata;

    @NotBlank(message = "El nombre del aeropuerto es obligatorio")
    private String nombre;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El pais es obligatorio")
    private String pais;

    public Aeropuerto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigoIata() { return codigoIata; }
    public void setCodigoIata(String codigoIata) { this.codigoIata = codigoIata; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getPais() { return pais; }
    public void setPais(String pais) { this.pais = pais; }
}
