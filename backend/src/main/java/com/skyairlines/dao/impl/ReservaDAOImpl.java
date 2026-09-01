package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.ReservaDAO;
import com.skyairlines.model.entity.Reserva;
import com.skyairlines.model.enums.EstadoReserva;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAOImpl implements ReservaDAO {

    private Reserva mapRow(ResultSet rs) throws SQLException {
        Timestamp tsExpira = rs.getTimestamp("expira_en");
        return new Reserva(
                rs.getInt("id"),
                rs.getInt("id_compra"),
                rs.getInt("id_vuelo_asiento"),
                rs.getInt("id_pasajero"),
                tsExpira != null ? tsExpira.toLocalDateTime().atOffset(ZoneOffset.UTC) : null,
                EstadoReserva.fromDbValue(rs.getString("estado"))
        );
    }

    @Override
    public java.util.Optional<Reserva> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado FROM reservas WHERE id = ?";
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
    public List<Reserva> findAll() throws SQLException {
        List<Reserva> list = new ArrayList<>();
        String sql = "SELECT id, id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado FROM reservas";
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
    public Reserva save(Reserva entity) throws SQLException {
        String sql = "INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCompra());
            ps.setInt(2, entity.getIdVueloAsiento());
            ps.setInt(3, entity.getIdPasajero());
            ps.setTimestamp(4, entity.getExpiraEn() != null ? Timestamp.valueOf(entity.getExpiraEn().toLocalDateTime()) : null);
            ps.setString(5, entity.getEstado().getDbValue());
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
    public Reserva update(Reserva entity) throws SQLException {
        String sql = "UPDATE reservas SET id_compra = ?, id_vuelo_asiento = ?, id_pasajero = ?, expira_en = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCompra());
            ps.setInt(2, entity.getIdVueloAsiento());
            ps.setInt(3, entity.getIdPasajero());
            ps.setTimestamp(4, entity.getExpiraEn() != null ? Timestamp.valueOf(entity.getExpiraEn().toLocalDateTime()) : null);
            ps.setString(5, entity.getEstado().getDbValue());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM reservas WHERE id = ?";
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
        String sql = "SELECT 1 FROM reservas WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM reservas";
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
    public List<Reserva> findByVueloAsiento(Integer idVueloAsiento) throws SQLException {
        List<Reserva> list = new ArrayList<>();
        String sql = "SELECT id, id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado FROM reservas WHERE id_vuelo_asiento = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVueloAsiento);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Reserva> findByPasajero(Integer idPasajero) throws SQLException {
        List<Reserva> list = new ArrayList<>();
        String sql = "SELECT id, id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado FROM reservas WHERE id_pasajero = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPasajero);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Reserva> findExpiradas() throws SQLException {
        List<Reserva> list = new ArrayList<>();
        String sql = "SELECT id, id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado FROM reservas WHERE expira_en < CURRENT_TIMESTAMP AND estado = 'PENDIENTE'";
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
