package pe.skyairline.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.skyairline.backend.model.Pasajero;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
}
