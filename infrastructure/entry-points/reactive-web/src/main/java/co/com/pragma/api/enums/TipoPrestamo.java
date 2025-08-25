package co.com.pragma.api.enums;

public enum TipoPrestamo {
    PERSONAL(1),
    VEHICULO(2),
    HIPOTECARIO(3),
    EDUCATIVO(4),
    LIBRE_INVERSION(5),
    EMPRESARIAL(6),
    NOMINA(7);

    private final int id;

    TipoPrestamo(int id) {
        this.id = id;
    }

    public static TipoPrestamo fromCodigo(int id) {
        for (TipoPrestamo tipoPrestamo : values()) {
            if (tipoPrestamo.id == id) {
                return tipoPrestamo;
            }
        }
        throw new IllegalArgumentException("Código de estado inválido: " + id);
    }

}