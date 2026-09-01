package com.skyairlines;

import com.formdev.flatlaf.FlatLightLaf;
import com.skyairlines.config.ConexionBD;
import com.skyairlines.view.auth.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.put("Component.focusWidth", 1);
            UIManager.put("Component.innerFocusWidth", 0);
            UIManager.put("Button.arc", 5);
            UIManager.put("Component.arc", 5);
            UIManager.put("TextComponent.arc", 5);
            FlatLightLaf.setup();
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                // silent
            }
        }

        SwingUtilities.invokeLater(() -> {
            try {
                var conn = ConexionBD.INSTANCE.getConnection();
                ConexionBD.INSTANCE.closeConnection(conn);
                System.out.println("[SkyAirlines] DB connection test successful");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "No se pudo conectar a la base de datos.\n\n"
                                + "Verifique que PostgreSQL este ejecutandose en localhost:5432\n"
                                + "Base de datos: skyairline_db\n\n"
                                + "Error: " + e.getMessage(),
                        "Error de Conexion",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }

            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            System.out.println("[SkyAirlines] Application started");
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ConexionBD.INSTANCE.shutdown();
            System.out.println("[SkyAirlines] Shutdown complete");
        }));
    }
}