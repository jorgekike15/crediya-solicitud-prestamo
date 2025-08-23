package co.com.pragma.api;

public record SolicitudResponseDTO(
        String idUsuario,
        String monto,
        String plazo,
        String email,
        String idEstadoSolicitud,
        String fechaSolicitud,
        String idTipoPrestamo) {
}
