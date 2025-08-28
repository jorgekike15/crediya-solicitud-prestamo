package co.com.pragma.model.solicitud;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private String monto;
    private String plazo;
    private String email;
    private int idEstado;
    private String fechaSolicitud;
    private String idTipoPrestamo;
    private String documentoIdentificacion;
    private double tasaInteres;
}
