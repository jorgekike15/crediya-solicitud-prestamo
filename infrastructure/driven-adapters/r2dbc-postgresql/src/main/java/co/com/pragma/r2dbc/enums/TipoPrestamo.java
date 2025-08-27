package co.com.pragma.r2dbc.enums;

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

}