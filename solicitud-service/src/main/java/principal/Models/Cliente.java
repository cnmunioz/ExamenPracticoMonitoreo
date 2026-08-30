package principal.Models;

import lombok.Data;

@Data
public class Cliente {

    private Long id;
    private String nombre;
    private String documento;
    private String correo;
    private String telefono;
    private Boolean activo;
}