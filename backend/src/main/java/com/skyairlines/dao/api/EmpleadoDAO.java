package com.skyairlines.dao.api;

import com.skyairlines.model.entity.Empleado;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface EmpleadoDAO extends GenericDAO<Empleado, Integer> {
    Optional<Empleado> findByCodigoEmpleado(String codigo) throws SQLException;
    Optional<Empleado> findByIdUsuario(Integer idUsuario) throws SQLException;
    List<Empleado> findByCargo(String cargo) throws SQLException;
    List<Empleado> findAllWithUsuario() throws SQLException;
}