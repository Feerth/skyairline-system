package com.skyairlines.view.equipaje;

import com.skyairlines.dao.impl.EquipajeDAOImpl;
import com.skyairlines.dao.impl.HistorialEquipajeDAOImpl;
import com.skyairlines.model.entity.HistorialEquipaje;
import com.skyairlines.model.enums.EstadoEquipaje;
import com.skyairlines.model.tablemodel.EquipajeTableModel;
import com.skyairlines.util.SwingUtils;
import com.skyairlines.view.main.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EquipajePanel extends JPanel {

    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_BORDER = new Color(220, 220, 220);
    private static final Color COLOR_PLACEHOLDER_BG = new Color(230, 230, 230);

    private final MainFrame mainFrame;
    private final Integer vueloId;

    private EquipajeTableModel tableModel;
    private JTable table;
    private JComboBox<String> estadoComboBox;
    private JButton btnActualizarEstado;
    private JLabel statusLabel;

    public EquipajePanel(MainFrame mainFrame, Integer vueloId) {
        this.mainFrame = mainFrame;
        this.vueloId = vueloId;
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildCenterTable();
        buildRightPanel();
        buildBottomPanel();

        cargarDatos();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(COLOR_LIGHT_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, COLOR_BORDER));

        JButton btnVolver = new JButton("\u2190 Volver a Detalles");
        btnVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVolver.setForeground(COLOR_BLUE);
        btnVolver.setBackground(COLOR_WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(true);
        btnVolver.setBorder(BorderFactory.createLineBorder(COLOR_BLUE, 1));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(180, 35));
        btnVolver.addActionListener(e -> mainFrame.showVueloDetalle(vueloId));
        topBar.add(btnVolver);

        JLabel title = new JLabel("Administraci\u00f3n de Equipaje - Vuelo #" + vueloId);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildCenterTable() {
        tableModel = new EquipajeTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        SwingUtils.configureTable(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                actualizarComboEstados();
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(COLOR_LIGHT_BG);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 1, 0, 0, COLOR_BORDER),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        rightPanel.setPreferredSize(new Dimension(260, 0));

        JLabel transitionTitle = new JLabel("Transici\u00f3n de Estado");
        transitionTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        transitionTitle.setForeground(COLOR_DARK);
        transitionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(transitionTitle);
        rightPanel.add(Box.createVerticalStrut(10));

        JLabel comboLabel = new JLabel("Nuevo estado:");
        comboLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboLabel.setForeground(new Color(100, 100, 100));
        comboLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(comboLabel);
        rightPanel.add(Box.createVerticalStrut(5));

        estadoComboBox = new JComboBox<>();
        estadoComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        estadoComboBox.setMaximumSize(new Dimension(230, 35));
        estadoComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        estadoComboBox.setEnabled(false);
        rightPanel.add(estadoComboBox);
        rightPanel.add(Box.createVerticalStrut(15));

        btnActualizarEstado = new JButton("Actualizar Estado");
        btnActualizarEstado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizarEstado.setForeground(COLOR_WHITE);
        btnActualizarEstado.setBackground(new Color(39, 174, 96));
        btnActualizarEstado.setFocusPainted(false);
        btnActualizarEstado.setBorderPainted(false);
        btnActualizarEstado.setOpaque(true);
        btnActualizarEstado.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnActualizarEstado.setMaximumSize(new Dimension(230, 40));
        btnActualizarEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnActualizarEstado.setEnabled(false);
        btnActualizarEstado.addActionListener(e -> actualizarEstado());
        rightPanel.add(btnActualizarEstado);
        rightPanel.add(Box.createVerticalStrut(25));

        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(230, 1));
        separator.setForeground(COLOR_BORDER);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(separator);
        rightPanel.add(Box.createVerticalStrut(15));

        JLabel historialLabel = new JLabel("Historial de Equipaje");
        historialLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        historialLabel.setForeground(COLOR_DARK);
        historialLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(historialLabel);
        rightPanel.add(Box.createVerticalStrut(10));

        JButton btnHistorial = new JButton("Ver Historial");
        btnHistorial.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnHistorial.setForeground(COLOR_WHITE);
        btnHistorial.setBackground(COLOR_BLUE);
        btnHistorial.setFocusPainted(false);
        btnHistorial.setBorderPainted(false);
        btnHistorial.setOpaque(true);
        btnHistorial.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnHistorial.setMaximumSize(new Dimension(230, 40));
        btnHistorial.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnHistorial.addActionListener(e -> mostrarHistorial());
        rightPanel.add(btnHistorial);

        add(rightPanel, BorderLayout.EAST);
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(COLOR_LIGHT_BG);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));
        bottomPanel.setPreferredSize(new Dimension(0, 60));

        statusLabel = new JLabel("  Total equipajes: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(new Color(100, 100, 100));
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel placeholderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        placeholderPanel.setBackground(COLOR_PLACEHOLDER_BG);
        placeholderPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel qrLabel = new JLabel("Proximamente - Escaneo QR/PDA");
        qrLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        qrLabel.setForeground(new Color(120, 120, 120));
        placeholderPanel.add(qrLabel);

        bottomPanel.add(placeholderPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void actualizarComboEstados() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            estadoComboBox.removeAllItems();
            estadoComboBox.setEnabled(false);
            btnActualizarEstado.setEnabled(false);
            return;
        }

        Object estadoActualObj = tableModel.getValueAt(selectedRow, 3);
        if (estadoActualObj == null) return;

        String estadoActualStr = estadoActualObj.toString();
        EstadoEquipaje estadoActual;
        try {
            estadoActual = EstadoEquipaje.fromDbValue(estadoActualStr);
        } catch (IllegalArgumentException ex) {
            estadoComboBox.removeAllItems();
            estadoComboBox.setEnabled(false);
            btnActualizarEstado.setEnabled(false);
            return;
        }

        estadoComboBox.removeAllItems();

        if (estadoActual != EstadoEquipaje.REGISTRADO) {
            EstadoEquipaje prev = EstadoEquipaje.getPreviousState(estadoActual);
            if (prev != estadoActual) {
                estadoComboBox.addItem(prev.getDbValue());
            }
        }

        if (estadoActual != EstadoEquipaje.ENTREGADO) {
            EstadoEquipaje next = EstadoEquipaje.getNextState(estadoActual);
            if (next != estadoActual) {
                estadoComboBox.addItem(next.getDbValue());
            }
        }

        boolean hasOptions = estadoComboBox.getItemCount() > 0;
        estadoComboBox.setEnabled(hasOptions);
        btnActualizarEstado.setEnabled(hasOptions);
    }

    private void actualizarEstado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            SwingUtils.showWarningDialog(this, "Selecci\u00f3n requerida", "Seleccione un equipaje de la tabla.");
            return;
        }

        String nuevoEstadoStr = (String) estadoComboBox.getSelectedItem();
        if (nuevoEstadoStr == null) {
            SwingUtils.showWarningDialog(this, "Selecci\u00f3n requerida", "Seleccione un nuevo estado.");
            return;
        }

        Object idObj = tableModel.getValueAt(selectedRow, 0);
        if (idObj == null) return;
        int equipajeId = ((Number) idObj).intValue();

        EstadoEquipaje nuevoEstado;
        try {
            nuevoEstado = EstadoEquipaje.fromDbValue(nuevoEstadoStr);
        } catch (IllegalArgumentException ex) {
            SwingUtils.showErrorDialog(this, "Error", "Estado no v\u00e1lido.");
            return;
        }

        if (!SwingUtils.showConfirmDialog(this,
                "\u00bfEst\u00e1 seguro de actualizar el estado del equipaje #" + equipajeId + " a " + nuevoEstado.getDbValue() + "?")) {
            return;
        }

        btnActualizarEstado.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                EquipajeDAOImpl dao = new EquipajeDAOImpl();
                return dao.actualizarEstado(equipajeId, nuevoEstado);
            }

            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        SwingUtils.showInfoDialog(EquipajePanel.this, "Estado Actualizado",
                                "Equipaje #" + equipajeId + " actualizado a: " + nuevoEstado.getDbValue());
                        cargarDatos();
                    } else {
                        SwingUtils.showErrorDialog(EquipajePanel.this, "Error",
                                "No se pudo actualizar el estado del equipaje.");
                        btnActualizarEstado.setEnabled(true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EquipajePanel.this, "Error",
                            "Error al actualizar: " + e.getMessage());
                    btnActualizarEstado.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void mostrarHistorial() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            SwingUtils.showWarningDialog(this, "Selecci\u00f3n requerida", "Seleccione un equipaje de la tabla.");
            return;
        }

        Object idObj = tableModel.getValueAt(selectedRow, 0);
        if (idObj == null) return;
        int equipajeId = ((Number) idObj).intValue();

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Historial de Equipaje #" + equipajeId, true);
        dialog.setSize(750, 450);
        dialog.setLocationRelativeTo(this);

        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setBackground(COLOR_WHITE);

        JLabel dialogTitle = new JLabel("  Historial de Equipaje #" + equipajeId);
        dialogTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dialogTitle.setForeground(COLOR_DARK);
        dialogTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialogTitle.setOpaque(true);
        dialogTitle.setBackground(COLOR_LIGHT_BG);
        dialogPanel.add(dialogTitle, BorderLayout.NORTH);

        String[] columnNames = {"FECHA", "ESTADO", "OBSERVACIONES", "EMPLEADO"};
        DefaultTableModel historialModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable historialTable = new JTable(historialModel);
        SwingUtils.configureTable(historialTable);
        historialTable.getColumnModel().getColumn(0).setPreferredWidth(160);
        historialTable.getColumnModel().getColumn(1).setPreferredWidth(140);
        historialTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        historialTable.getColumnModel().getColumn(3).setPreferredWidth(80);

        for (int i = 0; i < historialTable.getColumnCount(); i++) {
            historialTable.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(historialTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        dialogPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setBackground(COLOR_LIGHT_BG);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCerrar.setPreferredSize(new Dimension(120, 35));
        btnCerrar.addActionListener(e -> dialog.dispose());
        closePanel.add(btnCerrar);
        dialogPanel.add(closePanel, BorderLayout.SOUTH);

        dialog.setContentPane(dialogPanel);

        SwingWorker<List<HistorialEquipaje>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<HistorialEquipaje> doInBackground() throws Exception {
                HistorialEquipajeDAOImpl dao = new HistorialEquipajeDAOImpl();
                return dao.findByEquipaje(equipajeId);
            }

            @Override
            protected void done() {
                try {
                    List<HistorialEquipaje> historial = get();
                    historialModel.setRowCount(0);
                    if (historial.isEmpty()) {
                        historialModel.addRow(new Object[]{"---", "---", "No hay registros de historial.", "---"});
                    } else {
                        for (HistorialEquipaje h : historial) {
                            String fecha = h.getFechaRegistro() != null ? h.getFechaRegistro().toString() : "---";
                            String estado = h.getEstado() != null ? h.getEstado().getDbValue() : "---";
                            String observaciones = h.getObservaciones() != null ? h.getObservaciones() : "N/A";
                            String empleado = h.getIdEmpleado() != null ? String.valueOf(h.getIdEmpleado()) : "N/A";
                            historialModel.addRow(new Object[]{fecha, estado, observaciones, empleado});
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    historialModel.setRowCount(0);
                    historialModel.addRow(new Object[]{"---", "---", "Error al cargar historial: " + e.getMessage(), "---"});
                }
            }
        };
        worker.execute();

        dialog.setVisible(true);
    }

    private void cargarDatos() {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                EquipajeDAOImpl dao = new EquipajeDAOImpl();
                return dao.findByVuelo(vueloId).stream()
                        .map(e -> new Object[]{
                                e.getId(),
                                "Boleto #" + e.getIdBoleto() + " (" + e.getCodigoEtiquetaBag() + ")",
                                e.getCategoriaPeso() != null ? e.getCategoriaPeso().getDbValue() : "N/A",
                                e.getEstadoActual() != null ? e.getEstadoActual().getDbValue() : "N/A"
                        })
                        .collect(java.util.stream.Collectors.toList());
            }

            @Override
            protected void done() {
                try {
                    List<Object[]> data = get();
                    tableModel.refreshData(data);
                    statusLabel.setText("  Total equipajes: " + data.size());
                    actualizarComboEstados();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EquipajePanel.this, "Error",
                            "Error al cargar equipajes: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
}
