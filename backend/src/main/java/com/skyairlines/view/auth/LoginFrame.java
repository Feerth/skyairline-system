package com.skyairlines.view.auth;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.dao.impl.UsuarioDAOImpl;
import com.skyairlines.model.entity.Usuario;
import com.skyairlines.util.SessionManager;
import com.skyairlines.util.SwingUtils;
import com.skyairlines.view.main.MainFrame;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final UsuarioDAOImpl usuarioDAO = new UsuarioDAOImpl();

    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Color WHITE = Color.WHITE;
    private static final Color GRAY_TEXT = new Color(140, 140, 140);
    private static final Color TEXT_DARK = new Color(50, 50, 50);
    private static final Color BORDER_COLOR = new Color(180, 180, 180);
    private static final Color BG_COLOR = new Color(248, 249, 250);

    public LoginFrame() {
        setTitle("Sky Airlines Peru - Inicio de Sesion");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(800, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        getContentPane().setBackground(WHITE);
        setLayout(new GridBagLayout());

        SwingUtils.centerOnScreen(this);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setBackground(WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(75, 90, 75, 90)
        ));
        cardPanel.setMaximumSize(new Dimension(780, 975));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(WHITE);
        formPanel.setMaximumSize(new Dimension(630, Integer.MAX_VALUE));

        JLabel logoLabel = new JLabel("SKY AIRLINES");
        logoLabel.setFont(new Font("Dialog", Font.BOLD, 57));
        logoLabel.setForeground(DARK_BLUE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(logoLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JLabel subtitleLabel = new JLabel("Sistema de Gestion Administrativa v2.4.1");
        subtitleLabel.setFont(new Font("Dialog", Font.PLAIN, 22));
        subtitleLabel.setForeground(GRAY_TEXT);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(subtitleLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 57)));

        JSeparator separator1 = new JSeparator();
        separator1.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator1.setForeground(new Color(220, 220, 220));
        formPanel.add(separator1);

        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        JLabel emailLabel = new JLabel("Ingrese su email");
        emailLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        emailLabel.setForeground(GRAY_TEXT);
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(emailLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        emailField = new JTextField();
        emailField.setFont(new Font("Dialog", Font.PLAIN, 25));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 22, 18, 27)
        ));
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        emailField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(emailField);

        formPanel.add(Box.createRigidArea(new Dimension(0, 37)));

        JLabel passwordLabel = new JLabel("Ingrese su contrasena");
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        passwordLabel.setForeground(GRAY_TEXT);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordLabel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 25));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 22, 18, 27)
        ));
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 78));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordField);

        formPanel.add(Box.createRigidArea(new Dimension(0, 52)));

        loginButton = new JButton("Iniciar Sesion");
        loginButton.setFont(new Font("Dialog", Font.BOLD, 27));
        loginButton.setBackground(DARK_BLUE);
        loginButton.setForeground(WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(630, 81));
        loginButton.setMaximumSize(new Dimension(630, 81));
        loginButton.addActionListener(e -> performLogin());
        formPanel.add(loginButton);

        formPanel.add(Box.createRigidArea(new Dimension(0, 57)));

        JSeparator separator2 = new JSeparator();
        separator2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separator2.setForeground(new Color(220, 220, 220));
        formPanel.add(separator2);

        formPanel.add(Box.createRigidArea(new Dimension(0, 33)));

        JLabel footerLabel = new JLabel("Solo para personal autorizado de SkyAirline v2.4.1");
        footerLabel.setFont(new Font("Dialog", Font.ITALIC, 21));
        footerLabel.setForeground(GRAY_TEXT);
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(footerLabel);

        cardPanel.add(formPanel, new GridBagConstraints());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        add(cardPanel, gbc);

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });

        emailField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocusInWindow();
                }
            }
        });
    }

    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            SwingUtils.showErrorDialog(this, "Error de Validacion",
                    "Por favor, ingrese usuario y contrasena.");
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Validando...");

        SwingWorker<Usuario, Void> worker = new SwingWorker<>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                var usuarioOpt = usuarioDAO.findByEmail(email);
                if (usuarioOpt.isEmpty()) {
                    return null;
                }
                Usuario usuario = usuarioOpt.get();
                if (!BCrypt.checkpw(password, usuario.getPasswordHash())) {
                    return null;
                }
                return usuario;
            }

            @Override
            protected void done() {
                try {
                    Usuario usuario = get();

                    if (usuario == null) {
                        SwingUtils.showErrorDialog(LoginFrame.this,
                                "Error de Inicio de Sesion",
                                "Credenciales incorrectas. Intente nuevamente.");
                        passwordField.setText("");
                        passwordField.requestFocusInWindow();
                        loginButton.setEnabled(true);
                        loginButton.setText("Iniciar Sesion");
                        return;
                    }

                    if (!Boolean.TRUE.equals(usuario.getActivo())) {
                        SwingUtils.showWarningDialog(LoginFrame.this,
                                "Usuario Desactivado",
                                "Usuario desactivado. Contacte al administrador.");
                        loginButton.setEnabled(true);
                        loginButton.setText("Iniciar Sesion");
                        return;
                    }

                    SessionManager.getInstance().login(usuario);
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setVisible(true);
                    dispose();

                } catch (Exception ex) {
                    SwingUtils.showErrorDialog(LoginFrame.this,
                            "Error del Sistema",
                            "Error al conectar con la base de datos. Intente nuevamente.");
                    loginButton.setEnabled(true);
                    loginButton.setText("Iniciar Sesion");
                }
            }
        };

        worker.execute();
    }
}