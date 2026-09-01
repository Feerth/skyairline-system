package com.skyairlines.view.asiento;

import com.skyairlines.dao.impl.VueloAsientoDAOImpl;
import com.skyairlines.model.entity.InventarioAsientoDTO;
import com.skyairlines.model.tablemodel.AsientoInventarioTableModel;
import com.skyairlines.util.SwingUtils;
import com.skyairlines.view.main.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AsientoPanel extends JPanel {

    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_BORDER = new Color(220, 220, 220);
    private static final Color COLOR_RED = new Color(192, 57, 43);

    private final MainFrame mainFrame;
    private final Integer vueloId;

    private AsientoInventarioTableModel tableModel;
    private JTable table;
    private JButton btnSimularCancelacion;
    private JLabel statusLabel;

    public AsientoPanel(MainFrame mainFrame, Integer vueloId) {
        this.mainFrame = mainFrame;
        this.vueloId = vueloId;
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildCenterTable();
        buildRightPanel();
        buildBottomPanel();

        cargarInventario();
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

        JLabel title = new JLabel("Gesti\u00f3n de Asientos - Vuelo #" + vueloId);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildCenterTable() {
        tableModel = new AsientoInventarioTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        SwingUtils.configureTable(table);

        table.getColumnModel().getColumn(0).setPreferredWidth(140);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);

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

        JLabel simTitle = new JLabel("Simulaci\u00f3n");
        simTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        simTitle.setForeground(COLOR_DARK);
        simTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        rightPanel.add(simTitle);
        rightPanel.add(Box.createVerticalStrut(15));

        btnSimularCancelacion = new JButton("Simular Cancelacion");
        btnSimularCancelacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSimularCancelacion.setForeground(COLOR_WHITE);
        btnSimularCancelacion.setBackground(COLOR_RED);
        btnSimularCancelacion.setFocusPainted(false);
        btnSimularCancelacion.setBorderPainted(false);
        btnSimularCancelacion.setOpaque(true);
        btnSimularCancelacion.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnSimularCancelacion.setMaximumSize(new Dimension(230, 45));
        btnSimularCancelacion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSimularCancelacion.addActionListener(e -> simularCancelacion());
        rightPanel.add(btnSimularCancelacion);

        add(rightPanel, BorderLayout.EAST);
    }

    private void buildBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        bottomPanel.setBackground(COLOR_LIGHT_BG);
        bottomPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, COLOR_BORDER));
        bottomPanel.setPreferredSize(new Dimension(0, 50));

        statusLabel = new JLabel("Capacidad: --- | Vendidos: --- | Disponibles: ---");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(new Color(80, 80, 80));
        bottomPanel.add(statusLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void simularCancelacion() {
        btnSimularCancelacion.setEnabled(false);

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                VueloAsientoDAOImpl dao = new VueloAsientoDAOImpl();
                List<InventarioAsientoDTO> inventario = dao.getInventarioByVuelo(vueloId);
                int totalVendidos = 0;
                for (InventarioAsientoDTO dto : inventario) {
                    totalVendidos += dto.getVendidos();
                }
                if (totalVendidos == 0) {
                    return null;
                }
                dao.cancelarRandomReserva(vueloId);
                return true;
            }

            @Override
            protected void done() {
                try {
                    Boolean result = get();
                    if (result == null) {
                        SwingUtils.showWarningDialog(AsientoPanel.this, "Sin reservas",
                                "No hay reservas vendidas para cancelar en este vuelo.");
                        btnSimularCancelacion.setEnabled(true);
                    } else {
                        SwingUtils.showInfoDialog(AsientoPanel.this, "Reserva Cancelada",
                                "Reserva cancelada exitosamente. El asiento ahora est\u00e1 disponible.");
                        cargarInventario();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(AsientoPanel.this, "Error",
                            "Error al cancelar reserva: " + e.getMessage());
                    btnSimularCancelacion.setEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void cargarInventario() {
        SwingWorker<List<InventarioAsientoDTO>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<InventarioAsientoDTO> doInBackground() throws Exception {
                VueloAsientoDAOImpl dao = new VueloAsientoDAOImpl();
                return dao.getInventarioByVuelo(vueloId);
            }

            @Override
            protected void done() {
                try {
                    List<InventarioAsientoDTO> inventario = get();
                    tableModel.refreshData(inventario);

                    int totalCapacity = 0;
                    int totalVendidos = 0;
                    int totalDisponibles = 0;
                    for (InventarioAsientoDTO dto : inventario) {
                        totalCapacity += dto.getTotal();
                        totalVendidos += dto.getVendidos();
                        totalDisponibles += dto.getDisponibles();
                    }
                    statusLabel.setText(String.format("Capacidad: %d | Vendidos: %d | Disponibles: %d",
                            totalCapacity, totalVendidos, totalDisponibles));
                    btnSimularCancelacion.setEnabled(totalVendidos > 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(AsientoPanel.this, "Error",
                            "Error al cargar inventario: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
}
