package co.com.pragma.api.mapper;

import co.com.pragma.api.dto.CreateSolicitudDTO;
import co.com.pragma.api.dto.SolicitudResponseDTO;
import co.com.pragma.api.enums.EstadoSolicitud;
import co.com.pragma.api.enums.TipoPrestamo;
import co.com.pragma.model.solicitud.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SolicitudDTOMapper {

    @Mapping(target = "estadoSolicitud", source = "idEstado", qualifiedByName = "mapEstadoToResponse")
    @Mapping(target = "tipoPrestamo", source = "idTipoPrestamo", qualifiedByName = "mapTipoPrestamoToResponse")
    SolicitudResponseDTO toResponse(Solicitud solicitud);

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



}
