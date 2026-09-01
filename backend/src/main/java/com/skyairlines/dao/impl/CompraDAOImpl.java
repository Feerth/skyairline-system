package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.CompraDAO;
import com.skyairlines.model.entity.Compra;

import java.math.BigDecimal;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompraDAOImpl implements CompraDAO {

    private Compra mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("fecha_compra");
        return new Compra(
                rs.getInt("id"),
                rs.getInt("id_cliente"),
                rs.getString("codigo_transaccion"),
                rs.getBigDecimal("monto_total"),
                rs.getString("estado_pago"),
                ts != null ? ts.toLocalDateTime().atOffset(ZoneOffset.UTC) : null
        );
    }

    @Override
    public Optional<Compra> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra FROM compras WHERE id = ?";
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
    public List<Compra> findAll() throws SQLException {
        List<Compra> list = new ArrayList<>();
        String sql = "SELECT id, id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra FROM compras";
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
    public Compra save(Compra entity) throws SQLException {
        String sql = "INSERT INTO compras (id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCliente());
            ps.setString(2, entity.getCodigoTransaccion());
            ps.setBigDecimal(3, entity.getMontoTotal());
            ps.setString(4, entity.getEstadoPago());
            ps.setTimestamp(5, entity.getFechaCompra() != null ? Timestamp.valueOf(entity.getFechaCompra().toLocalDateTime()) : null);
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
    public Compra update(Compra entity) throws SQLException {
        String sql = "UPDATE compras SET id_cliente = ?, codigo_transaccion = ?, monto_total = ?, estado_pago = ?, fecha_compra = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdCliente());
            ps.setString(2, entity.getCodigoTransaccion());
            ps.setBigDecimal(3, entity.getMontoTotal());
            ps.setString(4, entity.getEstadoPago());
            ps.setTimestamp(5, entity.getFechaCompra() != null ? Timestamp.valueOf(entity.getFechaCompra().toLocalDateTime()) : null);
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM compras WHERE id = ?";
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
        String sql = "SELECT 1 FROM compras WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM compras";
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
    public Optional<Compra> findByCodigoTransaccion(String codigo) throws SQLException {
        String sql = "SELECT id, id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra FROM compras WHERE codigo_transaccion = ?";
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
    public List<Compra> findByCliente(Integer idCliente) throws SQLException {
        List<Compra> list = new ArrayList<>();
        String sql = "SELECT id, id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra FROM compras WHERE id_cliente = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Compra> findByEstado(String estado) throws SQLException {
        List<Compra> list = new ArrayList<>();
        String sql = "SELECT id, id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra FROM compras WHERE estado_pago = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
