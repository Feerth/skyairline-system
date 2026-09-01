package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.VueloAsientoDAO;
import com.skyairlines.exception.ConcurrentModificationException;
import com.skyairlines.model.entity.InventarioAsientoDTO;
import com.skyairlines.model.entity.VueloAsiento;
import com.skyairlines.model.enums.EstadoAsientoVuelo;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VueloAsientoDAOImpl implements VueloAsientoDAO {

    private VueloAsiento mapRow(ResultSet rs) throws SQLException {
        String estadoStr = rs.getString("estado");
        return new VueloAsiento(
                rs.getInt("id"),
                rs.getInt("id_vuelo"),
                rs.getInt("id_asiento_aeronave"),
                EstadoAsientoVuelo.fromDbValue(estadoStr),
                rs.getBigDecimal("precio"),
                rs.getInt("version")
        );
    }

    @Override
    public Optional<VueloAsiento> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_vuelo, id_asiento_aeronave, estado, precio, version FROM vuelo_asientos WHERE id = ?";
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
    public List<VueloAsiento> findAll() throws SQLException {
        List<VueloAsiento> list = new ArrayList<>();
        String sql = "SELECT id, id_vuelo, id_asiento_aeronave, estado, precio, version FROM vuelo_asientos";
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
    public VueloAsiento save(VueloAsiento entity) throws SQLException {
        String sql = "INSERT INTO vuelo_asientos (id_vuelo, id_asiento_aeronave, estado, precio, version) VALUES (?, ?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdVuelo());
            ps.setInt(2, entity.getIdAsientoAeronave());
            ps.setString(3, entity.getEstado().getDbValue());
            ps.setBigDecimal(4, entity.getPrecio());
            ps.setInt(5, entity.getVersion() != null ? entity.getVersion() : 0);
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
    public VueloAsiento update(VueloAsiento entity) throws SQLException {
        String sql = "UPDATE vuelo_asientos SET id_vuelo = ?, id_asiento_aeronave = ?, estado = ?, precio = ?, version = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getIdVuelo());
            ps.setInt(2, entity.getIdAsientoAeronave());
            ps.setString(3, entity.getEstado().getDbValue());
            ps.setBigDecimal(4, entity.getPrecio());
            ps.setInt(5, entity.getVersion());
            ps.setInt(6, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM vuelo_asientos WHERE id = ?";
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
        String sql = "SELECT 1 FROM vuelo_asientos WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM vuelo_asientos";
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
    public List<VueloAsiento> findByVueloId(Integer idVuelo) throws SQLException {
        List<VueloAsiento> list = new ArrayList<>();
        String sql = "SELECT id, id_vuelo, id_asiento_aeronave, estado, precio, version FROM vuelo_asientos WHERE id_vuelo = ?";
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
    public List<VueloAsiento> findByVueloAndEstado(Integer idVuelo, EstadoAsientoVuelo estado) throws SQLException {
        List<VueloAsiento> list = new ArrayList<>();
        String sql = "SELECT id, id_vuelo, id_asiento_aeronave, estado, precio, version FROM vuelo_asientos WHERE id_vuelo = ? AND estado = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVuelo);
            ps.setString(2, estado.getDbValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Optional<VueloAsiento> findByVueloAndAsiento(Integer idVuelo, Integer idAsiento) throws SQLException {
        String sql = "SELECT id, id_vuelo, id_asiento_aeronave, estado, precio, version FROM vuelo_asientos WHERE id_vuelo = ? AND id_asiento_aeronave = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVuelo);
            ps.setInt(2, idAsiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public int countByVueloAndEstado(Integer idVuelo, EstadoAsientoVuelo estado) throws SQLException {
        String sql = "SELECT COUNT(*) FROM vuelo_asientos WHERE id_vuelo = ? AND estado = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVuelo);
            ps.setString(2, estado.getDbValue());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    @Override
    public boolean actualizarEstadoConLock(Integer id, EstadoAsientoVuelo nuevoEstado, int expectedVersion) throws SQLException {
        String sql = "UPDATE vuelo_asientos SET estado = ?, version = version + 1 WHERE id = ? AND version = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.getDbValue());
            ps.setInt(2, id);
            ps.setInt(3, expectedVersion);
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated == 0) {
                String getVersionSql = "SELECT version FROM vuelo_asientos WHERE id = ?";
                try (PreparedStatement psVersion = conn.prepareStatement(getVersionSql)) {
                    psVersion.setInt(1, id);
                    try (ResultSet rs = psVersion.executeQuery()) {
                        int actualVersion = expectedVersion;
                        if (rs.next()) {
                            actualVersion = rs.getInt("version");
                        }
                        throw new ConcurrentModificationException(
                                "VueloAsiento", id, expectedVersion, actualVersion);
                    }
                }
            }
            ConexionBD.INSTANCE.commit(conn);
            return true;
        }
    }

    @Override
    public void cancelarRandomReserva(Integer idVuelo) throws SQLException {
        String selectSql = "SELECT id FROM vuelo_asientos WHERE id_vuelo = ? AND estado = 'VENDIDO' LIMIT 1";
        String updateSql = "UPDATE vuelo_asientos SET estado = 'DISPONIBLE', version = version + 1 WHERE id = ? AND estado = 'VENDIDO'";
        Connection conn = ConexionBD.INSTANCE.getConnection();
        try {
            conn.setAutoCommit(false);
            Integer asientoId = null;
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, idVuelo);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        asientoId = rs.getInt("id");
                    }
                }
            }
            if (asientoId != null) {
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, asientoId);
                    ps.executeUpdate();
                }
            }
            conn.commit();
        } catch (SQLException e) {
            ConexionBD.INSTANCE.rollback(conn);
            throw e;
        } finally {
            conn.setAutoCommit(true);
            ConexionBD.INSTANCE.closeConnection(conn);
        }
    }

    @Override
    public List<InventarioAsientoDTO> getInventarioByVuelo(Integer idVuelo) throws SQLException {
        List<InventarioAsientoDTO> list = new ArrayList<>();
        String sql = "SELECT " +
                "COALESCE(aa.clase, 'SIN_CLASE') as categoria, " +
                "COUNT(*) as total, " +
                "COUNT(CASE WHEN va.estado = 'VENDIDO' THEN 1 END) as vendidos, " +
                "COUNT(CASE WHEN va.estado = 'DISPONIBLE' THEN 1 END) as disponibles, " +
                "COUNT(CASE WHEN va.estado = 'BLOQUEADO' THEN 1 END) as cancelados " +
                "FROM vuelo_asientos va " +
                "JOIN asientos_aeronave aa ON va.id_asiento_aeronave = aa.id " +
                "WHERE va.id_vuelo = ? " +
                "GROUP BY aa.clase";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idVuelo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String categoria = rs.getString("categoria");
                    int total = rs.getInt("total");
                    int vendidos = rs.getInt("vendidos");
                    int disponibles = rs.getInt("disponibles");
                    int cancelados = rs.getInt("cancelados");
                    double ocupacion = (total > 0) ? (vendidos * 100.0 / total) : 0;
                    InventarioAsientoDTO dto = new InventarioAsientoDTO(categoria, total, vendidos, disponibles, cancelados);
                    dto.setOcupacion(ocupacion);
                    list.add(dto);
                }
            }
        }
        return list;
    }
}
