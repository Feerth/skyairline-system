package com.skyairlines.dao.api;

import com.skyairlines.model.entity.AsientoAeronave;
import java.sql.SQLException;
import java.util.List;

public interface AsientoAeronaveDAO extends GenericDAO<AsientoAeronave, Integer> {
    List<AsientoAeronave> findByAeronaveId(Integer idAeronave) throws SQLException;
    List<AsientoAeronave> findByAeronaveAndClase(Integer idAeronave, String clase) throws SQLException;
    int countByAeronaveId(Integer idAeronave) throws SQLException;
}