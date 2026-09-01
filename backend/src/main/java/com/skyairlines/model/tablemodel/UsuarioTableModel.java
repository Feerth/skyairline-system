package com.skyairlines.model.tablemodel;

import com.skyairlines.model.entity.Usuario;
import com.skyairlines.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class UsuarioTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"ID", "EMAIL", "ROL", "ACTIVO", "FECHA CREACIÓN"};
    private static final Class<?>[] COLUMN_CLASSES = {Integer.class, String.class, String.class, String.class, String.class};

    private List<Usuario> data;

    public UsuarioTableModel(List<Usuario> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<Usuario> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public Usuario getUsuarioAt(int row) {
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
        Usuario usuario = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return usuario.getId();
            case 1:
                return usuario.getEmail();
            case 2:
                return usuario.getRol() != null ? usuario.getRol().getDbValue() : "";
            case 3:
                return Boolean.TRUE.equals(usuario.getActivo()) ? "SÍ" : "NO";
            case 4:
                return DateUtils.formatDateTime(usuario.getCreatedAt());
            default:
                return null;
        }
    }
}
