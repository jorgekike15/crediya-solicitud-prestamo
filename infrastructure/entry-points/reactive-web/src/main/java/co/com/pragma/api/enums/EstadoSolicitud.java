package co.com.pragma.api.enums;

import lombok.Getter;

@Getter
public enum EstadoSolicitud {
    PENDIENTE(1),
    EN_REVISION(2),
    APROBADO(3),
    RECHAZADO(4);

    private final int codigo;

    EstadoSolicitud(int codigo) {
        this.codigo = codigo;
    }

    public static EstadoSolicitud fromCodigo(int codigo) {
        for (EstadoSolicitud estado : values()) {
            if (estado.codigo == codigo) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + codigo);
    }
}