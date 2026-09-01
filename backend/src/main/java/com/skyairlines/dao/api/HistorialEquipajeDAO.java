package com.skyairlines.dao.api;

import com.skyairlines.model.entity.HistorialEquipaje;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;

public interface HistorialEquipajeDAO extends GenericDAO<HistorialEquipaje, Integer> {
    List<HistorialEquipaje> findByEquipaje(Integer idEquipaje) throws SQLException;
    List<HistorialEquipaje> findByFechaRange(OffsetDateTime desde, OffsetDateTime hasta) throws SQLException;
}