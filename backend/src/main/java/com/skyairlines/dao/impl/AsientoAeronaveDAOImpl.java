package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.AsientoAeronaveDAO;
import com.skyairlines.model.entity.AsientoAeronave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsientoAeronaveDAOImpl implements AsientoAeronaveDAO {

    private AsientoAeronave mapRow(ResultSet rs) throws SQLException {
        return new AsientoAeronave(
                rs.getInt("id"),
                rs.getInt("id_aeronave"),
                rs.getString("codigo_asiento"),
                rs.getString("clase"),
                rs.getBoolean("es_emergencia")
        );
    }

    @Override
    public Optional<AsientoAeronave> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_aeronave, codigo_asiento, clase, es_emergencia FROM asientos_aeronave WHERE id = ?";
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
    public List<AsientoAeronave> findAll() throws SQLException {
        List<AsientoAeronave> list = new ArrayList<>();
        String sql = "SELECT id, id_aeronave, codigo_asiento, clase, es_emergencia FROM asientos_aeronave";
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
    public AsientoAeronave save(AsientoAeronave entity) throws SQLException {
        String sql = "INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdAeronave());
            ps.setString(2, entity.getCodigoAsiento());
            ps.setString(3, entity.getClase());
            ps.setBoolean(4, entity.getEsEmergencia());
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
    public AsientoAeronave update(AsientoAeronave entity) throws SQLException {
        String sql = "UPDATE asientos_aeronave SET id_aeronave = ?, codigo_asiento = ?, clase = ?, es_emergencia = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdAeronave());
            ps.setString(2, entity.getCodigoAsiento());
            ps.setString(3, entity.getClase());
            ps.setBoolean(4, entity.getEsEmergencia());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM asientos_aeronave WHERE id = ?";
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
        String sql = "SELECT 1 FROM asientos_aeronave WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM asientos_aeronave";
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
    public List<AsientoAeronave> findByAeronaveId(Integer idAeronave) throws SQLException {
        List<AsientoAeronave> list = new ArrayList<>();
        String sql = "SELECT id, id_aeronave, codigo_asiento, clase, es_emergencia FROM asientos_aeronave WHERE id_aeronave = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAeronave);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<AsientoAeronave> findByAeronaveAndClase(Integer idAeronave, String clase) throws SQLException {
        List<AsientoAeronave> list = new ArrayList<>();
        String sql = "SELECT id, id_aeronave, codigo_asiento, clase, es_emergencia FROM asientos_aeronave WHERE id_aeronave = ? AND clase = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAeronave);
            ps.setString(2, clase);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public int countByAeronaveId(Integer idAeronave) throws SQLException {
        String sql = "SELECT COUNT(*) FROM asientos_aeronave WHERE id_aeronave = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAeronave);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
}
