package co.com.pragma.model.tipoprestamo;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamo {

    private int id;
    private String nombre;
    private Double monto_minimo;
    private Double monto_maximo;
    private Double tasa_interes;
    private int validacion_automatica;
}
