package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Aeronave;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AeronaveDAO extends GenericDAO<Aeronave, Integer> {
    Optional<Aeronave> findByMatricula(String matricula) throws SQLException;
    List<Aeronave> findByEstado(String estado) throws SQLException;
    List<Aeronave> findActivas() throws SQLException;
}