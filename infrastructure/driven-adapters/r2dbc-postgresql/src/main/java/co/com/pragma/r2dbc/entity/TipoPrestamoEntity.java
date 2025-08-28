package co.com.pragma.r2dbc.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("tipo_prestamo")
public class TipoPrestamoEntity {

    @Id
    private int id;
    private String nombre;
    private Double monto_minimo;
    private Double monto_maximo;
    private Double tasa_interes;
    private int validacion_automatica;
}
