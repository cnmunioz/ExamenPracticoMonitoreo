package principal.Services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import principal.Configs.ClienteClient;
import principal.Models.Cliente;
import principal.Models.Solicitud;
import principal.Repositories.SolicitudRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudService {

    private static final Logger log =
            LoggerFactory.getLogger(SolicitudService.class);

    private final SolicitudRepository solicitudRepository;
    private final ClienteClient clienteClient;

    @CircuitBreaker(
            name = "clienteServiceCB",
            fallbackMethod = "fallbackCrearSolicitud"
    )
    public Solicitud registrarSolicitud(Solicitud solicitud) {

        log.info(
                "Registrando solicitud para clienteId={}",
                solicitud.getClienteId()
        );

        Cliente cliente =
                clienteClient.obtenerCliente(solicitud.getClienteId());

        if (cliente == null) {
            log.warn(
                    "Cliente no encontrado clienteId={}",
                    solicitud.getClienteId()
            );

            throw new RuntimeException("Cliente no encontrado");
        }

        if (Boolean.FALSE.equals(cliente.getActivo())) {
            log.warn(
                    "Intento de registro para cliente inactivo clienteId={}",
                    cliente.getId()
            );

            throw new RuntimeException("Cliente inactivo");
        }

        solicitud.setEstado("REGISTRADA");
        solicitud.setFechaRegistro(LocalDateTime.now());

        Solicitud solicitudGuardada =
                solicitudRepository.save(solicitud);

        log.info(
                "Solicitud registrada correctamente id={} clienteId={}",
                solicitudGuardada.getId(),
                solicitudGuardada.getClienteId()
        );

        return solicitudGuardada;
    }

    public List<Solicitud> listarSolicitudes() {
        return solicitudRepository.findAll();
    }

    public Solicitud obtenerPorId(Long id) {

        return solicitudRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Solicitud no encontrada"
                        )
                );
    }

    public Solicitud aprobar(Long id) {

        Solicitud solicitud = obtenerPorId(id);

        solicitud.setEstado("APROBADA");

        log.info("Solicitud aprobada id={}", id);

        return solicitudRepository.save(solicitud);
    }

    public Solicitud rechazar(Long id) {

        Solicitud solicitud = obtenerPorId(id);

        solicitud.setEstado("RECHAZADA");

        log.info("Solicitud rechazada id={}", id);

        return solicitudRepository.save(solicitud);
    }

public Solicitud fallbackCrearSolicitud(
        Solicitud solicitud,
        Throwable throwable) {

    System.out.println(
            "FALLBACK ACTIVADO: "
            + throwable.getClass().getName()
            + " - "
            + throwable.getMessage()
    );

    log.error(
            "FALLBACK ACTIVADO - tipoError={}, mensaje={}",
            throwable.getClass().getName(),
            throwable.getMessage(),
            throwable
    );

    solicitud.setEstado("PENDIENTE_VALIDACION");
    solicitud.setFechaRegistro(LocalDateTime.now());

    return solicitudRepository.save(solicitud);
}
}