package principal.Configs;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import principal.Models.Cliente;

@FeignClient(
    name = "cliente-service",
    url = "${cliente.service.url}"
)
public interface ClienteClient {
    @GetMapping("/api/v1/clientes/{id}")
    Cliente obtenerCliente(@PathVariable("id") Long id);
}
