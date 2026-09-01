package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.VueloDAO;
import com.skyairlines.model.entity.Aeronave;
import com.skyairlines.model.entity.Ruta;
import com.skyairlines.model.entity.Vuelo;

import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VueloDAOImpl implements VueloDAO {

    private Vuelo mapRow(ResultSet rs) throws SQLException {
        Timestamp tsSalida = rs.getTimestamp("fecha_salida_programada");
        Timestamp tsLlegada = rs.getTimestamp("fecha_llegada_programada");
        return new Vuelo(
                rs.getInt("id"),
                rs.getString("codigo_vuelo"),
                rs.getInt("id_ruta"),
                rs.getInt("id_aeronave"),
                tsSalida != null ? tsSalida.toLocalDateTime().atOffset(ZoneOffset.UTC) : null,
                tsLlegada != null ? tsLlegada.toLocalDateTime().atOffset(ZoneOffset.UTC) : null,
                rs.getString("estado")
        );
    }

    private Ruta mapRuta(ResultSet rs) throws SQLException {
        return new Ruta(
                rs.getInt("ruta_id"),
                rs.getString("ruta_codigo_ruta"),
                rs.getInt("ruta_id_aeropuerto_origen"),
                rs.getInt("ruta_id_aeropuerto_destino"),
                rs.getObject("ruta_duracion_estimada_min") != null ? rs.getInt("ruta_duracion_estimada_min") : null
        );
    }

    private Aeronave mapAeronave(ResultSet rs) throws SQLException {
        return new Aeronave(
                rs.getInt("aeronave_id"),
                rs.getString("aeronave_matricula"),
                rs.getString("aeronave_modelo"),
                rs.getInt("aeronave_capacidad_pasajeros"),
                rs.getString("aeronave_estado")
        );
    }

    @Override
    public Optional<Vuelo> findById(Integer id) throws SQLException {
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos WHERE id = ?";
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
    public List<Vuelo> findAll() throws SQLException {
        List<Vuelo> list = new ArrayList<>();
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos";
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
    public Vuelo save(Vuelo entity) throws SQLException {
        String sql = "INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoVuelo());
            ps.setInt(2, entity.getIdRuta());
            ps.setInt(3, entity.getIdAeronave());
            ps.setTimestamp(4, entity.getFechaSalidaProgramada() != null ? Timestamp.valueOf(entity.getFechaSalidaProgramada().toLocalDateTime()) : null);
            ps.setTimestamp(5, entity.getFechaLlegadaProgramada() != null ? Timestamp.valueOf(entity.getFechaLlegadaProgramada().toLocalDateTime()) : null);
            ps.setString(6, entity.getEstado());
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
    public Vuelo update(Vuelo entity) throws SQLException {
        String sql = "UPDATE vuelos SET codigo_vuelo = ?, id_ruta = ?, id_aeronave = ?, fecha_salida_programada = ?, fecha_llegada_programada = ?, estado = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoVuelo());
            ps.setInt(2, entity.getIdRuta());
            ps.setInt(3, entity.getIdAeronave());
            ps.setTimestamp(4, entity.getFechaSalidaProgramada() != null ? Timestamp.valueOf(entity.getFechaSalidaProgramada().toLocalDateTime()) : null);
            ps.setTimestamp(5, entity.getFechaLlegadaProgramada() != null ? Timestamp.valueOf(entity.getFechaLlegadaProgramada().toLocalDateTime()) : null);
            ps.setString(6, entity.getEstado());
            ps.setInt(7, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM vuelos WHERE id = ?";
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
        String sql = "SELECT 1 FROM vuelos WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM vuelos";
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
    public Optional<Vuelo> findByCodigoVuelo(String codigo) throws SQLException {
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos WHERE codigo_vuelo = ?";
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
    public List<Vuelo> findByEstado(String estado) throws SQLException {
        List<Vuelo> list = new ArrayList<>();
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos WHERE estado = ?";
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
    public List<Vuelo> findByFecha(OffsetDateTime fecha) throws SQLException {
        List<Vuelo> list = new ArrayList<>();
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos WHERE DATE(fecha_salida_programada) = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha.toLocalDate()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Vuelo> findByRuta(Integer idRuta) throws SQLException {
        List<Vuelo> list = new ArrayList<>();
        String sql = "SELECT id, codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado FROM vuelos WHERE id_ruta = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idRuta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Vuelo> findAllWithDetails() throws SQLException {
        List<Vuelo> list = new ArrayList<>();
        String sql = "SELECT v.id, v.codigo_vuelo, v.id_ruta, v.id_aeronave, v.fecha_salida_programada, v.fecha_llegada_programada, v.estado, " +
                "r.id AS ruta_id, r.codigo_ruta AS ruta_codigo_ruta, r.id_aeropuerto_origen AS ruta_id_aeropuerto_origen, " +
                "r.id_aeropuerto_destino AS ruta_id_aeropuerto_destino, r.duracion_estimada_min AS ruta_duracion_estimada_min, " +
                "a.id AS aeronave_id, a.matricula AS aeronave_matricula, a.modelo AS aeronave_modelo, " +
                "a.capacidad_pasajeros AS aeronave_capacidad_pasajeros, a.estado AS aeronave_estado " +
                "FROM vuelos v " +
                "JOIN rutas r ON v.id_ruta = r.id " +
                "JOIN aeronaves a ON v.id_aeronave = a.id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Vuelo vuelo = mapRow(rs);
                vuelo.setRuta(mapRuta(rs));
                vuelo.setAeronave(mapAeronave(rs));
                list.add(vuelo);
            }
        }
        return list;
    }

    @Override
    public int countByEstado(String estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vuelos WHERE estado = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public int countTodayFlights() throws SQLException {
        String sql = "SELECT COUNT(*) FROM vuelos WHERE DATE(fecha_salida_programada) = CURRENT_DATE";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
