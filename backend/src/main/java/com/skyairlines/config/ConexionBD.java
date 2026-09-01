package com.skyairlines.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public enum ConexionBD {
    INSTANCE;

    private final String url;
    private final String user;
    private final String password;

    ConexionBD() {
        Properties props = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db/config.properties")) {
            if (is != null) {
                props.load(is);
                System.out.println("[SkyAirlines] Config loaded from config.properties");
            } else {
                System.out.println("[SkyAirlines] config.properties not found, using defaults");
            }
        } catch (Exception e) {
            System.out.println("[SkyAirlines] Error loading config.properties, using defaults");
        }

        this.url = props.getProperty("db.url", "jdbc:postgresql://localhost:5432/skyairline_db");
        this.user = props.getProperty("db.user", "postgres");
        this.password = props.getProperty("db.password", "postgres");

        String driver = props.getProperty("db.driver", "org.postgresql.Driver");
        try {
            Class.forName(driver);
            System.out.println("[SkyAirlines] PostgreSQL driver loaded");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
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