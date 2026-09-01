package com.skyairlines.model.tablemodel;

import com.skyairlines.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EquipajeTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"ID EQUIPAJE", "PASAJERO", "CATEGORÍA DE PESO", "ESTADO ACTUAL"};
    private static final Class<?>[] COLUMN_CLASSES = {Integer.class, String.class, String.class, String.class};

    private List<Object[]> data;

    public EquipajeTableModel(List<Object[]> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<Object[]> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public void refreshData(Integer idVuelo) {
        try {
            List<Object[]> lista = new ArrayList<>();
            String sql = "SELECT e.id, " +
                    "COALESCE(p.nombre || ' ' || p.apellido, 'N/A') AS pasajero, " +
                    "e.categoria_peso, e.estado_actual " +
                    "FROM equipajes e " +
                    "JOIN boletos b ON e.id_boleto = b.id " +
                    "JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id " +
                    "LEFT JOIN pasajeros p ON b.id_pasajero = p.id " +
                    "WHERE va.id_vuelo = ? ORDER BY e.id";
            try (Connection conn = ConexionBD.INSTANCE.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idVuelo);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Object[]{
                                rs.getInt("id"),
                                rs.getString("pasajero"),
                                rs.getString("categoria_peso"),
                                rs.getString("estado_actual")
                        });
                    }
                }
            }
            this.data = lista;
            fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshDataAll() {
        try {
            List<Object[]> lista = new ArrayList<>();
            String sql = "SELECT e.id, " +
                    "COALESCE(p.nombre || ' ' || p.apellido, 'N/A') AS pasajero, " +
                    "e.categoria_peso, e.estado_actual " +
                    "FROM equipajes e " +
                    "JOIN boletos b ON e.id_boleto = b.id " +
                    "LEFT JOIN pasajeros p ON b.id_pasajero = p.id " +
                    "ORDER BY e.id";
            try (Connection conn = ConexionBD.INSTANCE.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("pasajero"),
                            rs.getString("categoria_peso"),
                            rs.getString("estado_actual")
                    });
                }
            }
            this.data = lista;
            fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
