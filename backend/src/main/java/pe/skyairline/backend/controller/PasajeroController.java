package pe.skyairline.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.skyairline.backend.model.Pasajero;
import pe.skyairline.backend.repository.PasajeroRepository;

import java.util.List;

@RestController
@RequestMapping("/api/pasajeros")
public class PasajeroController {

    private final PasajeroRepository repository;

    public PasajeroController(PasajeroRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pasajero> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pasajero> obtener(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pasajero crear(@Valid @RequestBody Pasajero pasajero) {
        pasajero.setId(null);
        return repository.save(pasajero);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pasajero> actualizar(@PathVariable Long id, @Valid @RequestBody Pasajero datos) {
        return repository.findById(id).map(existente -> {
            existente.setNombres(datos.getNombres());
            existente.setApellidos(datos.getApellidos());
            existente.setDocumento(datos.getDocumento());
            existente.setEmail(datos.getEmail());
            existente.setTelefono(datos.getTelefono());
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
