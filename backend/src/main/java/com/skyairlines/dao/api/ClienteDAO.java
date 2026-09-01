package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClienteDAO extends GenericDAO<Cliente, Integer> {
    Optional<Cliente> findByDocIdentidad(String docIdentidad) throws SQLException;
    Optional<Cliente> findByIdUsuario(Integer idUsuario) throws SQLException;
    List<Cliente> findByNombre(String nombre) throws SQLException;
    List<Cliente> findAllWithUsuario() throws SQLException;
}