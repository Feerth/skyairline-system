package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Aeropuerto;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AeropuertoDAO extends GenericDAO<Aeropuerto, Integer> {
    Optional<Aeropuerto> findByCodigoIata(String codigo) throws SQLException;
    List<Aeropuerto> findByCiudad(String ciudad) throws SQLException;
    List<Aeropuerto> findByPais(String pais) throws SQLException;
}