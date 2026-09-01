package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.RutaDAO;
import com.skyairlines.model.entity.Aeropuerto;
import com.skyairlines.model.entity.Ruta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RutaDAOImpl implements RutaDAO {

    private Ruta mapRow(ResultSet rs) throws SQLException {
        return new Ruta(
                rs.getInt("id"),
                rs.getString("codigo_ruta"),
                rs.getInt("id_aeropuerto_origen"),
                rs.getInt("id_aeropuerto_destino"),
                rs.getObject("duracion_estimada_min") != null ? rs.getInt("duracion_estimada_min") : null
        );
    }

    private Aeropuerto mapAeropuerto(ResultSet rs, String prefix) throws SQLException {
        return new Aeropuerto(
                rs.getInt(prefix + "_id"),
                rs.getString(prefix + "_codigo_iata"),
                rs.getString(prefix + "_nombre"),
                rs.getString(prefix + "_ciudad"),
                rs.getString(prefix + "_pais"),
                rs.getString(prefix + "_zona_horaria")
        );
    }

    @Override
    public Optional<Ruta> findById(Integer id) throws SQLException {
        String sql = "SELECT id, codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min FROM rutas WHERE id = ?";
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
    public List<Ruta> findAll() throws SQLException {
        List<Ruta> list = new ArrayList<>();
        String sql = "SELECT id, codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min FROM rutas";
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
    public Ruta save(Ruta entity) throws SQLException {
        String sql = "INSERT INTO rutas (codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoRuta());
            ps.setInt(2, entity.getIdAeropuertoOrigen());
            ps.setInt(3, entity.getIdAeropuertoDestino());
            if (entity.getDuracionEstimadaMin() != null) {
                ps.setInt(4, entity.getDuracionEstimadaMin());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
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
    public Ruta update(Ruta entity) throws SQLException {
        String sql = "UPDATE rutas SET codigo_ruta = ?, id_aeropuerto_origen = ?, id_aeropuerto_destino = ?, duracion_estimada_min = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoRuta());
            ps.setInt(2, entity.getIdAeropuertoOrigen());
            ps.setInt(3, entity.getIdAeropuertoDestino());
            if (entity.getDuracionEstimadaMin() != null) {
                ps.setInt(4, entity.getDuracionEstimadaMin());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM rutas WHERE id = ?";
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
        String sql = "SELECT 1 FROM rutas WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM rutas";
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
    public Optional<Ruta> findByCodigoRuta(String codigo) throws SQLException {
        String sql = "SELECT id, codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min FROM rutas WHERE codigo_ruta = ?";
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
    public List<Ruta> findByAeropuertoOrigen(Integer idOrigen) throws SQLException {
        List<Ruta> list = new ArrayList<>();
        String sql = "SELECT id, codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min FROM rutas WHERE id_aeropuerto_origen = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idOrigen);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Ruta> findByAeropuertoDestino(Integer idDestino) throws SQLException {
        List<Ruta> list = new ArrayList<>();
        String sql = "SELECT id, codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min FROM rutas WHERE id_aeropuerto_destino = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idDestino);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Ruta> findAllWithAeropuertos() throws SQLException {
        List<Ruta> list = new ArrayList<>();
        String sql = "SELECT r.id, r.codigo_ruta, r.id_aeropuerto_origen, r.id_aeropuerto_destino, r.duracion_estimada_min, " +
                "ao.id AS ao_id, ao.codigo_iata AS ao_codigo_iata, ao.nombre AS ao_nombre, ao.ciudad AS ao_ciudad, ao.pais AS ao_pais, ao.zona_horaria AS ao_zona_horaria, " +
                "ad.id AS ad_id, ad.codigo_iata AS ad_codigo_iata, ad.nombre AS ad_nombre, ad.ciudad AS ad_ciudad, ad.pais AS ad_pais, ad.zona_horaria AS ad_zona_horaria " +
                "FROM rutas r " +
                "JOIN aeropuertos ao ON r.id_aeropuerto_origen = ao.id " +
                "JOIN aeropuertos ad ON r.id_aeropuerto_destino = ad.id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ruta ruta = mapRow(rs);
                ruta.setAeropuertoOrigen(mapAeropuerto(rs, "ao"));
                ruta.setAeropuertoDestino(mapAeropuerto(rs, "ad"));
                list.add(ruta);
            }
        }
        return list;
    }
}
