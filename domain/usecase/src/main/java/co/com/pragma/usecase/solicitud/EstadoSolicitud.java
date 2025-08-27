package co.com.pragma.usecase.solicitud;

import lombok.Getter;

@Getter
public enum EstadoSolicitud {
    PENDIENTE_REVISION(0),
    REVISION_MANUAL(1),
    APROBADA(2),
    RECHAZADA(3),
    CANCELADA(4),
    DESEMBOLSADA(5),
    FINALIZADA(6);

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