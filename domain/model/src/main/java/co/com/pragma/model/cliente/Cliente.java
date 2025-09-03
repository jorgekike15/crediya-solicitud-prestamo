package co.com.pragma.model.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Cliente {

    private String nombre;
    private String apellido;
    private String email;
    private String documentoIdentificacion;
    private String telefono;
    private double salarioBase;
}
