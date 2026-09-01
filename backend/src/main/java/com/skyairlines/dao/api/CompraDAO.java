package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Compra;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CompraDAO extends GenericDAO<Compra, Integer> {
    Optional<Compra> findByCodigoTransaccion(String codigo) throws SQLException;
    List<Compra> findByCliente(Integer idCliente) throws SQLException;
    List<Compra> findByEstado(String estado) throws SQLException;
}