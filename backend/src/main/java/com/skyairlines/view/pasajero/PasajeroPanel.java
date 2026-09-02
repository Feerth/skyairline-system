package com.skyairlines.view.pasajero;

import com.skyairlines.model.tablemodel.PasajeroTableModel;
import com.skyairlines.util.SwingUtils;
import com.skyairlines.view.main.MainFrame;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PasajeroPanel extends JPanel {

    private final MainFrame mainFrame;
    private final Integer vueloId;

    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_WHITE = Color.WHITE;

    private JTable table;
    private PasajeroTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    private List<Object[]> fullData;

    public PasajeroPanel(MainFrame mainFrame, Integer vueloId) {
        this.mainFrame = mainFrame;
        this.vueloId = vueloId;
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildSearchBar();
        buildTable();
        buildStatusBar();

        loadData();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(COLOR_LIGHT_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);

        JButton btnVolver = new JButton("\u2190 Volver a Detalles");
        btnVolver.setFont(new Font("Dialog", Font.BOLD, 17));
        btnVolver.setForeground(COLOR_BLUE);
        btnVolver.setBackground(COLOR_WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(true);
        btnVolver.setBorder(BorderFactory.createLineBorder(COLOR_BLUE, 1));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(220, 42));
        btnVolver.addActionListener(e -> {
            if (mainFrame != null) {
                mainFrame.showVueloDetalle(vueloId);
            }
        });
        leftPanel.add(btnVolver);

        JLabel title = new JLabel("Pasajeros del Vuelo #" + vueloId);
        title.setFont(new Font("Dialog", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        leftPanel.add(title);

        topBar.add(leftPanel, BorderLayout.WEST);
        add(topBar, BorderLayout.NORTH);
    }

    private void buildSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(COLOR_WHITE);
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel searchLabel = new JLabel("Filtrar por nombre:");
        searchLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        searchLabel.setForeground(new Color(100, 100, 100));
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Dialog", Font.PLAIN, 17));
        searchField.setPreferredSize(new Dimension(300, 38));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
        searchPanel.add(searchField);

        add(searchPanel, BorderLayout.BEFORE_LINE_BEGINS);
    }

    private void buildTable() {
        tableModel = new PasajeroTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        SwingUtils.configureTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        statusBar.setBackground(COLOR_LIGHT_BG);
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        statusLabel = new JLabel("Total pasajeros: 0");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        statusLabel.setForeground(COLOR_DARK);
        statusBar.add(statusLabel);

        add(statusBar, BorderLayout.SOUTH);
    }

    private void filterTable() {
        if (fullData == null) return;
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            tableModel.refreshData(fullData);
            statusLabel.setText("Total pasajeros: " + fullData.size());
            return;
        }
        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : fullData) {
            String nombre = row[1] != null ? row[1].toString().toLowerCase() : "";
            if (nombre.contains(query)) {
                filtered.add(row);
            }
        }
        tableModel.refreshData(filtered);
        statusLabel.setText("Total pasajeros: " + filtered.size());
    }

    private void loadData() {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                List<Object[]> lista = new ArrayList<>();
                String sql = "SELECT ROW_NUMBER() OVER (ORDER BY p.id) AS numero, " +
                        "p.nombre || ' ' || p.apellido AS nombre_completo, " +
                        "aa.codigo_asiento, aa.clase " +
                        "FROM boletos b " +
                        "JOIN pasajeros p ON b.id_pasajero = p.id " +
                        "JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id " +
                        "JOIN asientos_aeronave aa ON va.id_asiento_aeronave = aa.id " +
                        "WHERE va.id_vuelo = ? AND b.estado = 'EMITIDO' " +
                        "ORDER BY aa.codigo_asiento";
                try (java.sql.Connection conn = com.skyairlines.config.ConexionBD.INSTANCE.getConnection();
                     java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, vueloId);
                    try (java.sql.ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            lista.add(new Object[]{
                                    rs.getInt("numero"),
                                    rs.getString("nombre_completo"),
                                    rs.getString("codigo_asiento"),
                                    rs.getString("clase")
                            });
                        }
                    }
                }
                return lista;
            }

            @Override
            protected void done() {
                try {
                    fullData = get();
                    tableModel.refreshData(fullData);
                    statusLabel.setText("Total pasajeros: " + fullData.size());
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(PasajeroPanel.this, "Error", "Error al cargar pasajeros: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }
}
