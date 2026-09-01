package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Boleto;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BoletoDAO extends GenericDAO<Boleto, Integer> {
    Optional<Boleto> findByCodigoEticket(String codigo) throws SQLException;
    List<Boleto> findByVuelo(Integer idVuelo) throws SQLException;
    List<Boleto> findByPasajero(Integer idPasajero) throws SQLException;
}