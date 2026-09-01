package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Reserva;
import java.sql.SQLException;
import java.util.List;

public interface ReservaDAO extends GenericDAO<Reserva, Integer> {
    List<Reserva> findByVueloAsiento(Integer idVueloAsiento) throws SQLException;
    List<Reserva> findByPasajero(Integer idPasajero) throws SQLException;
    List<Reserva> findExpiradas() throws SQLException;
}