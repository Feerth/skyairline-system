package com.skyairlines.model.tablemodel;

import com.skyairlines.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EmpleadoTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"ID", "CÓDIGO", "NOMBRE", "APELLIDO", "CARGO", "EMAIL"};
    private static final Class<?>[] COLUMN_CLASSES = {Integer.class, String.class, String.class, String.class, String.class, String.class};

    private List<Object[]> data;

    public EmpleadoTableModel(List<Object[]> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<Object[]> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public void refreshData() {
        try {
            List<Object[]> lista = new ArrayList<>();
            String sql = "SELECT e.id, e.codigo_empleado, e.nombre, e.apellido, e.cargo, u.email " +
                    "FROM empleados e " +
                    "LEFT JOIN usuarios u ON e.id_usuario = u.id " +
                    "ORDER BY e.id";
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
            this.data = lista;
            fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object[] getRowAt(int row) {
        if (row >= 0 && row < data.size()) {
            return data.get(row);
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return COLUMN_CLASSES[columnIndex];
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object[] row = data.get(rowIndex);
        if (row != null && columnIndex < row.length) {
            return row[columnIndex];
        }
        return null;
    }
}
