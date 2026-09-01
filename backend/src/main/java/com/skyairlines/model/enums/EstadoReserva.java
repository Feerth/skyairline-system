package com.skyairlines.model.enums;

public enum EstadoReserva {
    PENDIENTE("PENDIENTE"),
    CONFIRMADA("CONFIRMADA"),
    EXPIRADA("EXPIRADA"),
    CANCELADA("CANCELADA");

    private final String dbValue;

    EstadoReserva(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static EstadoReserva fromDbValue(String dbValue) {
        for (EstadoReserva estado : values()) {
            if (estado.dbValue.equals(dbValue)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Unknown EstadoReserva: " + dbValue);
    }
}