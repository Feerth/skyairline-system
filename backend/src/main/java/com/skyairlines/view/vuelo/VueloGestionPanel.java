package com.skyairlines.view.vuelo;

import com.skyairlines.dao.impl.AeronaveDAOImpl;
import com.skyairlines.dao.impl.RutaDAOImpl;
import com.skyairlines.dao.impl.VueloDAOImpl;
import com.skyairlines.model.entity.Aeronave;
import com.skyairlines.model.entity.Ruta;
import com.skyairlines.model.entity.Vuelo;
import com.skyairlines.model.tablemodel.VueloTableModel;
import com.skyairlines.util.DateUtils;
import com.skyairlines.util.SwingUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class VueloGestionPanel extends JPanel {

    private final JFrame mainFrame;
    private VueloTableModel tableModel;
    private JTable table;
    private JLabel statusLabel;
    private JTextField searchCodeField;
    private JTextField searchDateField;
    private JComboBox<String> searchEstadoCombo;

    private static final Color COLOR_GREEN = new Color(39, 174, 96);
    private static final Color COLOR_ORANGE = new Color(230, 126, 34);
    private static final Color COLOR_RED = new Color(231, 76, 60);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_DARK = new Color(44, 62, 80);

    public VueloGestionPanel(JFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildCenterTable();
        buildBottomBar();

        loadVuelos();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new GridBagLayout());
        topBar.setBackground(new Color(245, 247, 250));
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblCodigo = new JLabel("C\u00f3digo:");
        lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 0;
        gbc.weightx = 0;
        topBar.add(lblCodigo, gbc);

        searchCodeField = new JTextField(10);
        searchCodeField.setToolTipText("Buscar por c\u00f3digo...");
        searchCodeField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchCodeField.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 1;
        gbc.weightx = 0;
        topBar.add(searchCodeField, gbc);

        JLabel lblFecha = new JLabel("Fecha:");
        lblFecha.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 2;
        gbc.weightx = 0;
        topBar.add(lblFecha, gbc);

        searchDateField = new JTextField(10);
        searchDateField.setToolTipText("dd/mm/aaaa");
        searchDateField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchDateField.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 3;
        gbc.weightx = 0;
        topBar.add(searchDateField, gbc);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridx = 4;
        gbc.weightx = 0;
        topBar.add(lblEstado, gbc);

        searchEstadoCombo = new JComboBox<>(new String[]{
                "TODOS", "PROGRAMADO", "EMBARCANDO", "EN_VUELO", "COMPLETADO", "CANCELADO", "RETRASADO"
        });
        searchEstadoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchEstadoCombo.setPreferredSize(new Dimension(140, 30));
        gbc.gridx = 5;
        gbc.weightx = 0;
        topBar.add(searchEstadoCombo, gbc);

        JButton btnBuscar = SwingUtils.createStyledButton("Buscar", new Color(52, 152, 219), COLOR_WHITE);
        btnBuscar.setPreferredSize(new Dimension(90, 35));
        btnBuscar.addActionListener(e -> searchVuelos());
        gbc.gridx = 6;
        gbc.weightx = 0;
        topBar.add(btnBuscar, gbc);

        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 30));
        separator.setForeground(new Color(180, 180, 180));
        gbc.gridx = 7;
        gbc.weightx = 0;
        topBar.add(separator, gbc);

        JButton btnCrear = SwingUtils.createStyledButton("+ Crear", COLOR_GREEN, COLOR_WHITE);
        btnCrear.setPreferredSize(new Dimension(110, 35));
        btnCrear.addActionListener(e -> crearVuelo());
        gbc.gridx = 8;
        gbc.weightx = 0;
        topBar.add(btnCrear, gbc);

        JButton btnEditar = SwingUtils.createStyledButton("Editar", COLOR_ORANGE, COLOR_WHITE);
        btnEditar.setPreferredSize(new Dimension(90, 35));
        btnEditar.addActionListener(e -> editarVuelo());
        gbc.gridx = 9;
        gbc.weightx = 0;
        topBar.add(btnEditar, gbc);

        JButton btnEliminar = SwingUtils.createStyledButton("Eliminar", COLOR_RED, COLOR_WHITE);
        btnEliminar.setPreferredSize(new Dimension(100, 35));
        btnEliminar.addActionListener(e -> eliminarVuelo());
        gbc.gridx = 10;
        gbc.weightx = 0;
        topBar.add(btnEliminar, gbc);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildCenterTable() {
        tableModel = new VueloTableModel(new ArrayList<>());
        table = new JTable(tableModel);
        SwingUtils.configureTable(table);
        table.setRowSorter(new TableRowSorter<>(tableModel));

        table.getColumnModel().getColumn(5).setCellRenderer(new VueloButtonRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(130);
        table.getColumnModel().getColumn(5).setMinWidth(130);
        table.getColumnModel().getColumn(5).setMaxWidth(130);

        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        for (int i = 0; i < 5; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int col = table.columnAtPoint(e.getPoint());
                if (col == 5) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        Vuelo vuelo = tableModel.getVueloAt(table.convertRowIndexToModel(row));
                        if (vuelo != null && mainFrame != null) {
                            ((com.skyairlines.view.main.MainFrame) mainFrame).showVueloDetalle(vuelo.getId());
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void buildBottomBar() {
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setBackground(new Color(245, 247, 250));
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        statusLabel = new JLabel("Total: 0 vuelos encontrados");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        statusLabel.setForeground(new Color(100, 100, 100));
        bottomBar.add(statusLabel);

        add(bottomBar, BorderLayout.SOUTH);
    }

    private void loadVuelos() {
        SwingWorker<List<Vuelo>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Vuelo> doInBackground() throws Exception {
                VueloDAOImpl dao = new VueloDAOImpl();
                return dao.findAllWithDetails();
            }

            @Override
            protected void done() {
                try {
                    List<Vuelo> vuelos = get();
                    tableModel.refreshData(vuelos);
                    statusLabel.setText("Total: " + vuelos.size() + " vuelos encontrados");
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(VueloGestionPanel.this, "Error", "Error al cargar los vuelos: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void searchVuelos() {
        String codigo = searchCodeField.getText().trim();
        String fechaStr = searchDateField.getText().trim();
        String estado = (String) searchEstadoCombo.getSelectedItem();

        SwingWorker<List<Vuelo>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Vuelo> doInBackground() throws Exception {
                VueloDAOImpl dao = new VueloDAOImpl();
                List<Vuelo> allVuelos = dao.findAllWithDetails();

                List<Vuelo> filtered = new ArrayList<>();
                for (Vuelo v : allVuelos) {
                    boolean matchesCode = codigo.isEmpty() || (v.getCodigoVuelo() != null &&
                            v.getCodigoVuelo().toLowerCase().contains(codigo.toLowerCase()));

                    boolean matchesDate = true;
                    if (!fechaStr.isEmpty()) {
                        try {
                            String[] parts = fechaStr.split("/");
                            if (parts.length == 3) {
                                int day = Integer.parseInt(parts[0]);
                                int month = Integer.parseInt(parts[1]);
                                int year = Integer.parseInt(parts[2]);
                                java.time.LocalDate filterDate = java.time.LocalDate.of(year, month, day);
                                java.time.LocalDate vueloDate = v.getFechaSalidaProgramada() != null ?
                                        v.getFechaSalidaProgramada().atZoneSameInstant(ZoneId.systemDefault()).toLocalDate() : null;
                                matchesDate = filterDate.equals(vueloDate);
                            }
                        } catch (Exception ex) {
                            matchesDate = true;
                        }
                    }

                    boolean matchesEstado = "TODOS".equals(estado) ||
                            (estado != null && estado.equals(v.getEstado()));

                    if (matchesCode && matchesDate && matchesEstado) {
                        filtered.add(v);
                    }
                }
                return filtered;
            }

            @Override
            protected void done() {
                try {
                    List<Vuelo> result = get();
                    tableModel.refreshData(result);
                    statusLabel.setText("Total: " + result.size() + " vuelos encontrados");
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(VueloGestionPanel.this, "Error", "Error al buscar vuelos: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void crearVuelo() {
        VueloDialog dialog = new VueloDialog(mainFrame, "Crear Vuelo", null);
        dialog.setVisible(true);
        if (dialog.isApproved()) {
            loadVuelos();
        }
    }

    private void editarVuelo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione un vuelo para editar.");
            return;
        }
        Vuelo vuelo = tableModel.getVueloAt(table.convertRowIndexToModel(selectedRow));
        if (vuelo == null) {
            return;
        }
        VueloDialog dialog = new VueloDialog(mainFrame, "Editar Vuelo", vuelo);
        dialog.setVisible(true);
        if (dialog.isApproved()) {
            loadVuelos();
        }
    }

    private void eliminarVuelo() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione un vuelo para eliminar.");
            return;
        }
        Vuelo vuelo = tableModel.getVueloAt(table.convertRowIndexToModel(selectedRow));
        if (vuelo == null) {
            return;
        }

        boolean confirmed = SwingUtils.showConfirmDialog(this,
                "\u00bfEst\u00e1 seguro de eliminar el vuelo " + vuelo.getCodigoVuelo() + "?");
        if (confirmed) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    VueloDAOImpl dao = new VueloDAOImpl();
                    return dao.deleteById(vuelo.getId());
                }

                @Override
                protected void done() {
                    try {
                        Boolean success = get();
                        if (success) {
                            SwingUtils.showInfoDialog(VueloGestionPanel.this, "Éxito", "Vuelo eliminado correctamente.");
                            loadVuelos();
                        } else {
                            SwingUtils.showErrorDialog(VueloGestionPanel.this, "Error", "No se pudo eliminar el vuelo.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(VueloGestionPanel.this, "Error", "Error al eliminar: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    private class VueloButtonRenderer extends DefaultTableCellRenderer {
        private final JButton btn = new JButton("Ver Detalles");

        public VueloButtonRenderer() {
            btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            btn.setForeground(new Color(0, 102, 204));
            btn.setBackground(new Color(240, 245, 255));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setOpaque(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(0, 102, 204), 1),
                    BorderFactory.createEmptyBorder(4, 12, 4, 12)
            ));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            btn.setText("Ver Detalles");
            btn.setBackground(isSelected ? new Color(220, 235, 255) : new Color(240, 245, 255));
            return btn;
        }
    }

    private class VueloDialog extends JDialog {
        private boolean approved = false;
        private JComboBox<Ruta> rutaCombo;
        private JComboBox<Aeronave> aeronaveCombo;
        private JTextField codigoField;
        private JTextField fechaSalidaField;
        private JTextField fechaLlegadaField;
        private JComboBox<String> estadoCombo;

        public VueloDialog(JFrame owner, String title, Vuelo existingVuelo) {
            super(owner, title, true);
            setSize(450, 400);
            setLocationRelativeTo(owner);
            setResizable(false);

            JPanel contentPanel = new JPanel(new GridBagLayout());
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(COLOR_WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            int row = 0;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblCodigo = new JLabel("C\u00f3digo Vuelo:");
            lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblCodigo, gbc);
            codigoField = new JTextField(15);
            codigoField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            if (existingVuelo != null) codigoField.setText(existingVuelo.getCodigoVuelo());
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(codigoField, gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblRuta = new JLabel("Ruta:");
            lblRuta.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblRuta, gbc);
            rutaCombo = new JComboBox<>();
            rutaCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(rutaCombo, gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblAeronave = new JLabel("Aeronave:");
            lblAeronave.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblAeronave, gbc);
            aeronaveCombo = new JComboBox<>();
            aeronaveCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(aeronaveCombo, gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblFechaSalida = new JLabel("Fecha Salida:");
            lblFechaSalida.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblFechaSalida, gbc);
            fechaSalidaField = new JTextField(15);
            fechaSalidaField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fechaSalidaField.setToolTipText("dd/mm/aaaa HH:mm");
            if (existingVuelo != null && existingVuelo.getFechaSalidaProgramada() != null) {
                fechaSalidaField.setText(DateUtils.formatDateTime(existingVuelo.getFechaSalidaProgramada()));
            }
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(fechaSalidaField, gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblFechaLlegada = new JLabel("Fecha Llegada:");
            lblFechaLlegada.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblFechaLlegada, gbc);
            fechaLlegadaField = new JTextField(15);
            fechaLlegadaField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fechaLlegadaField.setToolTipText("dd/mm/aaaa HH:mm");
            if (existingVuelo != null && existingVuelo.getFechaLlegadaProgramada() != null) {
                fechaLlegadaField.setText(DateUtils.formatDateTime(existingVuelo.getFechaLlegadaProgramada()));
            }
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(fechaLlegadaField, gbc);
            row++;

            gbc.gridx = 0; gbc.gridy = row;
            JLabel lblEstado = new JLabel("Estado:");
            lblEstado.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contentPanel.add(lblEstado, gbc);
            estadoCombo = new JComboBox<>(new String[]{
                    "PROGRAMADO", "EMBARCANDO", "EN_VUELO", "COMPLETADO", "CANCELADO", "RETRASADO"
            });
            estadoCombo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            if (existingVuelo != null) {
                estadoCombo.setSelectedItem(existingVuelo.getEstado());
            }
            gbc.gridx = 1; gbc.gridy = row;
            contentPanel.add(estadoCombo, gbc);
            row++;

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(COLOR_WHITE);
            JButton btnCancelar = SwingUtils.createStyledButton("Cancelar", new Color(149, 165, 166), COLOR_WHITE);
            btnCancelar.setPreferredSize(new Dimension(100, 35));
            btnCancelar.addActionListener(e -> dispose());
            JButton btnGuardar = SwingUtils.createStyledButton("Guardar", COLOR_GREEN, COLOR_WHITE);
            btnGuardar.setPreferredSize(new Dimension(100, 35));
            btnGuardar.addActionListener(e -> guardarVuelo(existingVuelo));
            buttonPanel.add(btnCancelar);
            buttonPanel.add(btnGuardar);

            gbc.gridx = 0; gbc.gridy = row;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.EAST;
            contentPanel.add(buttonPanel, gbc);

            setContentPane(contentPanel);

            loadRutasAeronaves(existingVuelo);
        }

        private void loadRutasAeronaves(Vuelo existingVuelo) {
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                private List<Ruta> rutas;
                private List<Aeronave> aeronaves;

                @Override
                protected Void doInBackground() throws Exception {
                    RutaDAOImpl rutaDAO = new RutaDAOImpl();
                    AeronaveDAOImpl aeronaveDAO = new AeronaveDAOImpl();
                    rutas = rutaDAO.findAll();
                    aeronaves = aeronaveDAO.findAll();
                    return null;
                }

                @Override
                protected void done() {
                    try {
                        get();
                        rutaCombo.removeAllItems();
                        for (Ruta r : rutas) {
                            rutaCombo.addItem(r);
                        }
                        aeronaveCombo.removeAllItems();
                        for (Aeronave a : aeronaves) {
                            aeronaveCombo.addItem(a);
                        }
                        if (existingVuelo != null) {
                            for (int i = 0; i < rutaCombo.getItemCount(); i++) {
                                if (rutaCombo.getItemAt(i).getId().equals(existingVuelo.getIdRuta())) {
                                    rutaCombo.setSelectedIndex(i);
                                    break;
                                }
                            }
                            for (int i = 0; i < aeronaveCombo.getItemCount(); i++) {
                                if (aeronaveCombo.getItemAt(i).getId().equals(existingVuelo.getIdAeronave())) {
                                    aeronaveCombo.setSelectedIndex(i);
                                    break;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(VueloDialog.this, "Error", "Error al cargar datos: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }

        private void guardarVuelo(Vuelo existingVuelo) {
            String codigo = codigoField.getText().trim();
            if (codigo.isEmpty()) {
                SwingUtils.showWarningDialog(this, "Advertencia", "El c\u00f3digo de vuelo es obligatorio.");
                return;
            }

            Ruta rutaSeleccionada = (Ruta) rutaCombo.getSelectedItem();
            Aeronave aeronaveSeleccionada = (Aeronave) aeronaveCombo.getSelectedItem();

            if (rutaSeleccionada == null || aeronaveSeleccionada == null) {
                SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione una ruta y una aeronave.");
                return;
            }

            OffsetDateTime fechaSalida = null;
            OffsetDateTime fechaLlegada = null;
            try {
                String fsText = fechaSalidaField.getText().trim();
                if (!fsText.isEmpty()) {
                    String[] parts = fsText.split(" ");
                    String[] dateParts = parts[0].split("/");
                    String[] timeParts = parts.length > 1 ? parts[1].split(":") : new String[]{"00", "00"};
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);
                    int hour = Integer.parseInt(timeParts[0]);
                    int min = Integer.parseInt(timeParts[1]);
                    fechaSalida = java.time.LocalDateTime.of(year, month, day, hour, min)
                            .atOffset(ZoneId.systemDefault().getRules().getOffset(java.time.Instant.now()));
                }
            } catch (Exception e) {
                SwingUtils.showWarningDialog(this, "Advertencia", "Fecha de salida inv\u00e1lida. Use dd/mm/aaaa HH:mm");
                return;
            }

            try {
                String flText = fechaLlegadaField.getText().trim();
                if (!flText.isEmpty()) {
                    String[] parts = flText.split(" ");
                    String[] dateParts = parts[0].split("/");
                    String[] timeParts = parts.length > 1 ? parts[1].split(":") : new String[]{"00", "00"};
                    int day = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int year = Integer.parseInt(dateParts[2]);
                    int hour = Integer.parseInt(timeParts[0]);
                    int min = Integer.parseInt(timeParts[1]);
                    fechaLlegada = java.time.LocalDateTime.of(year, month, day, hour, min)
                            .atOffset(ZoneId.systemDefault().getRules().getOffset(java.time.Instant.now()));
                }
            } catch (Exception e) {
                SwingUtils.showWarningDialog(this, "Advertencia", "Fecha de llegada inv\u00e1lida. Use dd/mm/aaaa HH:mm");
                return;
            }

            final OffsetDateTime finalFechaSalida = fechaSalida;
            final OffsetDateTime finalFechaLlegada = fechaLlegada;

            SwingWorker<Vuelo, Void> worker = new SwingWorker<>() {
                @Override
                protected Vuelo doInBackground() throws Exception {
                    VueloDAOImpl dao = new VueloDAOImpl();
                    String estado = (String) estadoCombo.getSelectedItem();
                    Vuelo vuelo;
                    if (existingVuelo != null) {
                        vuelo = existingVuelo;
                        vuelo.setCodigoVuelo(codigo);
                        vuelo.setIdRuta(rutaSeleccionada.getId());
                        vuelo.setIdAeronave(aeronaveSeleccionada.getId());
                        vuelo.setFechaSalidaProgramada(finalFechaSalida);
                        vuelo.setFechaLlegadaProgramada(finalFechaLlegada);
                        vuelo.setEstado(estado);
                        return dao.update(vuelo);
                    } else {
                        vuelo = new Vuelo();
                        vuelo.setCodigoVuelo(codigo);
                        vuelo.setIdRuta(rutaSeleccionada.getId());
                        vuelo.setIdAeronave(aeronaveSeleccionada.getId());
                        vuelo.setFechaSalidaProgramada(finalFechaSalida);
                        vuelo.setFechaLlegadaProgramada(finalFechaLlegada);
                        vuelo.setEstado(estado);
                        return dao.save(vuelo);
                    }
                }

                @Override
                protected void done() {
                    try {
                        Vuelo result = get();
                        if (result != null) {
                            approved = true;
                            SwingUtils.showInfoDialog(VueloDialog.this, "Éxito",
                                    existingVuelo != null ? "Vuelo actualizado correctamente." : "Vuelo creado correctamente.");
                            dispose();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(VueloDialog.this, "Error", "Error al guardar: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }

        public boolean isApproved() {
            return approved;
        }
    }
}
