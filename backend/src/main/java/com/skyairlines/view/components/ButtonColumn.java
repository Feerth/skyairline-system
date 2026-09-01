package com.skyairlines.view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonColumn extends MouseAdapter {

    private final JTable table;
    private final AbstractAction action;
    private final int column;
    private final JButton renderButton;
    private int selectedRow = -1;

    private static final Color BUTTON_FG = new Color(0, 102, 204);
    private static final Color BUTTON_BG = new Color(240, 245, 255);
    private static final Color BUTTON_HOVER_BG = new Color(200, 220, 250);

    public ButtonColumn(JTable table, AbstractAction action, int column) {
        this.table = table;
        this.action = action;
        this.column = column;

        renderButton = new JButton("Ver Detalles");
        renderButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        renderButton.setForeground(BUTTON_FG);
        renderButton.setBackground(BUTTON_BG);
        renderButton.setFocusPainted(false);
        renderButton.setBorderPainted(false);
        renderButton.setOpaque(true);
        renderButton.setHorizontalAlignment(SwingConstants.CENTER);
        renderButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        renderButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BUTTON_FG, 1),
                BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));

        table.getColumnModel().getColumn(column).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(column).setPreferredWidth(130);
        table.getColumnModel().getColumn(column).setMinWidth(130);
        table.getColumnModel().getColumn(column).setMaxWidth(130);

        table.addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        int col = table.columnAtPoint(e.getPoint());

        if (row >= 0 && col == column) {
            selectedRow = row;
            table.setRowSelectionInterval(row, row);
            action.actionPerformed(new ActionEvent(table, ActionEvent.ACTION_PERFORMED, String.valueOf(row)));
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int row = table.rowAtPoint(e.getPoint());
        int col = table.columnAtPoint(e.getPoint());

        if (row >= 0 && col == column) {
            renderButton.setBackground(BUTTON_HOVER_BG);
            table.repaint();
        }
    }

    private class ButtonRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            renderButton.setText("Ver Detalles");
            renderButton.setBackground(BUTTON_BG);
            renderButton.setForeground(BUTTON_FG);

            if (isSelected) {
                renderButton.setBackground(new Color(220, 235, 255));
            }

            int mouseX = table.getMousePosition() != null ? table.getMousePosition().x : -1;
            int mouseY = table.getMousePosition() != null ? table.getMousePosition().y : -1;
            if (mouseX >= 0 && mouseY >= 0) {
                int hoverRow = table.rowAtPoint(new Point(mouseX, mouseY));
                int hoverCol = table.columnAtPoint(new Point(mouseX, mouseY));
                if (hoverRow == row && hoverCol == column) {
                    renderButton.setBackground(BUTTON_HOVER_BG);
                }
            }

            return renderButton;
        }
    }
}
