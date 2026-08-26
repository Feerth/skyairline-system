package pe.skyairline.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.skyairline.backend.model.Reserva;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
