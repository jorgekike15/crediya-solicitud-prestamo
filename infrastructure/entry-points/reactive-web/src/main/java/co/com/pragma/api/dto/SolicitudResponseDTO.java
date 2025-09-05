package co.com.pragma.api.dto;

import java.util.List;

public record SolicitudResponseDTO(
        int size,
        List<SolicitudDTO> solicitudes) {
}
