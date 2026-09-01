package com.skyairlines.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum ConexionBD {
    INSTANCE;

    private static final String URL = "jdbc:postgresql://localhost:5432/skyairline_db";
    private static final String USER = "postgres";
    private static final String PASS = "123123123";

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("[SkyAirlines] PostgreSQL driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASS);
        conn.setAutoCommit(false);
        return conn;
    }

    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                // silent
            }
        }
    }

    public void rollback(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit() && !connection.isClosed()) {
                    connection.rollback();
                }
            } catch (SQLException e) {
                // silent
            }
        }
    }

    public void commit(Connection connection) {
        if (connection != null) {
            try {
                if (!connection.getAutoCommit() && !connection.isClosed()) {
                    connection.commit();
                }
            } catch (SQLException e) {
                // silent
            }
        }
    }

    public void shutdown() {
        System.out.println("[SkyAirlines] Shutdown complete");
    }

    public boolean isHealthy() {
        try (Connection c = getConnection()) {
            return c.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
}