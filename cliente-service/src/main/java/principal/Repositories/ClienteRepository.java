package principal.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import principal.Models.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
