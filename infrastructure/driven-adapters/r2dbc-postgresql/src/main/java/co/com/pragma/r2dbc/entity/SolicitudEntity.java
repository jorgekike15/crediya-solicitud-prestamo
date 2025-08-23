package co.com.pragma.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@Table("solicitudes")
public class SolicitudEntity {

    @Id
    private int id;
    private double monto;
    private int plazo;
    private String email;
    private int idEstado;
    private int idTipoPrestamo;
    private String documentoIdentificacion;
    private LocalDate fechaSolicitud;

}
