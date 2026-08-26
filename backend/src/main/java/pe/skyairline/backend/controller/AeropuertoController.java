package pe.skyairline.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.skyairline.backend.model.Aeropuerto;
import pe.skyairline.backend.repository.AeropuertoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/aeropuertos")
public class AeropuertoController {

    private final AeropuertoRepository repository;

    public AeropuertoController(AeropuertoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Aeropuerto> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aeropuerto> obtener(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Aeropuerto crear(@Valid @RequestBody Aeropuerto aeropuerto) {
        aeropuerto.setId(null);
        return repository.save(aeropuerto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aeropuerto> actualizar(@PathVariable Long id, @Valid @RequestBody Aeropuerto datos) {
        return repository.findById(id).map(existente -> {
            existente.setCodigoIata(datos.getCodigoIata());
            existente.setNombre(datos.getNombre());
            existente.setCiudad(datos.getCiudad());
            existente.setPais(datos.getPais());
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
