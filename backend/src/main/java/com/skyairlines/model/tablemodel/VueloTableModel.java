package com.skyairlines.model.tablemodel;

import com.skyairlines.dao.impl.VueloDAOImpl;
import com.skyairlines.model.entity.Aeronave;
import com.skyairlines.model.entity.Vuelo;
import com.skyairlines.util.DateUtils;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class VueloTableModel extends AbstractTableModel {

    private static final String[] COLUMN_NAMES = {"CÓDIGO", "FECHA", "CAPACIDAD", "MODELO DE AVIÓN", "ESTADO", "ACCIONES"};
    private static final Class<?>[] COLUMN_CLASSES = {String.class, String.class, Integer.class, String.class, String.class, Object.class};

    private List<Vuelo> data;

    public VueloTableModel(List<Vuelo> data) {
        this.data = data != null ? new ArrayList<>(data) : new ArrayList<>();
    }

    public void refreshData(List<Vuelo> newData) {
        this.data = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        fireTableDataChanged();
    }

    public void refreshData() {
        try {
            VueloDAOImpl dao = new VueloDAOImpl();
            this.data = dao.findAllWithDetails();
            fireTableDataChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vuelo getVueloAt(int row) {
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
        Vuelo vuelo = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return vuelo.getCodigoVuelo();
            case 1:
                return DateUtils.formatDateShort(vuelo.getFechaSalidaProgramada());
            case 2:
                Aeronave aeronave = vuelo.getAeronave();
                return aeronave != null ? aeronave.getCapacidadPasajeros() : null;
            case 3:
                Aeronave aeronave2 = vuelo.getAeronave();
                return aeronave2 != null ? aeronave2.getModelo() : "";
            case 4:
                return vuelo.getEstado();
            case 5:
                return null;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 5;
    }
}
