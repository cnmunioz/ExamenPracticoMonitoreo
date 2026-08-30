package principal.Services;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import principal.Configs.ClienteClient;
import principal.Models.Cliente;
import principal.Models.Solicitud;
import principal.Repositories.SolicitudRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudRecoveryService {

    private static final Logger log =
            LoggerFactory.getLogger(SolicitudRecoveryService.class);

    private final SolicitudRepository solicitudRepository;
    private final ClienteClient clienteClient;

    public void recuperarSolicitudesPendientes() {

        log.info("Recuperando solicitudes pendientes de validación...");

        List<Solicitud> solicitudesPendientes =
                solicitudRepository.findByEstado("PENDIENTE_VALIDACION");

        for (Solicitud solicitud : solicitudesPendientes) {

            log.info(
                    "Reintentando validación de solicitud id={} clienteId={}",
                    solicitud.getId(),
                    solicitud.getClienteId()
            );

            try {

                Cliente cliente =
                        clienteClient.obtenerCliente(
                                solicitud.getClienteId()
                        );

                if (cliente != null &&
                    Boolean.TRUE.equals(cliente.getActivo())) {

                    solicitud.setEstado("REGISTRADA");

                    solicitudRepository.save(solicitud);

                    log.info(
                            "Solicitud recuperada correctamente id={}",
                            solicitud.getId()
                    );

                } else {

                    solicitud.setEstado("RECHAZADA");

                    solicitudRepository.save(solicitud);

                    log.warn(
                            "Solicitud rechazada porque cliente no está activo. solicitudId={} clienteId={}",
                            solicitud.getId(),
                            solicitud.getClienteId()
                    );
                }

            } catch (FeignException.NotFound ex) {

                solicitud.setEstado("RECHAZADA");

                solicitudRepository.save(solicitud);

                log.warn(
                        "Cliente inexistente. Solicitud rechazada solicitudId={} clienteId={}",
                        solicitud.getId(),
                        solicitud.getClienteId()
                );

            } catch (FeignException ex) {

                log.error(
                        "No fue posible recuperar solicitud id={}. cliente-service continúa sin responder.",
                        solicitud.getId()
                );
            }
        }
    }
}