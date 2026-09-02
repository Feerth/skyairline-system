package com.skyairlines.view.empleado;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.model.entity.Empleado;
import com.skyairlines.model.enums.RolUsuario;
import com.skyairlines.model.tablemodel.EmpleadoTableModel;
import com.skyairlines.util.SwingUtils;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoPanel extends JPanel {

    private static final Color COLOR_GREEN = new Color(39, 174, 96);
    private static final Color COLOR_RED = new Color(231, 76, 72);
    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_ORANGE = new Color(230, 126, 34);
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_WHITE = Color.WHITE;

    private JTable table;
    private EmpleadoTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    private List<Object[]> fullData;

    public EmpleadoPanel() {
        setLayout(new BorderLayout());
        setBackground(COLOR_WHITE);

        buildTopBar();
        buildSearchBar();
        buildTable();
        buildBottomButtons();

        loadData();
    }

    private void buildTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topBar.setBackground(COLOR_LIGHT_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JLabel title = new JLabel("Gestion de Personal");
        title.setFont(new Font("Dialog", Font.BOLD, 22));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(COLOR_WHITE);
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel searchLabel = new JLabel("Buscar:");
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
            public void insertUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterTable(); }
        });
        searchPanel.add(searchField);

        add(searchPanel, BorderLayout.BEFORE_LINE_BEGINS);
    }

    private void buildTable() {
        tableModel = new EmpleadoTableModel(new ArrayList<>());
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

    private void buildBottomButtons() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(COLOR_LIGHT_BG);
        buttonPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        JButton btnCrear = SwingUtils.createStyledButton("+ Crear", COLOR_GREEN, COLOR_WHITE);
        btnCrear.setPreferredSize(new Dimension(165, 46));
        btnCrear.addActionListener(e -> showCrearDialog());

        JButton btnEditar = SwingUtils.createStyledButton("Editar", COLOR_BLUE, COLOR_WHITE);
        btnEditar.setPreferredSize(new Dimension(165, 46));
        btnEditar.addActionListener(e -> showEditarDialog());

        JButton btnEliminar = SwingUtils.createStyledButton("Eliminar", COLOR_RED, COLOR_WHITE);
        btnEliminar.setPreferredSize(new Dimension(165, 46));
        btnEliminar.addActionListener(e -> eliminarEmpleado());

        JButton btnActualizar = SwingUtils.createStyledButton("Actualizar", COLOR_ORANGE, COLOR_WHITE);
        btnActualizar.setPreferredSize(new Dimension(185, 46));
        btnActualizar.addActionListener(e -> loadData());

        statusLabel = new JLabel("Total: 0 registros");
        statusLabel.setFont(new Font("Dialog", Font.ITALIC, 15));
        statusLabel.setForeground(new Color(120, 120, 120));

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnActualizar);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(statusLabel);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void filterTable() {
        if (fullData == null) return;
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            tableModel.refreshData(fullData);
            statusLabel.setText("Total: " + fullData.size() + " registros");
            return;
        }
        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : fullData) {
            String codigo = row[1] != null ? row[1].toString().toLowerCase() : "";
            String nombre = row[2] != null ? row[2].toString().toLowerCase() : "";
            String apellido = row[3] != null ? row[3].toString().toLowerCase() : "";
            String cargo = row[4] != null ? row[4].toString().toLowerCase() : "";
            String email = row[5] != null ? row[5].toString().toLowerCase() : "";
            if (codigo.contains(query) || nombre.contains(query) || apellido.contains(query) ||
                    cargo.contains(query) || email.contains(query)) {
                filtered.add(row);
            }
        }
        tableModel.refreshData(filtered);
        statusLabel.setText("Total: " + filtered.size() + " registros");
    }

    private void loadData() {
        SwingWorker<List<Object[]>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Object[]> doInBackground() throws Exception {
                List<Object[]> lista = new ArrayList<>();
                String sql = "SELECT e.id, e.codigo_empleado, e.nombre, e.apellido, e.cargo, u.email " +
                        "FROM empleados e LEFT JOIN usuarios u ON e.id_usuario = u.id ORDER BY e.id";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Object[]{
                                rs.getInt("id"),
                                rs.getString("codigo_empleado"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("cargo"),
                                rs.getString("email")
                        });
                    }
                }
                return lista;
            }

            @Override
            protected void done() {
                try {
                    fullData = get();
                    tableModel.refreshData(fullData);
                    statusLabel.setText("Total: " + fullData.size() + " registros");
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Error al cargar empleados: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private int getSelectedEmpleadoId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione un empleado de la tabla.");
            return -1;
        }
        Object id = tableModel.getValueAt(row, 0);
        return id != null ? (Integer) id : -1;
    }

    private void showCrearDialog() {
        EmpleadoDialog dialog = new EmpleadoDialog((Frame) SwingUtilities.getWindowAncestor(this), "Crear Empleado", null, null);
        dialog.setVisible(true);
        if (dialog.isAccepted()) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    Connection conn = ConexionBD.INSTANCE.getConnection();
                    try {
                        conn.setAutoCommit(false);

                        String hashedPassword = BCrypt.hashpw(dialog.getPassword(), BCrypt.gensalt());

                        String sqlUsuario = "INSERT INTO usuarios (email, password_hash, rol, activo) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                            ps.setString(1, dialog.getEmail());
                            ps.setString(2, hashedPassword);
                            ps.setString(3, dialog.getRol().getDbValue());
                            ps.setBoolean(4, true);
                            ps.executeUpdate();
                            ResultSet keys = ps.getGeneratedKeys();
                            int usuarioId = -1;
                            if (keys.next()) {
                                usuarioId = keys.getInt(1);
                            }

                            String sqlEmpleado = "INSERT INTO empleados (id_usuario, codigo_empleado, nombre, apellido, cargo) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement psE = conn.prepareStatement(sqlEmpleado)) {
                                psE.setInt(1, usuarioId);
                                psE.setString(2, dialog.getCodigoEmpleado());
                                psE.setString(3, dialog.getNombre());
                                psE.setString(4, dialog.getApellido());
                                psE.setString(5, dialog.getCargo());
                                psE.executeUpdate();
                            }
                        }

                        conn.commit();
                        return true;
                    } catch (Exception ex) {
                        ConexionBD.INSTANCE.rollback(conn);
                        throw ex;
                    } finally {
                        conn.close();
                    }
                }

                @Override
                protected void done() {
                    try {
                        get();
                        SwingUtils.showInfoDialog(EmpleadoPanel.this, "\u00C9xito", "Empleado creado exitosamente.");
                        loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Error al crear empleado: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    private void showEditarDialog() {
        int empleadoId = getSelectedEmpleadoId();
        if (empleadoId < 0) return;

        SwingWorker<Object[], Void> worker = new SwingWorker<>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                String sql = "SELECT e.id, e.codigo_empleado, e.nombre, e.apellido, e.cargo, u.email, e.id_usuario, u.rol " +
                        "FROM empleados e LEFT JOIN usuarios u ON e.id_usuario = u.id WHERE e.id = ?";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, empleadoId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("codigo_empleado"),
                                    rs.getString("nombre"),
                                    rs.getString("apellido"),
                                    rs.getString("cargo"),
                                    rs.getString("email"),
                                    rs.getInt("id_usuario"),
                                    rs.getString("rol")
                            };
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    Object[] data = get();
                    if (data == null) {
                        SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Empleado no encontrado.");
                        return;
                    }
                    Empleado existing = new Empleado();
                    existing.setId((Integer) data[0]);
                    EmpleadoDialog dialog = new EmpleadoDialog(
                            (Frame) SwingUtilities.getWindowAncestor(EmpleadoPanel.this),
                            "Editar Empleado",
                            existing,
                            data
                    );
                    dialog.setVisible(true);
                    if (dialog.isAccepted()) {
                        updateEmpleado(empleadoId, (Integer) data[6], dialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Error al cargar empleado: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateEmpleado(int empleadoId, int usuarioId, EmpleadoDialog dialog) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);

                    String sqlUsuario = "UPDATE usuarios SET email = ?, rol = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                        ps.setString(1, dialog.getEmail());
                        ps.setString(2, dialog.getRol().getDbValue());
                        ps.setInt(3, usuarioId);
                        ps.executeUpdate();
                    }

                    String sqlEmpleado = "UPDATE empleados SET codigo_empleado = ?, nombre = ?, apellido = ?, cargo = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlEmpleado)) {
                        ps.setString(1, dialog.getCodigoEmpleado());
                        ps.setString(2, dialog.getNombre());
                        ps.setString(3, dialog.getApellido());
                        ps.setString(4, dialog.getCargo());
                        ps.setInt(5, empleadoId);
                        ps.executeUpdate();
                    }

                    conn.commit();
                    return true;
                } catch (Exception ex) {
                    ConexionBD.INSTANCE.rollback(conn);
                    throw ex;
                } finally {
                    conn.close();
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    SwingUtils.showInfoDialog(EmpleadoPanel.this, "\u00C9xito", "Empleado actualizado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Error al actualizar empleado: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void eliminarEmpleado() {
        int empleadoId = getSelectedEmpleadoId();
        if (empleadoId < 0) return;

        boolean confirm = SwingUtils.showConfirmDialog(this, "\u00BFEst\u00e1 seguro de eliminar este empleado? Esta acci\u00f3n eliminar\u00e1 tambi\u00e9n el usuario asociado.");
        if (!confirm) return;

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);

                    int usuarioId = -1;
                    String sqlGetUsuario = "SELECT id_usuario FROM empleados WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlGetUsuario)) {
                        ps.setInt(1, empleadoId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                usuarioId = rs.getInt("id_usuario");
                            }
                        }
                    }

                    String sqlDeleteEmpleado = "DELETE FROM empleados WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlDeleteEmpleado)) {
                        ps.setInt(1, empleadoId);
                        ps.executeUpdate();
                    }

                    if (usuarioId > 0) {
                        String sqlDeleteUsuario = "DELETE FROM usuarios WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sqlDeleteUsuario)) {
                            ps.setInt(1, usuarioId);
                            ps.executeUpdate();
                        }
                    }

                    conn.commit();
                    return true;
                } catch (Exception ex) {
                    ConexionBD.INSTANCE.rollback(conn);
                    throw ex;
                } finally {
                    conn.close();
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    SwingUtils.showInfoDialog(EmpleadoPanel.this, "\u00C9xito", "Empleado eliminado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(EmpleadoPanel.this, "Error", "Error al eliminar empleado: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private class EmpleadoDialog extends JDialog {

        private boolean accepted = false;
        private final JTextField codigoField;
        private final JTextField nombreField;
        private final JTextField apellidoField;
        private final JTextField cargoField;
        private final JTextField emailField;
        private final JPasswordField passwordField;
        private final JComboBox<RolUsuario> rolComboBox;
        private final boolean isEdit;

        public EmpleadoDialog(Frame owner, String title, Empleado existing, Object[] data) {
            super(owner, title, true);
            this.isEdit = existing != null;
            setSize(580, 600);
            setLocationRelativeTo(owner);
            setResizable(false);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 25));
            mainPanel.setBackground(COLOR_WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            codigoField = new JTextField(20);
            nombreField = new JTextField(20);
            apellidoField = new JTextField(20);
            cargoField = new JTextField(20);
            emailField = new JTextField(20);
            passwordField = new JPasswordField(20);
            rolComboBox = new JComboBox<>(new RolUsuario[]{RolUsuario.OPERACIONES, RolUsuario.ADMINISTRADOR});

            int row = 0;

            addFormField(mainPanel, gbc, row++, "C\u00f3digo Empleado:", codigoField);
            addFormField(mainPanel, gbc, row++, "Nombre:", nombreField);
            addFormField(mainPanel, gbc, row++, "Apellido:", apellidoField);
            addFormField(mainPanel, gbc, row++, "Cargo:", cargoField);
            addFormField(mainPanel, gbc, row++, "Email:", emailField);
            addFormField(mainPanel, gbc, row++, "Password:", passwordField);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            JLabel rolLabel = new JLabel("Rol:");
            rolLabel.setFont(new Font("Dialog", Font.BOLD, 16));
            rolLabel.setForeground(COLOR_DARK);
            mainPanel.add(rolLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            mainPanel.add(rolComboBox, gbc);
            row++;

            if (isEdit && data != null) {
                codigoField.setText(data[1] != null ? data[1].toString() : "");
                nombreField.setText(data[2] != null ? data[2].toString() : "");
                apellidoField.setText(data[3] != null ? data[3].toString() : "");
                cargoField.setText(data[4] != null ? data[4].toString() : "");
                emailField.setText(data[5] != null ? data[5].toString() : "");
                passwordField.setToolTipText("Dejar vac\u00edo para no cambiar");
                if (data[7] != null) {
                    try {
                        rolComboBox.setSelectedItem(RolUsuario.fromDbValue(data[7].toString()));
                    } catch (Exception ignored) {}
                }
            }

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            JButton btnCancelar = SwingUtils.createStyledButton("Cancelar", new Color(149, 165, 166), COLOR_WHITE);
            btnCancelar.setPreferredSize(new Dimension(140, 42));
            btnCancelar.addActionListener(e -> dispose());

            JButton btnGuardar = SwingUtils.createStyledButton(isEdit ? "Actualizar" : "Guardar", COLOR_GREEN, COLOR_WHITE);
            btnGuardar.setPreferredSize(new Dimension(140, 42));
            btnGuardar.addActionListener(e -> {
                if (validateFields()) {
                    accepted = true;
                    dispose();
                }
            });

            buttonPanel.add(btnCancelar);
            buttonPanel.add(btnGuardar);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 2;
            mainPanel.add(buttonPanel, gbc);

            setContentPane(mainPanel);
        }

        private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            JLabel lbl = new JLabel(label);
            lbl.setFont(new Font("Dialog", Font.BOLD, 16));
            lbl.setForeground(COLOR_DARK);
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(field, gbc);
        }

        private boolean validateFields() {
            if (codigoField.getText().trim().isEmpty()) {
                SwingUtils.showWarningDialog(this, "Advertencia", "El c\u00f3digo de empleado es obligatorio.");
                return false;
            }
            if (nombreField.getText().trim().isEmpty()) {
                SwingUtils.showWarningDialog(this, "Advertencia", "El nombre es obligatorio.");
                return false;
            }
            if (apellidoField.getText().trim().isEmpty()) {
                SwingUtils.showWarningDialog(this, "Advertencia", "El apellido es obligatorio.");
                return false;
            }
            if (emailField.getText().trim().isEmpty()) {
                SwingUtils.showWarningDialog(this, "Advertencia", "El email es obligatorio.");
                return false;
            }
            if (!isEdit && passwordField.getPassword().length == 0) {
                SwingUtils.showWarningDialog(this, "Advertencia", "La contrase\u00f1a es obligatoria.");
                return false;
            }
            return true;
        }

        public boolean isAccepted() { return accepted; }
        public String getCodigoEmpleado() { return codigoField.getText().trim(); }
        public String getNombre() { return nombreField.getText().trim(); }
        public String getApellido() { return apellidoField.getText().trim(); }
        public String getCargo() { return cargoField.getText().trim(); }
        public String getEmail() { return emailField.getText().trim(); }
        public String getPassword() { return new String(passwordField.getPassword()); }
        public RolUsuario getRol() { return (RolUsuario) rolComboBox.getSelectedItem(); }
    }
}
