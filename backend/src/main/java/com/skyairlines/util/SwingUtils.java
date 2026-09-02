package com.skyairlines.util;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class SwingUtils {

    private SwingUtils() {
    }

    public static void configureTable(JTable table) {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(new Color(200, 200, 200));
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFont(new Font("Dialog", Font.PLAIN, 17));
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Dialog", Font.BOLD, 17));
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 42));
    }

    public static JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFont(new Font("Dialog", Font.BOLD, 17));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(185, 42));
        return button;
    }

    public static boolean showConfirmDialog(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(
                parent,
                message,
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return result == JOptionPane.YES_OPTION;
    }

    public static void showInfoDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void showErrorDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.ERROR_MESSAGE
        );
    }

    public static void showWarningDialog(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(
                parent,
                message,
                title,
                JOptionPane.WARNING_MESSAGE
        );
    }

    public static void centerOnScreen(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = frame.getWidth();
        int height = frame.getHeight();
        int x = (screenSize.width - width) / 2;
        int y = (screenSize.height - height) / 2;
        frame.setLocation(x, y);
    }

    public static DefaultTableCellRenderer formatTableCellRenderer() {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        renderer.setVerticalAlignment(SwingConstants.CENTER);
        renderer.setFont(new Font("Dialog", Font.PLAIN, 17));
        renderer.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return renderer;
    }
}
