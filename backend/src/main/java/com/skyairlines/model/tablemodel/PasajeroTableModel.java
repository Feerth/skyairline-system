package com.skyairlines.model.tablemodel;

import com.skyairlines.config.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class PasajeroTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"#", "PASAJERO", "ASIENTO ASIGNADO", "CATEGORÍA DE ASIENTO"};
    private static final Class<?>[] COLUMN_CLASSES = {Integer.class, String.class, String.class, String.class};

    private List<Object[]> data;

    public PasajeroTableModel(List<Object[]> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<Object[]> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public void refreshData(Integer idVuelo) {
        try {
            List<Object[]> lista = new ArrayList<>();
            String sql = "SELECT ROW_NUMBER() OVER (ORDER BY p.id) AS numero, " +
                    "p.nombre || ' ' || p.apellido AS nombre_completo, " +
                    "aa.codigo_asiento, aa.clase " +
                    "FROM boletos b " +
                    "JOIN pasajeros p ON b.id_pasajero = p.id " +
                    "JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id " +
                    "JOIN asientos_aeronave aa ON va.id_asiento_aeronave = aa.id " +
                    "WHERE va.id_vuelo = ? AND b.estado = 'EMITIDO' " +
                    "ORDER BY aa.codigo_asiento";
            try (Connection conn = ConexionBD.INSTANCE.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idVuelo);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new Object[]{
                                rs.getInt("numero"),
                                rs.getString("nombre_completo"),
                                rs.getString("codigo_asiento"),
                                rs.getString("clase")
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
