package pe.skyairline.desktop.model;

public class Aeropuerto {
    private Long id;
    private String codigoIata;
    private String nombre;
    private String ciudad;
    private String pais;

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

    @Override
    public String toString() {
        return (codigoIata == null ? "" : codigoIata) + " - " + (ciudad == null ? "" : ciudad);
    }
}
