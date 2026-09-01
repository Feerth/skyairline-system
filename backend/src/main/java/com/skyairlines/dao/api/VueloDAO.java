package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Vuelo;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface VueloDAO extends GenericDAO<Vuelo, Integer> {
    Optional<Vuelo> findByCodigoVuelo(String codigo) throws SQLException;
    List<Vuelo> findByEstado(String estado) throws SQLException;
    List<Vuelo> findByFecha(OffsetDateTime fecha) throws SQLException;
    List<Vuelo> findByRuta(Integer idRuta) throws SQLException;
    List<Vuelo> findAllWithDetails() throws SQLException;
    int countByEstado(String estado) throws SQLException;
    int countTodayFlights() throws SQLException;
}