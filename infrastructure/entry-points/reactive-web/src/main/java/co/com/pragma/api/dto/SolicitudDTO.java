package co.com.pragma.api.dto;

public record SolicitudDTO(
        String monto,
        String plazo,
        String email,
        String estadoSolicitud,
        String fechaSolicitud,
        String tipoPrestamo,
        String documentoIdentificacion,
        double tasaInteres)
{}

