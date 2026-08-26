package pe.skyairline.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.skyairline.backend.model.Pago;

public interface PagoRepository extends JpaRepository<Pago, Long> {
}
