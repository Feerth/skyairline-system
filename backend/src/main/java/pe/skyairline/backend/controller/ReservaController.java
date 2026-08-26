package pe.skyairline.backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.skyairline.backend.model.Reserva;
import pe.skyairline.backend.repository.ReservaRepository;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaRepository repository;

    public ReservaController(ReservaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Reserva> listar() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserva> obtener(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reserva crear(@Valid @RequestBody Reserva reserva) {
        reserva.setId(null);
        if (reserva.getVuelo() != null && reserva.getVuelo().getPrecio() != null) {
            reserva.setTotal(reserva.getVuelo().getPrecio().multiply(BigDecimal.valueOf(reserva.getNumPasajes())));
        }
        return repository.save(reserva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reserva> actualizar(@PathVariable Long id, @Valid @RequestBody Reserva datos) {
        return repository.findById(id).map(existente -> {
            existente.setPasajero(datos.getPasajero());
            existente.setVuelo(datos.getVuelo());
            existente.setFechaReserva(datos.getFechaReserva());
            existente.setNumPasajes(datos.getNumPasajes());
            existente.setEstado(datos.getEstado());
            if (existente.getVuelo() != null && existente.getVuelo().getPrecio() != null) {
                existente.setTotal(existente.getVuelo().getPrecio().multiply(BigDecimal.valueOf(existente.getNumPasajes())));
            }
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
