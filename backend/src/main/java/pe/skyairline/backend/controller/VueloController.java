package pe.skyairline.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.skyairline.backend.model.Vuelo;
import pe.skyairline.backend.repository.VueloRepository;

import java.util.List;

@RestController
@RequestMapping("/api/vuelos")
public class VueloController {

    private final VueloRepository repository;

    public VueloController(VueloRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Vuelo> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vuelo> obtener(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vuelo crear(@Valid @RequestBody Vuelo vuelo) {
        vuelo.setId(null);
        return repository.save(vuelo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vuelo> actualizar(@PathVariable Long id, @Valid @RequestBody Vuelo datos) {
        return repository.findById(id).map(existente -> {
            existente.setNumeroVuelo(datos.getNumeroVuelo());
            existente.setOrigen(datos.getOrigen());
            existente.setDestino(datos.getDestino());
            existente.setFechaSalida(datos.getFechaSalida());
            existente.setHoraSalida(datos.getHoraSalida());
            existente.setHoraLlegada(datos.getHoraLlegada());
            existente.setPrecio(datos.getPrecio());
            existente.setAsientosDisponibles(datos.getAsientosDisponibles());
            existente.setEstado(datos.getEstado());
            return ResponseEntity.ok(repository.save(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
