package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.AeropuertoDAO;
import com.skyairlines.model.entity.Aeropuerto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeropuertoDAOImpl implements AeropuertoDAO {

    private Aeropuerto mapRow(ResultSet rs) throws SQLException {
        return new Aeropuerto(
                rs.getInt("id"),
                rs.getString("codigo_iata"),
                rs.getString("nombre"),
                rs.getString("ciudad"),
                rs.getString("pais"),
                rs.getString("zona_horaria")
        );
    }

    @Override
    public Optional<Aeropuerto> findById(Integer id) throws SQLException {
        String sql = "SELECT id, codigo_iata, nombre, ciudad, pais, zona_horaria FROM aeropuertos WHERE id = ?";
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
    public List<Aeropuerto> findAll() throws SQLException {
        List<Aeropuerto> list = new ArrayList<>();
        String sql = "SELECT id, codigo_iata, nombre, ciudad, pais, zona_horaria FROM aeropuertos";
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
    public Aeropuerto save(Aeropuerto entity) throws SQLException {
        String sql = "INSERT INTO aeropuertos (codigo_iata, nombre, ciudad, pais, zona_horaria) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoIata());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getCiudad());
            ps.setString(4, entity.getPais());
            ps.setString(5, entity.getZonaHoraria());
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
    public Aeropuerto update(Aeropuerto entity) throws SQLException {
        String sql = "UPDATE aeropuertos SET codigo_iata = ?, nombre = ?, ciudad = ?, pais = ?, zona_horaria = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoIata());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getCiudad());
            ps.setString(4, entity.getPais());
            ps.setString(5, entity.getZonaHoraria());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM aeropuertos WHERE id = ?";
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
        String sql = "SELECT 1 FROM aeropuertos WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM aeropuertos";
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
    public Optional<Aeropuerto> findByCodigoIata(String codigo) throws SQLException {
        String sql = "SELECT id, codigo_iata, nombre, ciudad, pais, zona_horaria FROM aeropuertos WHERE codigo_iata = ?";
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
    public List<Aeropuerto> findByCiudad(String ciudad) throws SQLException {
        List<Aeropuerto> list = new ArrayList<>();
        String sql = "SELECT id, codigo_iata, nombre, ciudad, pais, zona_horaria FROM aeropuertos WHERE ciudad = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ciudad);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Aeropuerto> findByPais(String pais) throws SQLException {
        List<Aeropuerto> list = new ArrayList<>();
        String sql = "SELECT id, codigo_iata, nombre, ciudad, pais, zona_horaria FROM aeropuertos WHERE pais = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pais);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
