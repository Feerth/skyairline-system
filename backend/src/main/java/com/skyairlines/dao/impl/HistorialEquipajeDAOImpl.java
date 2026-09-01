package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.HistorialEquipajeDAO;
import com.skyairlines.model.entity.HistorialEquipaje;
import com.skyairlines.model.enums.EstadoEquipaje;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class HistorialEquipajeDAOImpl implements HistorialEquipajeDAO {

    private HistorialEquipaje mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_registro");
        return new HistorialEquipaje(
                rs.getInt("id"),
                rs.getInt("id_equipaje"),
                EstadoEquipaje.fromDbValue(rs.getString("estado")),
                rs.getInt("ubicacion_aeropuerto_id"),
                rs.getString("cinta_carrusel"),
                rs.getString("observaciones"),
                rs.getInt("id_empleado"),
                ts != null ? ts.toLocalDateTime().atOffset(ZoneOffset.UTC) : null
        );
    }

    @Override
    public java.util.Optional<HistorialEquipaje> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro FROM historial_equipaje WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return java.util.Optional.of(mapRow(rs));
                }
            }
        }
        return java.util.Optional.empty();
    }

    @Override
    public List<HistorialEquipaje> findAll() throws SQLException {
        List<HistorialEquipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro FROM historial_equipaje";
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
    public HistorialEquipaje save(HistorialEquipaje entity) throws SQLException {
        String sql = "INSERT INTO historial_equipaje (id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdEquipaje());
            ps.setString(2, entity.getEstado().getDbValue());
            ps.setInt(3, entity.getUbicacionAeropuertoId());
            ps.setString(4, entity.getCintaCarrusel());
            ps.setString(5, entity.getObservaciones());
            ps.setInt(6, entity.getIdEmpleado());
            ps.setTimestamp(7, entity.getFechaRegistro() != null ? Timestamp.valueOf(entity.getFechaRegistro().toLocalDateTime()) : null);
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
    public HistorialEquipaje update(HistorialEquipaje entity) throws SQLException {
        String sql = "UPDATE historial_equipaje SET id_equipaje = ?, estado = ?, ubicacion_aeropuerto_id = ?, cinta_carrusel = ?, observaciones = ?, id_empleado = ?, fecha_registro = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdEquipaje());
            ps.setString(2, entity.getEstado().getDbValue());
            ps.setInt(3, entity.getUbicacionAeropuertoId());
            ps.setString(4, entity.getCintaCarrusel());
            ps.setString(5, entity.getObservaciones());
            ps.setInt(6, entity.getIdEmpleado());
            ps.setTimestamp(7, entity.getFechaRegistro() != null ? Timestamp.valueOf(entity.getFechaRegistro().toLocalDateTime()) : null);
            ps.setInt(8, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM historial_equipaje WHERE id = ?";
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
        String sql = "SELECT 1 FROM historial_equipaje WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM historial_equipaje";
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
    public List<HistorialEquipaje> findByEquipaje(Integer idEquipaje) throws SQLException {
        List<HistorialEquipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro FROM historial_equipaje WHERE id_equipaje = ? ORDER BY fecha_registro DESC";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipaje);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<HistorialEquipaje> findByFechaRange(OffsetDateTime desde, OffsetDateTime hasta) throws SQLException {
        List<HistorialEquipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro FROM historial_equipaje WHERE fecha_registro BETWEEN ? AND ? ORDER BY fecha_registro DESC";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde.toLocalDateTime()));
            ps.setTimestamp(2, Timestamp.valueOf(hasta.toLocalDateTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
