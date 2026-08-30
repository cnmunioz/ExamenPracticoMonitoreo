package principal.Services;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolicitudCircuitBreakerListener {

    private static final Logger log =
            LoggerFactory.getLogger(SolicitudCircuitBreakerListener.class);

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final SolicitudRecoveryService solicitudRecoveryService;

    @PostConstruct
    public void registrarListener() {

        CircuitBreaker circuitBreaker =
                circuitBreakerRegistry.circuitBreaker("clienteServiceCB");

        circuitBreaker.getEventPublisher()
                .onStateTransition(event -> {

                    log.info(
                            "Circuit Breaker clienteServiceCB cambió de estado: {}",
                            event.getStateTransition()
                    );

                    /*
                     * Cuando cliente-service vuelve a estar disponible,
                     * intentamos recuperar las solicitudes que quedaron
                     * PENDIENTE_VALIDACION.
                     */
                    if (event.getStateTransition().getToState()
                            == CircuitBreaker.State.CLOSED) {

                        log.info(
                                "cliente-service recuperado. " +
                                "Procesando solicitudes pendientes..."
                        );

                        solicitudRecoveryService
                                .recuperarSolicitudesPendientes();
                    }
                });
    }
}