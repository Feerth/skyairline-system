package com.skyairlines.model.enums;

public enum CategoriaEquipaje {
    LIGERO("LIGERO"),
    NORMAL("NORMAL"),
    PESADO("PESADO");

    private final String dbValue;

    CategoriaEquipaje(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    public static CategoriaEquipaje fromDbValue(String dbValue) {
        for (CategoriaEquipaje cat : values()) {
            if (cat.dbValue.equals(dbValue)) {
                return cat;
            }
        }
        throw new IllegalArgumentException("Unknown CategoriaEquipaje: " + dbValue);
    }
}