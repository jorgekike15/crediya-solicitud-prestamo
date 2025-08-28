package co.com.pragma.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateSolicitudDTO(
        @NotBlank(message = "El monto es obligatorio")
        @Min(value = 0, message = "El monto no puede ser menor que 0")
        String monto,
        @NotBlank(message = "El plazo es obligatorio")
        @Min(value = 1, message = "El plazo no puede ser menor que 1")
        String plazo,
        @NotBlank(message = "El email es obligatorio")
        String email,
        @NotBlank(message = "La fecha de solicitud es obligatoria")
        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "La fecha debe tener el formato YYYY-MM-DD")
        String fechaSolicitud,
        @NotBlank(message = "El tipo de prestamo es obligatorio")
        String idTipoPrestamo,
        @NotBlank(message = "El documento de identificación es obligatorio")
        @Pattern(regexp = "\\d+", message = "El documento de identificación solo debe contener números")
        String documentoIdentificacion) {
}
