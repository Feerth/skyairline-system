package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.EmpleadoDAO;
import com.skyairlines.model.entity.Empleado;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmpleadoDAOImpl implements EmpleadoDAO {

    private Empleado mapRow(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getInt("id"),
                rs.getInt("id_usuario"),
                rs.getString("codigo_empleado"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("cargo")
        );
    }

    @Override
    public Optional<Empleado> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_usuario, codigo_empleado, nombre, apellido, cargo FROM empleados WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Empleado> findAll() throws SQLException {
        List<Empleado> list = new ArrayList<>();
        String sql = "SELECT id, id_usuario, codigo_empleado, nombre, apellido, cargo FROM empleados";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public Empleado save(Empleado entity) throws SQLException {
        String sql = "INSERT INTO empleados (id_usuario, codigo_empleado, nombre, apellido, cargo) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (entity.getIdUsuario() != null) {
                ps.setInt(1, entity.getIdUsuario());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, entity.getCodigoEmpleado());
            ps.setString(3, entity.getNombre());
            ps.setString(4, entity.getApellido());
            ps.setString(5, entity.getCargo());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    entity.setId(rs.getInt(1));
                }
            }
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public Empleado update(Empleado entity) throws SQLException {
        String sql = "UPDATE empleados SET id_usuario = ?, codigo_empleado = ?, nombre = ?, apellido = ?, cargo = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (entity.getIdUsuario() != null) {
                ps.setInt(1, entity.getIdUsuario());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setString(2, entity.getCodigoEmpleado());
            ps.setString(3, entity.getNombre());
            ps.setString(4, entity.getApellido());
            ps.setString(5, entity.getCargo());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
            return rows > 0;
        }
    }

    @Override
    public boolean existsById(Integer id) throws SQLException {
        String sql = "SELECT 1 FROM empleados WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM empleados";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public Optional<Empleado> findByCodigoEmpleado(String codigo) throws SQLException {
        String sql = "SELECT id, id_usuario, codigo_empleado, nombre, apellido, cargo FROM empleados WHERE codigo_empleado = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Empleado> findByIdUsuario(Integer idUsuario) throws SQLException {
        String sql = "SELECT id, id_usuario, codigo_empleado, nombre, apellido, cargo FROM empleados WHERE id_usuario = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Empleado> findByCargo(String cargo) throws SQLException {
        List<Empleado> list = new ArrayList<>();
        String sql = "SELECT id, id_usuario, codigo_empleado, nombre, apellido, cargo FROM empleados WHERE cargo = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cargo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Empleado> findAllWithUsuario() throws SQLException {
        List<Empleado> list = new ArrayList<>();
        String sql = "SELECT e.id, e.id_usuario, e.codigo_empleado, e.nombre, e.apellido, e.cargo, u.email " +
                "FROM empleados e JOIN usuarios u ON e.id_usuario = u.id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }
}
