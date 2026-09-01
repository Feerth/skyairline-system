package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Usuario;
import com.skyairlines.model.enums.RolUsuario;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email) throws SQLException;
    Optional<Usuario> findByEmailAndPassword(String email, String passwordHash) throws SQLException;
    List<Usuario> findByRol(RolUsuario rol) throws SQLException;
    List<Usuario> findActivos() throws SQLException;
}