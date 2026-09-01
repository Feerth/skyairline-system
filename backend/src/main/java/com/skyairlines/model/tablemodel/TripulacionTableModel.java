package com.skyairlines.model.tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TripulacionTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"ROL", "NOMBRE", "LICENCIA", "EXPERIENCIA"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, String.class, String.class, String.class};

    private List<String[]> data;

    public TripulacionTableModel(List<String[]> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<String[]> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public String[] getRowAt(int row) {
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
        String[] row = data.get(rowIndex);
        if (row != null && columnIndex < row.length) {
            return row[columnIndex];
        }
        return null;
    }
}
