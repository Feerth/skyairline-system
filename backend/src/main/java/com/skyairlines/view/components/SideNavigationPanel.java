package com.skyairlines.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashMap;
import java.util.Map;

public class SideNavigationPanel extends JPanel {

    public interface NavigationListener {
        void onNavigate(String panel);
    }

    private static final Color NAVY = new Color(0, 26, 51);
    private static final Color NAVY_HOVER = new Color(0, 51, 102);
    private static final Color ACCENT = new Color(0, 102, 204);
    private static final Color WHITE = Color.WHITE;
    private static final Color TEXT_SECONDARY = new Color(150, 170, 190);
    private static final int BUTTON_WIDTH = 390;
    private static final int BUTTON_HEIGHT = 90;

    private final NavigationListener listener;
    private final Map<String, JButton> buttonMap = new LinkedHashMap<>();
    private JButton activeButton;

    public SideNavigationPanel(NavigationListener listener) {
        this.listener = listener;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(NAVY);
        setPreferredSize(new Dimension(BUTTON_WIDTH, 0));
        setMinimumSize(new Dimension(BUTTON_WIDTH, 0));
        setMaximumSize(new Dimension(BUTTON_WIDTH, Integer.MAX_VALUE));

        buildUI();
    }

    private void buildUI() {
        JPanel logoPanel = new JPanel(new GridBagLayout());
        logoPanel.setBackground(NAVY);
        logoPanel.setPreferredSize(new Dimension(BUTTON_WIDTH, 128));
        logoPanel.setMaximumSize(new Dimension(BUTTON_WIDTH, 128));

        JLabel logoLabel = new JLabel("SKY AIRLINES");
        logoLabel.setForeground(WHITE);
        logoLabel.setFont(new Font("Dialog", Font.BOLD, 33));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoPanel.add(logoLabel);

        add(logoPanel);

        add(Box.createRigidArea(new Dimension(0, 10)));

        String[][] buttons = {
            {"DASHBOARD", "Dashboard"},
            {"VUELOS", "Vuelos"},
            {"CLIENTES", "Clientes"},
            {"PERSONAL", "Personal"},
            {"USUARIOS", "Usuarios"},
            {"REPORTES", "Reportes"}
        };

        for (String[] buttonDef : buttons) {
            JButton button = createNavButton(buttonDef[1]);
            final String panel = buttonDef[0];
            button.addActionListener(e -> {
                if (listener != null) {
                    listener.onNavigate(panel);
                }
            });
            buttonMap.put(panel, button);
            add(button);
            add(Box.createRigidArea(new Dimension(0, 2)));
        }

        add(Box.createVerticalGlue());

        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(NAVY);
        footerPanel.setPreferredSize(new Dimension(BUTTON_WIDTH, 75));
        footerPanel.setMaximumSize(new Dimension(BUTTON_WIDTH, 75));
        JLabel footerLabel = new JLabel("v2.4.1");
        footerLabel.setForeground(TEXT_SECONDARY);
        footerLabel.setFont(new Font("Dialog", Font.PLAIN, 21));
        footerPanel.add(footerLabel);
        add(footerPanel);
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button.setBackground(NAVY);
        button.setForeground(WHITE);
        button.setFont(new Font("Dialog", Font.PLAIN, 27));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(NAVY_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (button != activeButton) {
                    button.setBackground(NAVY);
                }
            }
        });

        return button;
    }

    public void setActiveButton(String panel) {
        if (activeButton != null) {
            activeButton.setBackground(NAVY);
            activeButton.setFont(new Font("Dialog", Font.PLAIN, 27));
        }

        JButton newActive = buttonMap.get(panel);
        if (newActive != null) {
            newActive.setBackground(ACCENT);
            newActive.setFont(new Font("Dialog", Font.BOLD, 27));
            activeButton = newActive;
        }
    }
}