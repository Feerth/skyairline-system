package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Ruta;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RutaDAO extends GenericDAO<Ruta, Integer> {
    Optional<Ruta> findByCodigoRuta(String codigo) throws SQLException;
    List<Ruta> findByAeropuertoOrigen(Integer idOrigen) throws SQLException;
    List<Ruta> findByAeropuertoDestino(Integer idDestino) throws SQLException;
    List<Ruta> findAllWithAeropuertos() throws SQLException;
}