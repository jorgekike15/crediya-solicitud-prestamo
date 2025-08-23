package co.com.pragma.api;

public record CreateSolicitudDTO(
        String idUsuario,
        String monto,
        String plazo,
        String email,
        String idEstadoSolicitud,
        String fechaSolicitud,
        String idTipoPrestamo) {
}
