package co.com.pragma.model.solicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private String id;
    private String idUsuario;
    private String monto;
    private String plazo;
    private String email;
    private String idEstadoSolicitud;
    private String fechaSolicitud;
    private String idTipoPrestamo;
}
