package co.com.pragma.model.messagesender;

import co.com.pragma.model.solicitud.Solicitud;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class MessageAutoValidation {

    private int idSolicitud;
    private double salarioCliente;
    private double montoSolicitado;
    private double tasaInteresMensual;
    private int plazoMeses;
    private List<Solicitud> prestamosActivos;

}
