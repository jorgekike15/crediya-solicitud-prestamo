package co.com.pragma.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateSolicitudDTO(
        @NotBlank(message = "El monto es obligatorio")
        @Min(value = 0, message = "El monto no puede ser menor que 0")
        String monto,
        @NotBlank(message = "El plazo es obligatorio")
        String plazo,
        @NotBlank(message = "El email es obligatorio")
        String email,
        @NotBlank(message = "La fecha de solicitud es obligatoria")
        String fechaSolicitud,
        @NotBlank(message = "El tipo de prestamo es obligatorio")
        String idTipoPrestamo,
        @NotBlank(message = "El documento de identificación es obligatorio")
        String documentoIdentificacion) {
}
