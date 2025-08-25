package co.com.pragma.api;

public record SolicitudResponseDTO(
        String monto,
        String plazo,
        String email,
        String estadoSolicitud,
        String fechaSolicitud,
        String tipoPrestamo,
        String documentoIdentificacion) {
}
