package com.skyairlines.view.cliente;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.model.entity.Cliente;
import com.skyairlines.model.enums.RolUsuario;
import com.skyairlines.model.tablemodel.ClienteTableModel;
import com.skyairlines.util.SwingUtils;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientePanel extends JPanel {

    private static final Color COLOR_GREEN = new Color(39, 174, 96);
    private static final Color COLOR_RED = new Color(231, 76, 60);
    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_ORANGE = new Color(230, 126, 34);
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_WHITE = Color.WHITE;

    private JTable table;
    private ClienteTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    private List<Object[]> fullData;

    public ClientePanel() {
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

        JLabel title = new JLabel("Gestion de Clientes");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(COLOR_DARK);
        topBar.add(title);

        add(topBar, BorderLayout.NORTH);
    }

    private void buildSearchBar() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(COLOR_WHITE);
        searchPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchLabel.setForeground(new Color(100, 100, 100));
        searchPanel.add(searchLabel);

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(300, 30));
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
        tableModel = new ClienteTableModel(new ArrayList<>());
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
        btnCrear.setPreferredSize(new Dimension(130, 38));
        btnCrear.addActionListener(e -> showCrearDialog());

        JButton btnEditar = SwingUtils.createStyledButton("Editar", COLOR_BLUE, COLOR_WHITE);
        btnEditar.setPreferredSize(new Dimension(130, 38));
        btnEditar.addActionListener(e -> showEditarDialog());

        JButton btnEliminar = SwingUtils.createStyledButton("Eliminar", COLOR_RED, COLOR_WHITE);
        btnEliminar.setPreferredSize(new Dimension(130, 38));
        btnEliminar.addActionListener(e -> eliminarCliente());

        JButton btnActualizar = SwingUtils.createStyledButton("Actualizar", COLOR_ORANGE, COLOR_WHITE);
        btnActualizar.setPreferredSize(new Dimension(150, 38));
        btnActualizar.addActionListener(e -> loadData());

        statusLabel = new JLabel("Total: 0 registros");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
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
            String nombre = row[1] != null ? row[1].toString().toLowerCase() : "";
            String apellido = row[2] != null ? row[2].toString().toLowerCase() : "";
            String docId = row[3] != null ? row[3].toString().toLowerCase() : "";
            String email = row[5] != null ? row[5].toString().toLowerCase() : "";
            if (nombre.contains(query) || apellido.contains(query) || docId.contains(query) || email.contains(query)) {
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
                String sql = "SELECT c.id, c.nombre, c.apellido, c.doc_identidad, c.telefono, u.email " +
                        "FROM clientes c LEFT JOIN usuarios u ON c.id_usuario = u.id ORDER BY c.id";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Object[]{
                                rs.getInt("id"),
                                rs.getString("nombre"),
                                rs.getString("apellido"),
                                rs.getString("doc_identidad"),
                                rs.getString("telefono"),
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
                    SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Error al cargar clientes: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private int getSelectedClientId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione un cliente de la tabla.");
            return -1;
        }
        Object id = tableModel.getValueAt(row, 0);
        return id != null ? (Integer) id : -1;
    }

    private void showCrearDialog() {
        ClienteDialog dialog = new ClienteDialog((Frame) SwingUtilities.getWindowAncestor(this), "Crear Cliente", null, null);
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
                            ps.setString(3, RolUsuario.CLIENTE.getDbValue());
                            ps.setBoolean(4, true);
                            ps.executeUpdate();
                            ResultSet keys = ps.getGeneratedKeys();
                            int usuarioId = -1;
                            if (keys.next()) {
                                usuarioId = keys.getInt(1);
                            }

                            String sqlCliente = "INSERT INTO clientes (id_usuario, nombre, apellido, doc_identidad, telefono) VALUES (?, ?, ?, ?, ?)";
                            try (PreparedStatement psC = conn.prepareStatement(sqlCliente)) {
                                psC.setInt(1, usuarioId);
                                psC.setString(2, dialog.getNombre());
                                psC.setString(3, dialog.getApellido());
                                psC.setString(4, dialog.getDocIdentidad());
                                psC.setString(5, dialog.getTelefono());
                                psC.executeUpdate();
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
                        SwingUtils.showInfoDialog(ClientePanel.this, "\u00C9xito", "Cliente creado exitosamente.");
                        loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Error al crear cliente: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    private void showEditarDialog() {
        int clienteId = getSelectedClientId();
        if (clienteId < 0) return;

        SwingWorker<Object[], Void> worker = new SwingWorker<>() {
            @Override
            protected Object[] doInBackground() throws Exception {
                String sql = "SELECT c.id, c.nombre, c.apellido, c.doc_identidad, c.telefono, u.email, c.id_usuario " +
                        "FROM clientes c LEFT JOIN usuarios u ON c.id_usuario = u.id WHERE c.id = ?";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, clienteId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            return new Object[]{
                                    rs.getInt("id"),
                                    rs.getString("nombre"),
                                    rs.getString("apellido"),
                                    rs.getString("doc_identidad"),
                                    rs.getString("telefono"),
                                    rs.getString("email"),
                                    rs.getInt("id_usuario")
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
                        SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Cliente no encontrado.");
                        return;
                    }
                    Cliente existing = new Cliente();
                    existing.setId((Integer) data[0]);
                    ClienteDialog dialog = new ClienteDialog(
                            (Frame) SwingUtilities.getWindowAncestor(ClientePanel.this),
                            "Editar Cliente",
                            existing,
                            data
                    );
                    dialog.setVisible(true);
                    if (dialog.isAccepted()) {
                        updateCliente(clienteId, (Integer) data[6], dialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Error al cargar cliente: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateCliente(int clienteId, int usuarioId, ClienteDialog dialog) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);

                    String sqlUsuario = "UPDATE usuarios SET email = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                        ps.setString(1, dialog.getEmail());
                        ps.setInt(2, usuarioId);
                        ps.executeUpdate();
                    }

                    String sqlCliente = "UPDATE clientes SET nombre = ?, apellido = ?, doc_identidad = ?, telefono = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlCliente)) {
                        ps.setString(1, dialog.getNombre());
                        ps.setString(2, dialog.getApellido());
                        ps.setString(3, dialog.getDocIdentidad());
                        ps.setString(4, dialog.getTelefono());
                        ps.setInt(5, clienteId);
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
                    SwingUtils.showInfoDialog(ClientePanel.this, "\u00C9xito", "Cliente actualizado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Error al actualizar cliente: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void eliminarCliente() {
        int clienteId = getSelectedClientId();
        if (clienteId < 0) return;

        boolean confirm = SwingUtils.showConfirmDialog(this, "\u00BFEst\u00e1 seguro de eliminar este cliente? Esta acci\u00f3n eliminar\u00e1 tambi\u00e9n el usuario asociado.");
        if (!confirm) return;

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);

                    int usuarioId = -1;
                    String sqlGetUsuario = "SELECT id_usuario FROM clientes WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlGetUsuario)) {
                        ps.setInt(1, clienteId);
                        try (ResultSet rs = ps.executeQuery()) {
                            if (rs.next()) {
                                usuarioId = rs.getInt("id_usuario");
                            }
                        }
                    }

                    String sqlDeleteCliente = "DELETE FROM clientes WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlDeleteCliente)) {
                        ps.setInt(1, clienteId);
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
                    SwingUtils.showInfoDialog(ClientePanel.this, "\u00C9xito", "Cliente eliminado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(ClientePanel.this, "Error", "Error al eliminar cliente: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private class ClienteDialog extends JDialog {

        private boolean accepted = false;
        private final JTextField nombreField;
        private final JTextField apellidoField;
        private final JTextField docIdentidadField;
        private final JTextField telefonoField;
        private final JTextField emailField;
        private final JPasswordField passwordField;
        private final boolean isEdit;

        public ClienteDialog(Frame owner, String title, Cliente existing, Object[] data) {
            super(owner, title, true);
            this.isEdit = existing != null;
            setSize(450, 420);
            setLocationRelativeTo(owner);
            setResizable(false);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(COLOR_WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            nombreField = new JTextField(20);
            apellidoField = new JTextField(20);
            docIdentidadField = new JTextField(20);
            telefonoField = new JTextField(20);
            emailField = new JTextField(20);
            passwordField = new JPasswordField(20);

            int row = 0;

            addFormField(mainPanel, gbc, row++, "Nombre:", nombreField);
            addFormField(mainPanel, gbc, row++, "Apellido:", apellidoField);
            addFormField(mainPanel, gbc, row++, "Doc. Identidad:", docIdentidadField);
            addFormField(mainPanel, gbc, row++, "Tel\u00e9fono:", telefonoField);
            addFormField(mainPanel, gbc, row++, "Email:", emailField);
            addFormField(mainPanel, gbc, row++, "Password:", passwordField);

            if (isEdit && data != null) {
                nombreField.setText(data[1] != null ? data[1].toString() : "");
                apellidoField.setText(data[2] != null ? data[2].toString() : "");
                docIdentidadField.setText(data[3] != null ? data[3].toString() : "");
                telefonoField.setText(data[4] != null ? data[4].toString() : "");
                emailField.setText(data[5] != null ? data[5].toString() : "");
                passwordField.setToolTipText("Dejar vac\u00edo para no cambiar");
            }

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
            buttonPanel.setOpaque(false);

            JButton btnCancelar = SwingUtils.createStyledButton("Cancelar", new Color(149, 165, 166), COLOR_WHITE);
            btnCancelar.setPreferredSize(new Dimension(110, 35));
            btnCancelar.addActionListener(e -> dispose());

            JButton btnGuardar = SwingUtils.createStyledButton(isEdit ? "Actualizar" : "Guardar", COLOR_GREEN, COLOR_WHITE);
            btnGuardar.setPreferredSize(new Dimension(110, 35));
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
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(COLOR_DARK);
            panel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            panel.add(field, gbc);
        }

        private boolean validateFields() {
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
        public String getNombre() { return nombreField.getText().trim(); }
        public String getApellido() { return apellidoField.getText().trim(); }
        public String getDocIdentidad() { return docIdentidadField.getText().trim(); }
        public String getTelefono() { return telefonoField.getText().trim(); }
        public String getEmail() { return emailField.getText().trim(); }
        public String getPassword() { return new String(passwordField.getPassword()); }
    }
}
