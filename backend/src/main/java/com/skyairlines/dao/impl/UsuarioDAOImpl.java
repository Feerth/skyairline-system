package com.skyairlines.dao.impl;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.api.UsuarioDAO;
import com.skyairlines.model.entity.Usuario;
import com.skyairlines.model.enums.RolUsuario;

import java.sql.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAOImpl implements UsuarioDAO {

    private Usuario mapRow(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("created_at");
        OffsetDateTime createdAt = ts != null ? ts.toLocalDateTime().atOffset(OffsetDateTime.now().getOffset()) : null;
        return new Usuario(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password_hash"),
                RolUsuario.fromDbValue(rs.getString("rol")),
                rs.getBoolean("activo"),
                createdAt
        );
    }

    @Override
    public Optional<Usuario> findById(Integer id) throws SQLException {
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE id = ?";
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
    public List<Usuario> findAll() throws SQLException {
        List<Usuario> list = new ArrayList<>();
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios";
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
    public Usuario save(Usuario entity) throws SQLException {
        String sql = "INSERT INTO usuarios (email, password_hash, rol, activo) VALUES (?, ?, ?, ?) RETURNING id";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPasswordHash());
            ps.setString(3, entity.getRol().getDbValue());
            ps.setBoolean(4, entity.getActivo());
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
    public Usuario update(Usuario entity) throws SQLException {
        String sql = "UPDATE usuarios SET email = ?, password_hash = ?, rol = ?, activo = ? WHERE id = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getEmail());
            ps.setString(2, entity.getPasswordHash());
            ps.setString(3, entity.getRol().getDbValue());
            ps.setBoolean(4, entity.getActivo());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
            ConexionBD.INSTANCE.commit(conn);
        }
        return entity;
    }

    @Override
    public boolean deleteById(Integer id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
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
        String sql = "SELECT 1 FROM usuarios WHERE id = ?";
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
        String sql = "SELECT COUNT(*) FROM usuarios";
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
    public Optional<Usuario> findByEmail(String email) throws SQLException {
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE email = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findByEmailAndPassword(String email, String passwordHash) throws SQLException {
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE email = ? AND password_hash = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passwordHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> findByRol(RolUsuario rol) throws SQLException {
        List<Usuario> list = new ArrayList<>();
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE rol = ?";
        try (Connection conn = ConexionBD.INSTANCE.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getDbValue());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Usuario> findActivos() throws SQLException {
        List<Usuario> list = new ArrayList<>();
        String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE activo = true";
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
