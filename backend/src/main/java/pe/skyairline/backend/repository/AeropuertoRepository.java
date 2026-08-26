package pe.skyairline.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.skyairline.backend.model.Aeropuerto;

public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long> {
}
