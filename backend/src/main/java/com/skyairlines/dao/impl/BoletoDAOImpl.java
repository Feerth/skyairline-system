package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.BoletoDAO;
import com.skyairlines.model.entity.Boleto;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BoletoDAOImpl implements BoletoDAO {

    private Boleto mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_emision");
        return new Boleto(
                rs.getInt("id"),
                rs.getString("codigo_eticket"),
                rs.getInt("id_reserva"),
                rs.getInt("id_vuelo_asiento"),
                rs.getInt("id_pasajero"),
                rs.getString("estado"),
                ts != null ? ts.toLocalDateTime().atOffset(ZoneOffset.UTC) : null
        );
    }

    @Override
    public Optional<Boleto> findById(Integer id) throws SQLException {
        String sql = "SELECT id, codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision FROM boletos WHERE id = ?";
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
    public List<Boleto> findAll() throws SQLException {
        List<Boleto> list = new ArrayList<>();
        String sql = "SELECT id, codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision FROM boletos";
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
    public Boleto save(Boleto entity) throws SQLException {
        String sql = "INSERT INTO boletos (codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoEticket());
            ps.setInt(2, entity.getIdReserva());
            ps.setInt(3, entity.getIdVueloAsiento());
            ps.setInt(4, entity.getIdPasajero());
            ps.setString(5, entity.getEstado());
            ps.setTimestamp(6, entity.getFechaEmision() != null ? Timestamp.valueOf(entity.getFechaEmision().toLocalDateTime()) : null);
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
    public Boleto update(Boleto entity) throws SQLException {
        String sql = "UPDATE boletos SET codigo_eticket = ?, id_reserva = ?, id_vuelo_asiento = ?, id_pasajero = ?, estado = ?, fecha_emision = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoEticket());
            ps.setInt(2, entity.getIdReserva());
            ps.setInt(3, entity.getIdVueloAsiento());
            ps.setInt(4, entity.getIdPasajero());
            ps.setString(5, entity.getEstado());
            ps.setTimestamp(6, entity.getFechaEmision() != null ? Timestamp.valueOf(entity.getFechaEmision().toLocalDateTime()) : null);
            ps.setInt(7, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM boletos WHERE id = ?";
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
        String sql = "SELECT 1 FROM boletos WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM boletos";
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
    public Optional<Boleto> findByCodigoEticket(String codigo) throws SQLException {
        String sql = "SELECT id, codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision FROM boletos WHERE codigo_eticket = ?";
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
    public List<Boleto> findByVuelo(Integer idVuelo) throws SQLException {
        List<Boleto> list = new ArrayList<>();
        String sql = "SELECT b.id, b.codigo_eticket, b.id_reserva, b.id_vuelo_asiento, b.id_pasajero, b.estado, b.fecha_emision " +
                "FROM boletos b JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id WHERE va.id_vuelo = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVuelo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Boleto> findByPasajero(Integer idPasajero) throws SQLException {
        List<Boleto> list = new ArrayList<>();
        String sql = "SELECT id, codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision FROM boletos WHERE id_pasajero = ?";
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
}
