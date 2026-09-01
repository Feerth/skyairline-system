package com.skyairlines.view.usuario;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.model.entity.Usuario;
import com.skyairlines.model.enums.RolUsuario;
import com.skyairlines.model.tablemodel.UsuarioTableModel;
import com.skyairlines.util.DateUtils;
import com.skyairlines.util.SwingUtils;

import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioPanel extends JPanel {

    private static final Color COLOR_GREEN = new Color(39, 174, 96);
    private static final Color COLOR_RED = new Color(231, 76, 60);
    private static final Color COLOR_BLUE = new Color(52, 152, 219);
    private static final Color COLOR_ORANGE = new Color(230, 126, 34);
    private static final Color COLOR_DARK = new Color(44, 62, 80);
    private static final Color COLOR_LIGHT_BG = new Color(245, 247, 250);
    private static final Color COLOR_WHITE = Color.WHITE;

    private JTable table;
    private UsuarioTableModel tableModel;
    private JTextField searchField;
    private JLabel statusLabel;
    private List<Usuario> fullData;

    public UsuarioPanel() {
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

        JLabel title = new JLabel("Gestion de Usuarios");
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
        tableModel = new UsuarioTableModel(new ArrayList<>());
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
        btnEliminar.addActionListener(e -> eliminarUsuario());

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
        List<Usuario> filtered = new ArrayList<>();
        for (Usuario u : fullData) {
            String email = u.getEmail() != null ? u.getEmail().toLowerCase() : "";
            String rol = u.getRol() != null ? u.getRol().getDbValue().toLowerCase() : "";
            if (email.contains(query) || rol.contains(query)) {
                filtered.add(u);
            }
        }
        tableModel.refreshData(filtered);
        statusLabel.setText("Total: " + filtered.size() + " registros");
    }

    private void loadData() {
        SwingWorker<List<Usuario>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Usuario> doInBackground() throws Exception {
                List<Usuario> list = new ArrayList<>();
                String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios ORDER BY id";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql);
                     ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Timestamp ts = rs.getTimestamp("created_at");
                        java.time.OffsetDateTime createdAt = ts != null ?
                                ts.toLocalDateTime().atOffset(java.time.OffsetDateTime.now().getOffset()) : null;
                        list.add(new Usuario(
                                rs.getInt("id"),
                                rs.getString("email"),
                                rs.getString("password_hash"),
                                RolUsuario.fromDbValue(rs.getString("rol")),
                                rs.getBoolean("activo"),
                                createdAt
                        ));
                    }
                }
                return list;
            }

            @Override
            protected void done() {
                try {
                    fullData = get();
                    tableModel.refreshData(fullData);
                    statusLabel.setText("Total: " + fullData.size() + " registros");
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Error al cargar usuarios: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private int getSelectedUsuarioId() {
        int row = table.getSelectedRow();
        if (row < 0) {
            SwingUtils.showWarningDialog(this, "Advertencia", "Seleccione un usuario de la tabla.");
            return -1;
        }
        Usuario u = tableModel.getUsuarioAt(row);
        return u != null ? u.getId() : -1;
    }

    private void showCrearDialog() {
        UsuarioDialog dialog = new UsuarioDialog((Frame) SwingUtilities.getWindowAncestor(this), "Crear Usuario", null);
        dialog.setVisible(true);
        if (dialog.isAccepted()) {
            SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    String hashedPassword = BCrypt.hashpw(dialog.getPassword(), BCrypt.gensalt());
                    Connection conn = ConexionBD.INSTANCE.getConnection();
                    try {
                        conn.setAutoCommit(false);
                        String sql = "INSERT INTO usuarios (email, password_hash, rol, activo) VALUES (?, ?, ?, ?)";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, dialog.getEmail());
                            ps.setString(2, hashedPassword);
                            ps.setString(3, dialog.getRol().getDbValue());
                            ps.setBoolean(4, dialog.isActivo());
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
                        SwingUtils.showInfoDialog(UsuarioPanel.this, "\u00C9xito", "Usuario creado exitosamente.");
                        loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                        SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Error al crear usuario: " + e.getMessage());
                    }
                }
            };
            worker.execute();
        }
    }

    private void showEditarDialog() {
        int usuarioId = getSelectedUsuarioId();
        if (usuarioId < 0) return;

        SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                String sql = "SELECT id, email, password_hash, rol, activo, created_at FROM usuarios WHERE id = ?";
                try (Connection conn = ConexionBD.INSTANCE.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, usuarioId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Timestamp ts = rs.getTimestamp("created_at");
                            java.time.OffsetDateTime createdAt = ts != null ?
                                    ts.toLocalDateTime().atOffset(java.time.OffsetDateTime.now().getOffset()) : null;
                            return new Usuario(
                                    rs.getInt("id"),
                                    rs.getString("email"),
                                    rs.getString("password_hash"),
                                    RolUsuario.fromDbValue(rs.getString("rol")),
                                    rs.getBoolean("activo"),
                                    createdAt
                            );
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get();
                    if (usuario == null) {
                        SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Usuario no encontrado.");
                        return;
                    }
                    UsuarioDialog dialog = new UsuarioDialog(
                            (Frame) SwingUtilities.getWindowAncestor(UsuarioPanel.this),
                            "Editar Usuario",
                            usuario
                    );
                    dialog.setVisible(true);
                    if (dialog.isAccepted()) {
                        updateUsuario(usuarioId, dialog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Error al cargar usuario: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void updateUsuario(int usuarioId, UsuarioDialog dialog) {
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);

                    String newPassword = dialog.getPassword();
                    if (newPassword != null && !newPassword.isEmpty()) {
                        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
                        String sql = "UPDATE usuarios SET email = ?, password_hash = ?, rol = ?, activo = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, dialog.getEmail());
                            ps.setString(2, hashedPassword);
                            ps.setString(3, dialog.getRol().getDbValue());
                            ps.setBoolean(4, dialog.isActivo());
                            ps.setInt(5, usuarioId);
                            ps.executeUpdate();
                        }
                    } else {
                        String sql = "UPDATE usuarios SET email = ?, rol = ?, activo = ? WHERE id = ?";
                        try (PreparedStatement ps = conn.prepareStatement(sql)) {
                            ps.setString(1, dialog.getEmail());
                            ps.setString(2, dialog.getRol().getDbValue());
                            ps.setBoolean(3, dialog.isActivo());
                            ps.setInt(4, usuarioId);
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
                    SwingUtils.showInfoDialog(UsuarioPanel.this, "\u00C9xito", "Usuario actualizado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Error al actualizar usuario: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private void eliminarUsuario() {
        int usuarioId = getSelectedUsuarioId();
        if (usuarioId < 0) return;

        boolean confirm = SwingUtils.showConfirmDialog(this, "\u00BFEst\u00e1 seguro de eliminar este usuario?");
        if (!confirm) return;

        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                Connection conn = ConexionBD.INSTANCE.getConnection();
                try {
                    conn.setAutoCommit(false);
                    String sql = "DELETE FROM usuarios WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setInt(1, usuarioId);
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
                    SwingUtils.showInfoDialog(UsuarioPanel.this, "\u00C9xito", "Usuario eliminado exitosamente.");
                    loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                    SwingUtils.showErrorDialog(UsuarioPanel.this, "Error", "Error al eliminar usuario: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    private class UsuarioDialog extends JDialog {

        private boolean accepted = false;
        private final JTextField emailField;
        private final JPasswordField passwordField;
        private final JComboBox<RolUsuario> rolComboBox;
        private final JCheckBox activoCheckBox;
        private final boolean isEdit;

        public UsuarioDialog(Frame owner, String title, Usuario existing) {
            super(owner, title, true);
            this.isEdit = existing != null;
            setSize(420, 340);
            setLocationRelativeTo(owner);
            setResizable(false);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            mainPanel.setBackground(COLOR_WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            emailField = new JTextField(20);
            passwordField = new JPasswordField(20);
            rolComboBox = new JComboBox<>(RolUsuario.values());
            activoCheckBox = new JCheckBox("Activo", true);

            int row = 0;

            addFormField(mainPanel, gbc, row++, "Email:", emailField);
            addFormField(mainPanel, gbc, row++, "Password:", passwordField);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            JLabel rolLabel = new JLabel("Rol:");
            rolLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            rolLabel.setForeground(COLOR_DARK);
            mainPanel.add(rolLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            mainPanel.add(rolComboBox, gbc);
            row++;

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.weightx = 0;
            JLabel activoLabel = new JLabel("Estado:");
            activoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            activoLabel.setForeground(COLOR_DARK);
            mainPanel.add(activoLabel, gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            activoCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            mainPanel.add(activoCheckBox, gbc);
            row++;

            if (isEdit && existing != null) {
                emailField.setText(existing.getEmail() != null ? existing.getEmail() : "");
                passwordField.setToolTipText("Dejar vac\u00edo para no cambiar");
                if (existing.getRol() != null) {
                    rolComboBox.setSelectedItem(existing.getRol());
                }
                activoCheckBox.setSelected(Boolean.TRUE.equals(existing.getActivo()));
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
        public String getEmail() { return emailField.getText().trim(); }
        public String getPassword() { return new String(passwordField.getPassword()); }
        public RolUsuario getRol() { return (RolUsuario) rolComboBox.getSelectedItem(); }
        public boolean isActivo() { return activoCheckBox.isSelected(); }
    }
}
