package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.AeronaveDAO;
import com.skyairlines.model.entity.Aeronave;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeronaveDAOImpl implements AeronaveDAO {

    private Aeronave mapRow(ResultSet rs) throws SQLException {
        return new Aeronave(
                rs.getInt("id"),
                rs.getString("matricula"),
                rs.getString("modelo"),
                rs.getInt("capacidad_pasajeros"),
                rs.getString("estado")
        );
    }

    @Override
    public Optional<Aeronave> findById(Integer id) throws SQLException {
        String sql = "SELECT id, matricula, modelo, capacidad_pasajeros, estado FROM aeronaves WHERE id = ?";
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
    public List<Aeronave> findAll() throws SQLException {
        List<Aeronave> list = new ArrayList<>();
        String sql = "SELECT id, matricula, modelo, capacidad_pasajeros, estado FROM aeronaves";
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
    public Aeronave save(Aeronave entity) throws SQLException {
        String sql = "INSERT INTO aeronaves (matricula, modelo, capacidad_pasajeros, estado) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getMatricula());
            ps.setString(2, entity.getModelo());
            ps.setInt(3, entity.getCapacidadPasajeros());
            ps.setString(4, entity.getEstado());
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
    public Aeronave update(Aeronave entity) throws SQLException {
        String sql = "UPDATE aeronaves SET matricula = ?, modelo = ?, capacidad_pasajeros = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getMatricula());
            ps.setString(2, entity.getModelo());
            ps.setInt(3, entity.getCapacidadPasajeros());
            ps.setString(4, entity.getEstado());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM aeronaves WHERE id = ?";
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
        String sql = "SELECT 1 FROM aeronaves WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM aeronaves";
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
    public Optional<Aeronave> findByMatricula(String matricula) throws SQLException {
        String sql = "SELECT id, matricula, modelo, capacidad_pasajeros, estado FROM aeronaves WHERE matricula = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Aeronave> findByEstado(String estado) throws SQLException {
        List<Aeronave> list = new ArrayList<>();
        String sql = "SELECT id, matricula, modelo, capacidad_pasajeros, estado FROM aeronaves WHERE estado = ?";
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

    @Override
    public List<Aeronave> findActivas() throws SQLException {
        List<Aeronave> list = new ArrayList<>();
        String sql = "SELECT id, matricula, modelo, capacidad_pasajeros, estado FROM aeronaves WHERE estado = 'ACTIVO'";
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
