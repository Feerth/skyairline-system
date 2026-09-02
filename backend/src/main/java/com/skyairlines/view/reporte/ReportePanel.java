package com.skyairlines.view.reporte;

import javax.swing.*;
import java.awt.*;

public class ReportePanel extends JPanel {

    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_BLUE = new Color(52, 152, 219);

    public ReportePanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildPlaceholderContent();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(COLOR_LIGHT_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel title = new JLabel("Reportes");
        title.setFont(new Font("Dialog", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildPlaceholderContent() {
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(COLOR_WHITE);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_LIGHT_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(50, 60, 50, 72)
        ));
        card.setPreferredSize(new Dimension(800, 430));
        card.setMaximumSize(new Dimension(800, 430));

        JLabel iconLabel = new JLabel("R");
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 72));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(iconLabel);
        card.add(Box.createVerticalStrut(20));

        JLabel constructionTitle = new JLabel("M\u00f3dulo en Construcci\u00f3n");
        constructionTitle.setFont(new Font("Dialog", Font.BOLD, 35));
        constructionTitle.setForeground(COLOR_DARK);
        constructionTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(constructionTitle);
        card.add(Box.createVerticalStrut(20));

        JLabel description = new JLabel("<html><div style='text-align:center;width:450px'>" +
                "El m\u00f3dulo de reportes est\u00e1 siendo desarrollado. " +
                "Pr\u00f3ximamente podr\u00e1s generar reportes de vuelos, pasajeros, equipaje y personal operativo." +
                "</div></html>");
        description.setFont(new Font("Dialog", Font.PLAIN, 18));
        description.setForeground(new Color(100, 100, 100));
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(description);
        card.add(Box.createVerticalStrut(30));

        JLabel versionLabel = new JLabel("VERSI\u00d3N ESTIMADA: Q1 2027");
        versionLabel.setFont(new Font("Dialog", Font.BOLD, 17));
        versionLabel.setForeground(COLOR_BLUE);
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(versionLabel);

        centerPanel.add(card);
        add(centerPanel, BorderLayout.CENTER);
    }
}
