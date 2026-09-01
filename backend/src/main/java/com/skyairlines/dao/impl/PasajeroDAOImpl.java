package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.PasajeroDAO;
import com.skyairlines.model.entity.Pasajero;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasajeroDAOImpl implements PasajeroDAO {

    private Pasajero mapRow(ResultSet rs) throws SQLException {
        Date fechaNac = rs.getDate("fecha_nacimiento");
        return new Pasajero(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("doc_identidad"),
                fechaNac != null ? fechaNac.toLocalDate() : null,
                rs.getString("nacionalidad")
        );
    }

    @Override
    public Optional<Pasajero> findById(Integer id) throws SQLException {
        String sql = "SELECT id, nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad FROM pasajeros WHERE id = ?";
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
    public List<Pasajero> findAll() throws SQLException {
        List<Pasajero> list = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad FROM pasajeros";
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
    public Pasajero save(Pasajero entity) throws SQLException {
        String sql = "INSERT INTO pasajeros (nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getApellido());
            ps.setString(3, entity.getDocIdentidad());
            if (entity.getFechaNacimiento() != null) {
                ps.setDate(4, Date.valueOf(entity.getFechaNacimiento()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, entity.getNacionalidad());
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
    public Pasajero update(Pasajero entity) throws SQLException {
        String sql = "UPDATE pasajeros SET nombre = ?, apellido = ?, doc_identidad = ?, fecha_nacimiento = ?, nacionalidad = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setString(2, entity.getApellido());
            ps.setString(3, entity.getDocIdentidad());
            if (entity.getFechaNacimiento() != null) {
                ps.setDate(4, Date.valueOf(entity.getFechaNacimiento()));
            } else {
                ps.setNull(4, Types.DATE);
            }
            ps.setString(5, entity.getNacionalidad());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM pasajeros WHERE id = ?";
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
        String sql = "SELECT 1 FROM pasajeros WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM pasajeros";
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
    public Optional<Pasajero> findByDocIdentidad(String docIdentidad) throws SQLException {
        String sql = "SELECT id, nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad FROM pasajeros WHERE doc_identidad = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, docIdentidad);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Pasajero> findByNombre(String nombre) throws SQLException {
        List<Pasajero> list = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad FROM pasajeros WHERE nombre ILIKE ? OR apellido ILIKE ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
