package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.EquipajeDAO;
import com.skyairlines.model.entity.Equipaje;
import com.skyairlines.model.enums.CategoriaEquipaje;
import com.skyairlines.model.enums.EstadoEquipaje;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquipajeDAOImpl implements EquipajeDAO {

    private Equipaje mapRow(ResultSet rs) throws SQLException {
        return new Equipaje(
                rs.getInt("id"),
                rs.getInt("id_boleto"),
                rs.getString("codigo_etiqueta_bag"),
                CategoriaEquipaje.fromDbValue(rs.getString("categoria_peso")),
                rs.getBigDecimal("peso_kg"),
                EstadoEquipaje.fromDbValue(rs.getString("estado_actual")),
                rs.getString("cinta_carrusel_actual")
        );
    }

    @Override
    public Optional<Equipaje> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual FROM equipajes WHERE id = ?";
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
    public List<Equipaje> findAll() throws SQLException {
        List<Equipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual FROM equipajes";
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
    public Equipaje save(Equipaje entity) throws SQLException {
        String sql = "INSERT INTO equipajes (id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdBoleto());
            ps.setString(2, entity.getCodigoEtiquetaBag());
            ps.setString(3, entity.getCategoriaPeso().getDbValue());
            ps.setBigDecimal(4, entity.getPesoKg());
            ps.setString(5, entity.getEstadoActual().getDbValue());
            ps.setString(6, entity.getCintaCarruselActual());
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
    public Equipaje update(Equipaje entity) throws SQLException {
        String sql = "UPDATE equipajes SET id_boleto = ?, codigo_etiqueta_bag = ?, categoria_peso = ?, peso_kg = ?, estado_actual = ?, cinta_carrusel_actual = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdBoleto());
            ps.setString(2, entity.getCodigoEtiquetaBag());
            ps.setString(3, entity.getCategoriaPeso().getDbValue());
            ps.setBigDecimal(4, entity.getPesoKg());
            ps.setString(5, entity.getEstadoActual().getDbValue());
            ps.setString(6, entity.getCintaCarruselActual());
            ps.setInt(7, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM equipajes WHERE id = ?";
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
        String sql = "SELECT 1 FROM equipajes WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM equipajes";
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
    public Optional<Equipaje> findByCodigoEtiqueta(String codigo) throws SQLException {
        String sql = "SELECT id, id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual FROM equipajes WHERE codigo_etiqueta_bag = ?";
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
    public List<Equipaje> findByBoleto(Integer idBoleto) throws SQLException {
        List<Equipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual FROM equipajes WHERE id_boleto = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idBoleto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Equipaje> findByVuelo(Integer idVuelo) throws SQLException {
        List<Equipaje> list = new ArrayList<>();
        String sql = "SELECT e.id, e.id_boleto, e.codigo_etiqueta_bag, e.categoria_peso, e.peso_kg, e.estado_actual, e.cinta_carrusel_actual " +
                "FROM equipajes e " +
                "JOIN boletos b ON e.id_boleto = b.id " +
                "JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id " +
                "WHERE va.id_vuelo = ?";
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
    public boolean actualizarEstado(Integer id, EstadoEquipaje nuevoEstado) throws SQLException {
        String sql = "UPDATE equipajes SET estado_actual = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.getDbValue());
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
            return rows > 0;
        }
    }

    @Override
    public List<Equipaje> findByEstado(EstadoEquipaje estado) throws SQLException {
        List<Equipaje> list = new ArrayList<>();
        String sql = "SELECT id, id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual FROM equipajes WHERE estado_actual = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado.getDbValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
}
