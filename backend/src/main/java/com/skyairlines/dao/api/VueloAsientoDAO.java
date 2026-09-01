package com.skyairlines.dao.api;

import com.skyairlines.model.entity.InventarioAsientoDTO;
import com.skyairlines.model.entity.VueloAsiento;
import com.skyairlines.model.enums.EstadoAsientoVuelo;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface VueloAsientoDAO extends GenericDAO<VueloAsiento, Integer> {
    List<VueloAsiento> findByVueloId(Integer idVuelo) throws SQLException;
    List<VueloAsiento> findByVueloAndEstado(Integer idVuelo, EstadoAsientoVuelo estado) throws SQLException;
    Optional<VueloAsiento> findByVueloAndAsiento(Integer idVuelo, Integer idAsiento) throws SQLException;
    int countByVueloAndEstado(Integer idVuelo, EstadoAsientoVuelo estado) throws SQLException;
    boolean actualizarEstadoConLock(Integer id, EstadoAsientoVuelo nuevoEstado, int expectedVersion) throws SQLException;
    void cancelarRandomReserva(Integer idVuelo) throws SQLException;
    List<InventarioAsientoDTO> getInventarioByVuelo(Integer idVuelo) throws SQLException;
}