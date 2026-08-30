package principal.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import principal.Models.Solicitud;

import java.util.List;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findByEstado(String estado);
}