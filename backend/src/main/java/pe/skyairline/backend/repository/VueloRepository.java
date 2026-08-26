package pe.skyairline.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.skyairline.backend.model.Vuelo;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
}
