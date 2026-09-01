package com.skyairlines.model.enums;

public enum EstadoEquipaje {
    REGISTRADO("REGISTRADO"),
    CONTROL_SEGURIDAD("CONTROL_SEGURIDAD"),
    EMBARCADO("EMBARCADO"),
    DESCARGADO("DESCARGADO"),
    DISTRIBUCION_CINTA("DISTRIBUCION_CINTA"),
    ENTREGADO("ENTREGADO"),
    RETENIDO("RETENIDO");

    private final String dbValue;

    EstadoEquipaje(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static EstadoEquipaje fromDbValue(String dbValue) {
        for (EstadoEquipaje estado : values()) {
            if (estado.dbValue.equals(dbValue)) {
                return estado;
            }
        }
        throw new IllegalArgumentException("Unknown EstadoEquipaje: " + dbValue);
    }

    public static EstadoEquipaje getNextState(EstadoEquipaje current) {
        switch (current) {
            case REGISTRADO: return CONTROL_SEGURIDAD;
            case CONTROL_SEGURIDAD: return EMBARCADO;
            case EMBARCADO: return DESCARGADO;
            case DESCARGADO: return DISTRIBUCION_CINTA;
            case DISTRIBUCION_CINTA: return ENTREGADO;
            default: return current;
        }
    }

    public static EstadoEquipaje getPreviousState(EstadoEquipaje current) {
        switch (current) {
            case CONTROL_SEGURIDAD: return REGISTRADO;
            case EMBARCADO: return CONTROL_SEGURIDAD;
            case DESCARGADO: return EMBARCADO;
            case DISTRIBUCION_CINTA: return DESCARGADO;
            case ENTREGADO: return DISTRIBUCION_CINTA;
            default: return current;
        }
    }
}