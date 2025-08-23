package co.com.pragma.r2dbc.entity.enums;

import lombok.Getter;

@Getter
public enum EstadoUsuario {
    PENDIENTE(1),
    EN_REVISION(2),
    APROBADO(3),
    RECHAZADO(4);

    private final int codigo;

    EstadoUsuario(int codigo) {
        this.codigo = codigo;
    }

    public static EstadoUsuario fromCodigo(int codigo) {
        for (EstadoUsuario estado : values()) {
            if (estado.codigo == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }
}