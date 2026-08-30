package principal.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import principal.Models.Solicitud;
import principal.Services.SolicitudService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @PostMapping
    public ResponseEntity<Solicitud> crear(
            @RequestBody Solicitud solicitud) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                    solicitudService.registrarSolicitud(solicitud)
                );
    }

    @GetMapping
    public List<Solicitud> listar() {
        return solicitudService.listarSolicitudes();
    }

    @GetMapping("/{id}")
    public Solicitud obtener(@PathVariable Long id) {
        return solicitudService.obtenerPorId(id);
    }

    @PutMapping("/{id}/aprobar")
    public Solicitud aprobar(@PathVariable Long id) {
        return solicitudService.aprobar(id);
    }

    @PutMapping("/{id}/rechazar")
    public Solicitud rechazar(@PathVariable Long id) {
        return solicitudService.rechazar(id);
    }
}