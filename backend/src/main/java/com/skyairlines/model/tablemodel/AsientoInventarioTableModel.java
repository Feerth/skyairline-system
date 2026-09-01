package com.skyairlines.model.tablemodel;

import com.skyairlines.model.entity.InventarioAsientoDTO;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class AsientoInventarioTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"CATEGORÍA", "TOTAL", "VENDIDOS", "DISPONIBLES", "CANCELADOS", "OCUPACIÓN (%)"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, Integer.class, Integer.class, Integer.class, Integer.class, String.class};

    private List<InventarioAsientoDTO> data;

    public AsientoInventarioTableModel(List<InventarioAsientoDTO> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<InventarioAsientoDTO> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public InventarioAsientoDTO getDTOAt(int row) {
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
        InventarioAsientoDTO dto = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return dto.getCategoria();
            case 1:
                return dto.getTotal();
            case 2:
                return dto.getVendidos();
            case 3:
                return dto.getDisponibles();
            case 4:
                return dto.getCancelados();
            case 5:
                return String.format("%.1f%%", dto.getOcupacion());
            default:
                return null;
        }
    }
}
