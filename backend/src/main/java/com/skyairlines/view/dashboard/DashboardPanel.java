package com.skyairlines.view.dashboard;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.model.tablemodel.AlertaTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DashboardPanel extends JPanel {

    private JLabel vuelosCountLabel;
    private JLabel personalCountLabel;
    private JLabel clientesCountLabel;
    private JLabel equipajesCountLabel;
    private JTable alertaTable;
    private AlertaTableModel alertaTableModel;

    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Color ACCENT_BLUE = new Color(41, 128, 185);
    private static final Color WHITE = Color.WHITE;
    private static final Color LIGHT_BG = new Color(240, 243, 247);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(40, 40, 40);
    private static final Color TEXT_GRAY = new Color(120, 120, 120);
    private static final Color BORDER_COLOR = new Color(220, 220, 220);
    private static final Color GREEN = new Color(39, 174, 96);
    private static final Color ORANGE = new Color(243, 156, 33);
    private static final Color PURPLE = new Color(142, 68, 173);
    private static final Color RED = new Color(231, 76, 72);

    public DashboardPanel() {
        setLayout(new BorderLayout(0, 0));
        setBackground(LIGHT_BG);
        buildUI();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                SwingUtilities.invokeLater(() -> refreshData());
            }
        });

        SwingUtilities.invokeLater(this::refreshData);
    }

    private void buildUI() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(DARK_BLUE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 38));

        JLabel titleLabel = new JLabel("Panel de Control - Resumen Operativo del D\u00eda");
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Dialog", Font.BOLD, 38));
        titlePanel.add(titleLabel, BorderLayout.WEST);

        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 38));
        centerPanel.setBackground(LIGHT_BG);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 38));

        JPanel metricsPanel = new JPanel(new GridLayout(2, 2, 20, 38));
        metricsPanel.setOpaque(false);

        JPanel card1 = createMetricCard("Vuelos Programados Hoy", "0", ACCENT_BLUE);
        vuelosCountLabel = findValueLabel(card1);

        JPanel card2 = createMetricCard("Personal Activo", "0", GREEN);
        personalCountLabel = findValueLabel(card2);

        JPanel card3 = createMetricCard("Clientes Registrados", "0", PURPLE);
        clientesCountLabel = findValueLabel(card3);

        JPanel card4 = createMetricCard("Equipajes en Proceso", "0", ORANGE);
        equipajesCountLabel = findValueLabel(card4);

        metricsPanel.add(card1);
        metricsPanel.add(card2);
        metricsPanel.add(card3);
        metricsPanel.add(card4);

        centerPanel.add(metricsPanel, BorderLayout.NORTH);

        JPanel alertPanel = new JPanel(new BorderLayout(0, 10));
        alertPanel.setOpaque(false);

        JLabel alertTitle = new JLabel("Alertas Operativas del Sistema");
        alertTitle.setFont(new Font("Dialog", Font.BOLD, 28));
        alertTitle.setForeground(TEXT_DARK);
        alertPanel.add(alertTitle, BorderLayout.NORTH);

        alertaTableModel = new AlertaTableModel(null);
        alertaTable = new JTable(alertaTableModel);
        configureAlertTable();

        JScrollPane scrollPane = new JScrollPane(alertaTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(WHITE);
        alertPanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(alertPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createMetricCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 38)
        ));

        JPanel colorBar = new JPanel();
        colorBar.setBackground(accentColor);
        colorBar.setPreferredSize(new Dimension(5, 0));
        card.add(colorBar, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 8));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Dialog", Font.BOLD, 67));
        valueLabel.setForeground(TEXT_DARK);
        valueLabel.putClientProperty("metricLabel", Boolean.TRUE);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Dialog", Font.PLAIN, 25));
        titleLabel.setForeground(TEXT_GRAY);

        contentPanel.add(valueLabel, BorderLayout.NORTH);
        contentPanel.add(titleLabel, BorderLayout.SOUTH);

        card.add(contentPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel findValueLabel(JPanel card) {
        JPanel content = (JPanel) card.getComponent(1);
        return (JLabel) content.getComponent(0);
    }

    private void configureAlertTable() {
        alertaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alertaTable.setRowHeight(35);
        alertaTable.setShowGrid(true);
        alertaTable.setGridColor(new Color(200, 200, 200));
        alertaTable.setIntercellSpacing(new Dimension(1, 1));
        alertaTable.setFont(new Font("Dialog", Font.PLAIN, 24));
        alertaTable.setSelectionBackground(ACCENT_BLUE);
        alertaTable.setSelectionForeground(WHITE);
        alertaTable.setFillsViewportHeight(true);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        cellRenderer.setVerticalAlignment(SwingConstants.CENTER);
        cellRenderer.setFont(new Font("Dialog", Font.PLAIN, 24));
        cellRenderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        for (int i = 0; i < alertaTable.getColumnCount(); i++) {
            alertaTable.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        alertaTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 24));
        alertaTable.getTableHeader().setBackground(DARK_BLUE);
        alertaTable.getTableHeader().setForeground(WHITE);
        alertaTable.getTableHeader().setPreferredSize(new Dimension(0, 63));
    }

    public void refreshData() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private int vuelosCount;
            private int personalCount;
            private int clientesCount;
            private int equipajesCount;

            @Override
            protected Void doInBackground() {
                try (Connection conn = ConexionBD.INSTANCE.getConnection()) {
                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT COUNT(*) FROM vuelos WHERE DATE(fecha_salida_programada) = CURRENT_DATE");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) vuelosCount = rs.getInt(1);
                    }

                    try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM empleados");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) personalCount = rs.getInt(1);
                    }

                    try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM clientes");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) clientesCount = rs.getInt(1);
                    }

                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT COUNT(*) FROM equipajes WHERE estado_actual != 'ENTREGADO'");
                         ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) equipajesCount = rs.getInt(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void done() {
                vuelosCountLabel.setText(String.valueOf(vuelosCount));
                personalCountLabel.setText(String.valueOf(personalCount));
                clientesCountLabel.setText(String.valueOf(clientesCount));
                equipajesCountLabel.setText(String.valueOf(equipajesCount));

                alertaTableModel.refreshData();

                revalidate();
                repaint();
            }
        };

        worker.execute();
    }
}
