package com.skyairlines.view.vuelo;

import com.skyairlines.dao.impl.EmpleadoDAOImpl;
import com.skyairlines.dao.impl.RutaDAOImpl;
import com.skyairlines.dao.impl.VueloDAOImpl;
import com.skyairlines.model.entity.Empleado;
import com.skyairlines.model.entity.Ruta;
import com.skyairlines.model.entity.Vuelo;
import com.skyairlines.model.tablemodel.TripulacionTableModel;
import com.skyairlines.util.DateUtils;
import com.skyairlines.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VueloDetallePanel extends JPanel {

    private final JFrame mainFrame;
    private final Integer vueloId;

    private static final Color COLOR_GREEN = new Color(39, 174, 96);
    private static final Color COLOR_ORANGE = new Color(230, 126, 34);
    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_WHITE = Color.WHITE;
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);

    private JLabel rutaLabel;
    private JLabel salidaLabel;
    private JLabel llegadaLabel;
    private JLabel matriculaLabel;
    private JLabel capacidadLabel;
    private JLabel estadoLabel;
    private JTable tripulacionTable;
    private TripulacionTableModel tripulacionModel;

    public VueloDetallePanel(JFrame mainFrame, Integer vueloId) {
        this.mainFrame = mainFrame;
        this.vueloId = vueloId;
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(COLOR_WHITE);
        add(centerPanel, BorderLayout.CENTER);

        buildMetadataSection(centerPanel);
        buildCrewSection(centerPanel);
        buildBottomButtons();

        loadData();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(COLOR_LIGHT_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JButton btnVolver = new JButton("\u2190 Volver");
        btnVolver.setFont(new Font("Dialog", Font.BOLD, 17));
        btnVolver.setForeground(COLOR_BLUE);
        btnVolver.setBackground(COLOR_WHITE);
        btnVolver.setFocusPainted(false);
        btnVolver.setBorderPainted(true);
        btnVolver.setBorder(BorderFactory.createLineBorder(COLOR_BLUE, 1));
        btnVolver.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnVolver.setPreferredSize(new Dimension(140, 42));
        btnVolver.addActionListener(e -> {
            if (mainFrame != null) {
                ((com.skyairlines.view.main.MainFrame) mainFrame).showVuelos();
            }
        });
        topBar.add(btnVolver);

        JLabel title = new JLabel("Detalle de Vuelo");
        title.setFont(new Font("Dialog", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildMetadataSection(JPanel parent) {
        JPanel metadataPanel = new JPanel(new GridBagLayout());
        metadataPanel.setBackground(COLOR_WHITE);
        metadataPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(15, 20, 15, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 25);
        gbc.anchor = GridBagConstraints.WEST;

        rutaLabel = addMetadataField(metadataPanel, gbc, 0, "Ruta:", "---");
        salidaLabel = addMetadataField(metadataPanel, gbc, 1, "Salida:", "---");
        llegadaLabel = addMetadataField(metadataPanel, gbc, 2, "Llegada Estimada:", "---");
        matriculaLabel = addMetadataField(metadataPanel, gbc, 3, "Matr\u00edcula:", "---");
        capacidadLabel = addMetadataField(metadataPanel, gbc, 4, "Capacidad Total:", "---");
        estadoLabel = addMetadataField(metadataPanel, gbc, 5, "Estado:", "---");

        parent.add(metadataPanel, BorderLayout.NORTH);
    }

    private JLabel addMetadataField(JPanel panel, GridBagConstraints gbc, int row, String labelText, String value) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Dialog", Font.BOLD, 17));
        lbl.setForeground(new Color(100, 100, 100));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        JLabel valueLbl = new JLabel(value);
        valueLbl.setFont(new Font("Dialog", Font.PLAIN, 17));
        valueLbl.setForeground(COLOR_DARK);
        panel.add(valueLbl, gbc);

        return valueLbl;
    }

    private void buildCrewSection(JPanel parent) {
        JPanel crewPanel = new JPanel(new BorderLayout());
        crewPanel.setBackground(COLOR_WHITE);
        crewPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 25));

        JLabel crewTitle = new JLabel("Tripulaci\u00f3n Asignada");
        crewTitle.setFont(new Font("Dialog", Font.BOLD, 19));
        crewTitle.setForeground(COLOR_DARK);
        crewTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        crewPanel.add(crewTitle, BorderLayout.NORTH);

        JLabel noteLabel = new JLabel("Tripulaci\u00f3n registrada en el sistema");
        noteLabel.setFont(new Font("Dialog", Font.ITALIC, 15));
        noteLabel.setForeground(new Color(130, 130, 130));

        tripulacionModel = new TripulacionTableModel(new ArrayList<>());
        tripulacionTable = new JTable(tripulacionModel);
        SwingUtils.configureTable(tripulacionTable);

        tripulacionTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        tripulacionTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        tripulacionTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        tripulacionTable.getColumnModel().getColumn(3).setPreferredWidth(100);

        for (int i = 0; i < tripulacionTable.getColumnCount(); i++) {
            tripulacionTable.getColumnModel().getColumn(i).setCellRenderer(SwingUtils.formatTableCellRenderer());
        }

        JScrollPane scrollPane = new JScrollPane(tripulacionTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(COLOR_WHITE);

        JPanel notePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        notePanel.setBackground(COLOR_WHITE);
        notePanel.add(noteLabel);

        crewPanel.add(notePanel, BorderLayout.CENTER);
        crewPanel.add(scrollPane, BorderLayout.SOUTH);

        parent.add(crewPanel, BorderLayout.CENTER);
    }

    private void buildBottomButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(COLOR_LIGHT_BG);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnEquipaje = SwingUtils.createStyledButton("[EQUIPAJE] Administracion de Equipaje", new Color(142, 68, 173), COLOR_WHITE);
        btnEquipaje.setPreferredSize(new Dimension(300, 48));
        btnEquipaje.addActionListener(e -> {
            if (mainFrame != null) {
                ((com.skyairlines.view.main.MainFrame) mainFrame).showEquipaje(vueloId);
            }
        });

        JButton btnAsientos = SwingUtils.createStyledButton("[ASIENTOS] Gestion de Asientos", COLOR_BLUE, COLOR_WHITE);
        btnAsientos.setPreferredSize(new Dimension(280, 48));
        btnAsientos.addActionListener(e -> {
            if (mainFrame != null) {
                ((com.skyairlines.view.main.MainFrame) mainFrame).showAsientos(vueloId);
            }
        });

        JButton btnPasajeros = SwingUtils.createStyledButton("[PASAJEROS] Pasajeros del Vuelo", COLOR_GREEN, COLOR_WHITE);
        btnPasajeros.setPreferredSize(new Dimension(290, 48));
        btnPasajeros.addActionListener(e -> {
            if (mainFrame != null) {
                ((com.skyairlines.view.main.MainFrame) mainFrame).showPasajeros(vueloId);
            }
        });

        buttonPanel.add(btnEquipaje);
        buttonPanel.add(btnAsientos);
        buttonPanel.add(btnPasajeros);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        SwingWorker<Vuelo, Void> worker = new SwingWorker<>() {
            private List<Empleado> empleados;

            @Override
            protected Vuelo doInBackground() throws Exception {
                VueloDAOImpl vueloDAO = new VueloDAOImpl();
                List<Vuelo> allVuelos = vueloDAO.findAllWithDetails();
                Vuelo vuelo = null;
                for (Vuelo v : allVuelos) {
                    if (v.getId().equals(vueloId)) {
                        vuelo = v;
                        break;
                    }
                }

                if (vuelo != null && vuelo.getIdRuta() != null) {
                    RutaDAOImpl rutaDAO = new RutaDAOImpl();
                    List<Ruta> rutas = rutaDAO.findAllWithAeropuertos();
                    for (Ruta r : rutas) {
                        if (r.getId().equals(vuelo.getIdRuta())) {
                            vuelo.setRuta(r);
                            break;
                        }
                    }
                }

                EmpleadoDAOImpl empleadoDAO = new EmpleadoDAOImpl();
                empleados = empleadoDAO.findAll();

                return vuelo;
            }

            @Override
            protected void done() {
                try {
                    Vuelo vuelo = get();
                    if (vuelo == null) {
                        SwingUtils.showErrorDialog(VueloDetallePanel.this, "Error", "Vuelo no encontrado.");
                        return;
                    }
                    updateMetadata(vuelo);
                    updateCrewTable();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(VueloDetallePanel.this, "Error", "Error al cargar datos: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateMetadata(Vuelo vuelo) {
        Ruta ruta = vuelo.getRuta();
        if (ruta != null) {
            String origen = "---";
            String destino = "---";
            if (ruta.getAeropuertoOrigen() != null) {
                origen = ruta.getAeropuertoOrigen().getCodigoIata();
            } else {
                origen = String.valueOf(ruta.getIdAeropuertoOrigen());
            }
            if (ruta.getAeropuertoDestino() != null) {
                destino = ruta.getAeropuertoDestino().getCodigoIata();
            } else {
                destino = String.valueOf(ruta.getIdAeropuertoDestino());
            }
            rutaLabel.setText(origen + " \u2192 " + destino + " (" + ruta.getCodigoRuta() + ")");
        } else {
            rutaLabel.setText("Ruta ID: " + vuelo.getIdRuta());
        }

        salidaLabel.setText(vuelo.getFechaSalidaProgramada() != null ?
                DateUtils.formatDateTime(vuelo.getFechaSalidaProgramada()) : "---");
        llegadaLabel.setText(vuelo.getFechaLlegadaProgramada() != null ?
                DateUtils.formatDateTime(vuelo.getFechaLlegadaProgramada()) : "---");

        if (vuelo.getAeronave() != null) {
            matriculaLabel.setText(vuelo.getAeronave().getMatricula());
            capacidadLabel.setText(String.valueOf(vuelo.getAeronave().getCapacidadPasajeros()));
        } else {
            matriculaLabel.setText("---");
            capacidadLabel.setText("---");
        }

        estadoLabel.setText(vuelo.getEstado() != null ? vuelo.getEstado() : "---");
        if (vuelo.getEstado() != null) {
            switch (vuelo.getEstado()) {
                case "PROGRAMADO":
                    estadoLabel.setForeground(new Color(52, 152, 219));
                    break;
                case "COMPLETADO":
                    estadoLabel.setForeground(COLOR_GREEN);
                    break;
                case "CANCELADO":
                    estadoLabel.setForeground(new Color(231, 76, 72));
                    break;
                case "RETRASADO":
                    estadoLabel.setForeground(COLOR_ORANGE);
                    break;
                default:
                    estadoLabel.setForeground(COLOR_DARK);
                    break;
            }
        }
    }

    private void updateCrewTable() {
        SwingWorker<List<String[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<String[]> doInBackground() throws Exception {
                EmpleadoDAOImpl dao = new EmpleadoDAOImpl();
                List<Empleado> empleados = dao.findAll();
                List<String[]> crewData = new ArrayList<>();
                for (Empleado e : empleados) {
                    String rol = e.getCargo() != null ? e.getCargo() : "N/A";
                    String nombre = (e.getNombre() != null ? e.getNombre() : "") +
                            " " + (e.getApellido() != null ? e.getApellido() : "");
                    nombre = nombre.trim();
                    String licencia = e.getCodigoEmpleado() != null ? e.getCodigoEmpleado() : "N/A";
                    crewData.add(new String[]{rol, nombre, licencia, "N/A"});
                }
                return crewData;
            }

            @Override
            protected void done() {
                try {
                    List<String[]> crewData = get();
                    tripulacionModel.refreshData(crewData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        worker.execute();
    }
}
