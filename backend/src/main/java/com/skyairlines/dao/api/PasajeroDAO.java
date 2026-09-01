package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Pasajero;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PasajeroDAO extends GenericDAO<Pasajero, Integer> {
    Optional<Pasajero> findByDocIdentidad(String docIdentidad) throws SQLException;
    List<Pasajero> findByNombre(String nombre) throws SQLException;
}