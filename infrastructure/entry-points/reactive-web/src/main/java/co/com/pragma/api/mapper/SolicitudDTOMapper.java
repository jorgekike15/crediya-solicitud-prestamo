package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateSolicitudDTO;
import co.com.pragma.api.dto.SolicitudDTO;
import co.com.pragma.api.dto.SolicitudResponseDTO;
import co.com.pragma.api.enums.EstadoSolicitud;
import co.com.pragma.api.enums.TipoPrestamo;
import co.com.pragma.model.solicitud.Solicitud;
import co.com.pragma.model.solicitud.SolicitudResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SolicitudDTOMapper {

    @Mapping(target = "estadoSolicitud", source = "idEstado", qualifiedByName = "mapEstadoToResponse")
    @Mapping(target = "tipoPrestamo", source = "idTipoPrestamo", qualifiedByName = "mapTipoPrestamoToResponse")
    SolicitudDTO toResponse(Solicitud solicitud);

    @Named("mapEstadoToResponse")
    default String mapEstadoToResponse(int idEstado) {
        return EstadoSolicitud.fromCodigo(idEstado).name();
    }

    @Named("mapTipoPrestamoToResponse")
    default String mapTipoPrestamoToResponse(int idTipoPrestamo) {
        return TipoPrestamo.fromCodigo(idTipoPrestamo).name();
    }


    @Mapping(target = "idEstado", expression = "java(1)")
    Solicitud toModelCreate(CreateSolicitudDTO createSolicitudDTO);

    default SolicitudResponseDTO toResponseDTO(SolicitudResponse response) {
        List<SolicitudDTO> dtos = response.getSolicitudes() == null
                ? Collections.emptyList()
                : response.getSolicitudes().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return new SolicitudResponseDTO(response.getSize(), dtos);
    }

}
