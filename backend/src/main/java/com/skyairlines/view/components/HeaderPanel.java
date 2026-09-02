package com.skyairlines.view.components;

import com.skyairlines.util.DateUtils;
import com.skyairlines.util.SessionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HeaderPanel extends JPanel {

    private final JLabel roleLabel;
    private final JLabel dateLabel;
    private final Runnable onLogout;

    private static final Color DARK_BLUE = new Color(0, 51, 102);
    private static final Color WHITE = Color.WHITE;
    private static final Color RED = new Color(200, 30, 57);
    private static final Color RED_HOVER = new Color(170, 20, 38);

    public HeaderPanel(Runnable onLogout) {
        this.onLogout = onLogout;
        setLayout(new BorderLayout());
        setBackground(DARK_BLUE);
        setPreferredSize(new Dimension(0, 110));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 4, 27));

        roleLabel = new JLabel(SessionManager.getInstance().getRolDisplay());
        roleLabel.setForeground(WHITE);
        roleLabel.setFont(new Font("Dialog", Font.BOLD, 25));
        topPanel.add(roleLabel, BorderLayout.WEST);

        JButton logoutButton = new JButton("[Cerrar Sesion]");
        logoutButton.setBackground(RED);
        logoutButton.setForeground(WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorderPainted(false);
        logoutButton.setOpaque(true);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 24));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.setPreferredSize(new Dimension(285, 60));
        logoutButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(RED_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoutButton.setBackground(RED);
            }
        });
        logoutButton.addActionListener(e -> {
            if (onLogout != null) {
                onLogout.run();
            }
        });
        topPanel.add(logoutButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 4));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        dateLabel = new JLabel(DateUtils.getCurrentDateFormatted());
        dateLabel.setForeground(new Color(180, 200, 220));
        dateLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        bottomPanel.add(dateLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void refreshHeader() {
        roleLabel.setText(SessionManager.getInstance().getRolDisplay());
        dateLabel.setText(DateUtils.getCurrentDateFormatted());
        revalidate();
        repaint();
    }
}
