package co.com.pragma.r2dbc;

import co.com.pragma.r2dbc.entity.SolicitudEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface SolicitudReactiveRepository extends ReactiveCrudRepository<SolicitudEntity, Integer>, ReactiveQueryByExampleExecutor<SolicitudEntity> {

    @Query("SELECT * FROM solicitudes WHERE id_estado IN (:estados) LIMIT :size OFFSET :offset")
    Flux<SolicitudEntity> findByIdEstadoInPaged(List<Integer> estados, int size, int offset);

    @Modifying
    @Query("UPDATE solicitudes SET id_estado = :idEstado WHERE id = :idSolicitud")
    Mono<Integer> updateIdEstadoByIdSolicitud(@Param("idEstado") Integer idEstado, @Param("idSolicitud") Integer idSolicitud);

    Flux<SolicitudEntity> findAllByDocumentoIdentificacion (String documentoIdentificacion);
}
