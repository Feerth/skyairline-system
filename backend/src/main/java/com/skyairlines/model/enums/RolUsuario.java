package com.skyairlines.model.enums;

public enum RolUsuario {
    ADMINISTRADOR("ADMINISTRADOR"),
    OPERACIONES("OPERACIONES"),
    CLIENTE("CLIENTE");

    private final String dbValue;

    RolUsuario(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static RolUsuario fromDbValue(String dbValue) {
        for (RolUsuario rol : values()) {
            if (rol.dbValue.equals(dbValue)) {
                return rol;
            }
        }
        throw new IllegalArgumentException("Unknown RolUsuario: " + dbValue);
    }
}