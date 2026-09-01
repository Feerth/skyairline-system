package com.skyairlines.model.tablemodel;

import com.skyairlines.config.ConexionBD;
import com.skyairlines.model.entity.Alerta;
import com.skyairlines.util.DateUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AlertaTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"TÍTULO", "DESCRIPCIÓN", "NIVEL", "FECHA"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, String.class, String.class, String.class};

    private List<Alerta> data;

    public AlertaTableModel(List<Alerta> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public Alerta getAlertaAt(int row) {
        if (row >= 0 && row < data.size()) {
            return data.get(row);
        }
        return null;
    }

    public void refreshData(List<Alerta> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public void refreshData() {
        try {
            List<Alerta> lista = new ArrayList<>();
            String sql = "SELECT id, titulo, descripcion, nivel, activa, fecha_creacion " +
                    "FROM alertas_operativas WHERE activa = true ORDER BY fecha_creacion DESC";
            try (Connection conn = ConexionBD.INSTANCE.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("fecha_creacion");
                    lista.add(new Alerta(
                            rs.getInt("id"),
                            rs.getString("titulo"),
                            rs.getString("descripcion"),
                            rs.getString("nivel"),
                            rs.getBoolean("activa"),
                            ts != null ? ts.toLocalDateTime().atOffset(ZoneOffset.UTC) : null
                    ));
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
        Alerta alerta = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return alerta.getTitulo();
            case 1:
                return alerta.getDescripcion();
            case 2:
                return alerta.getNivel();
            case 3:
                return DateUtils.formatDateTime(alerta.getFechaCreacion());
            default:
                return null;
        }
    }
}
