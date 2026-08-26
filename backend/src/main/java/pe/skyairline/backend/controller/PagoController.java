package pe.skyairline.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.skyairline.backend.model.Pago;
import pe.skyairline.backend.repository.PagoRepository;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoRepository repository;

    public PagoController(PagoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Pago> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtener(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pago crear(@Valid @RequestBody Pago pago) {
        pago.setId(null);
        return repository.save(pago);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pago> actualizar(@PathVariable Long id, @Valid @RequestBody Pago datos) {
        return repository.findById(id).map(existente -> {
            existente.setReserva(datos.getReserva());
            existente.setMonto(datos.getMonto());
            existente.setFechaPago(datos.getFechaPago());
            existente.setMetodoPago(datos.getMetodoPago());
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
