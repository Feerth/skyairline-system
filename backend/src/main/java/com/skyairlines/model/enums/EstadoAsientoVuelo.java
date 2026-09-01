package com.skyairlines.model.enums;

public enum EstadoAsientoVuelo {
    DISPONIBLE("DISPONIBLE"),
    RESERVADO("RESERVADO"),
    VENDIDO("VENDIDO"),
    BLOQUEADO("BLOQUEADO");

    private final String dbValue;

    EstadoAsientoVuelo(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static EstadoAsientoVuelo fromDbValue(String dbValue) {
        for (EstadoAsientoVuelo estado : values()) {
            if (estado.dbValue.equals(dbValue)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Unknown EstadoAsientoVuelo: " + dbValue);
    }
}