package com.skyairlines.view.main;

import com.skyairlines.util.SessionManager;
import com.skyairlines.view.asiento.AsientoPanel;
import com.skyairlines.view.cliente.ClientePanel;
import com.skyairlines.view.components.HeaderPanel;
import com.skyairlines.view.components.SideNavigationPanel;
import com.skyairlines.view.dashboard.DashboardPanel;
import com.skyairlines.view.empleado.EmpleadoPanel;
import com.skyairlines.view.equipaje.EquipajePanel;
import com.skyairlines.view.pasajero.PasajeroPanel;
import com.skyairlines.view.reporte.ReportePanel;
import com.skyairlines.view.usuario.UsuarioPanel;
import com.skyairlines.view.vuelo.VueloDetallePanel;
import com.skyairlines.view.vuelo.VueloGestionPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    private final SideNavigationPanel sideNavigationPanel;
    private final HeaderPanel headerPanel;

    private static final String CARD_DASHBOARD = "DASHBOARD";
    private static final String CARD_VUELOS = "VUELOS";
    private static final String CARD_CLIENTES = "CLIENTES";
    private static final String CARD_PERSONAL = "PERSONAL";
    private static final String CARD_USUARIOS = "USUARIOS";
    private static final String CARD_REPORTES = "REPORTES";
    private static final String CARD_VUELO_DETALLE = "VUELO_DETALLE";
    private static final String CARD_EQUIPAJE = "EQUIPAJE";
    private static final String CARD_ASIENTO = "ASIENTO";
    private static final String CARD_PASAJERO = "PASAJERO";

    private JPanel dynamicDetallePanel;
    private JPanel dynamicEquipajePanel;
    private JPanel dynamicAsientoPanel;
    private JPanel dynamicPasajeroPanel;

    public MainFrame() {
        setTitle("Sky Airlines Per\u00fa - Panel Administrativo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 700));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(Color.WHITE);

        headerPanel = new HeaderPanel(() -> {
            SessionManager.getInstance().logout();
            com.skyairlines.view.auth.LoginFrame loginFrame = new com.skyairlines.view.auth.LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        });

        sideNavigationPanel = new SideNavigationPanel(this::navigateTo);

        cardPanel.add(new DashboardPanel(), CARD_DASHBOARD);
        cardPanel.add(new VueloGestionPanel(this), CARD_VUELOS);
        cardPanel.add(new ClientePanel(), CARD_CLIENTES);
        cardPanel.add(new EmpleadoPanel(), CARD_PERSONAL);
        cardPanel.add(new UsuarioPanel(), CARD_USUARIOS);
        cardPanel.add(new ReportePanel(), CARD_REPORTES);

        sideNavigationPanel.setActiveButton(CARD_DASHBOARD);
        cardLayout.show(cardPanel, CARD_DASHBOARD);

        setLayout(new BorderLayout());
        add(sideNavigationPanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);
    }

    private void navigateTo(String panelName) {
        if (panelName == null) return;

        sideNavigationPanel.setActiveButton(panelName);
        cardLayout.show(cardPanel, panelName);
        headerPanel.refreshHeader();
    }

    public void showPanel(String name) {
        navigateTo(name);
    }

    public void showVueloDetalle(Integer vueloId) {
        if (dynamicDetallePanel != null) {
            cardPanel.remove(dynamicDetallePanel);
        }
        dynamicDetallePanel = new VueloDetallePanel(this, vueloId);
        cardPanel.add(dynamicDetallePanel, CARD_VUELO_DETALLE);
        cardLayout.show(cardPanel, CARD_VUELO_DETALLE);
        sideNavigationPanel.setActiveButton(CARD_VUELOS);
    }

    public void showVuelos() {
        sideNavigationPanel.setActiveButton(CARD_VUELOS);
        cardLayout.show(cardPanel, CARD_VUELOS);
        headerPanel.refreshHeader();
    }

    public void showEquipaje(Integer vueloId) {
        if (dynamicEquipajePanel != null) {
            cardPanel.remove(dynamicEquipajePanel);
        }
        dynamicEquipajePanel = new EquipajePanel(this, vueloId);
        cardPanel.add(dynamicEquipajePanel, CARD_EQUIPAJE);
        cardLayout.show(cardPanel, CARD_EQUIPAJE);
        sideNavigationPanel.setActiveButton(CARD_VUELOS);
    }

    public void showAsientos(Integer vueloId) {
        if (dynamicAsientoPanel != null) {
            cardPanel.remove(dynamicAsientoPanel);
        }
        dynamicAsientoPanel = new AsientoPanel(this, vueloId);
        cardPanel.add(dynamicAsientoPanel, CARD_ASIENTO);
        cardLayout.show(cardPanel, CARD_ASIENTO);
        sideNavigationPanel.setActiveButton(CARD_VUELOS);
    }

    public void showPasajeros(Integer vueloId) {
        if (dynamicPasajeroPanel != null) {
            cardPanel.remove(dynamicPasajeroPanel);
        }
        dynamicPasajeroPanel = new PasajeroPanel(this, vueloId);
        cardPanel.add(dynamicPasajeroPanel, CARD_PASAJERO);
        cardLayout.show(cardPanel, CARD_PASAJERO);
        sideNavigationPanel.setActiveButton(CARD_VUELOS);
    }
}
