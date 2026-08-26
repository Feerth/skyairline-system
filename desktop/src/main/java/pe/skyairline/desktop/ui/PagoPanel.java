package pe.skyairline.desktop.ui;

import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pe.skyairline.desktop.api.ApiClient;
import pe.skyairline.desktop.model.EstadoPago;
import pe.skyairline.desktop.model.MetodoPago;
import pe.skyairline.desktop.model.Pago;
import pe.skyairline.desktop.model.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagoPanel extends CrudPanel<Pago> {

    public PagoPanel() {
        super("Pagos");
    }

    @Override
    protected List<TableColumn<Pago, ?>> buildColumns() {
        List<TableColumn<Pago, ?>> cols = new ArrayList<>();

        TableColumn<Pago, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colId.setMaxWidth(60);

        TableColumn<Pago, String> colReserva = new TableColumn<>("Reserva");
        colReserva.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getReserva() == null ? "" : "#" + d.getValue().getReserva().getId() + " - " +
                        (d.getValue().getReserva().getPasajero() == null ? "" : d.getValue().getReserva().getPasajero().toString())));

        TableColumn<Pago, String> colMonto = new TableColumn<>("Monto (S/)");
        colMonto.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getMonto() == null ? "" : d.getValue().getMonto().toString()));

        TableColumn<Pago, String> colMetodo = new TableColumn<>("Metodo");
        colMetodo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getMetodoPago() == null ? "" : d.getValue().getMetodoPago().name()));

        TableColumn<Pago, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getEstado() == null ? "" : d.getValue().getEstado().name()));

        TableColumn<Pago, String> colFecha = new TableColumn<>("Fecha pago");
        colFecha.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getFechaPago() == null ? "" : d.getValue().getFechaPago().toString()));

        cols.add(colId);
        cols.add(colReserva);
        cols.add(colMonto);
        cols.add(colMetodo);
        cols.add(colEstado);
        cols.add(colFecha);
        return cols;
    }

    @Override
    protected List<Pago> fetchAll() throws Exception {
        CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Pago.class);
        return ApiClient.getList("/pagos", type);
    }

    @Override
    protected void crear(Pago item) throws Exception {
        ApiClient.post("/pagos", item, Pago.class);
    }

    @Override
    protected void actualizar(Pago item) throws Exception {
        ApiClient.put("/pagos/" + item.getId(), item, Pago.class);
    }

    @Override
    protected void eliminar(Pago item) throws Exception {
        ApiClient.delete("/pagos/" + item.getId());
    }

    @Override
    protected Optional<Pago> mostrarFormulario(Pago itemExistente) {
        Dialog<Pago> dialog = new Dialog<>();
        dialog.setTitle(itemExistente == null ? "Nuevo pago" : "Editar pago");

        ComboBox<Reserva> reserva = new ComboBox<>();
        TextField monto = new TextField();
        ComboBox<MetodoPago> metodo = new ComboBox<>();
        metodo.getItems().addAll(MetodoPago.values());
        ComboBox<EstadoPago> estado = new ComboBox<>();
        estado.getItems().addAll(EstadoPago.values());
        DatePicker fechaPago = new DatePicker(LocalDate.now());

        try {
            CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Reserva.class);
            reserva.getItems().addAll(ApiClient.<Reserva>getList("/reservas", type));
        } catch (Exception ex) {
            mostrarError(ex);
        }

        reserva.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Reserva r) {
                if (r == null) return "";
                String pasajeroNombre = r.getPasajero() == null ? "" : r.getPasajero().toString();
                return "#" + r.getId() + " - " + pasajeroNombre;
            }
            @Override
            public Reserva fromString(String s) { return null; }
        });

        if (itemExistente != null) {
            reserva.setValue(itemExistente.getReserva());
            monto.setText(itemExistente.getMonto() == null ? "" : itemExistente.getMonto().toString());
            metodo.setValue(itemExistente.getMetodoPago());
            estado.setValue(itemExistente.getEstado());
            if (itemExistente.getFechaPago() != null) fechaPago.setValue(itemExistente.getFechaPago());
        } else {
            estado.setValue(EstadoPago.PENDIENTE);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("Reserva:"), reserva);
        grid.addRow(1, new Label("Monto (S/):"), monto);
        grid.addRow(2, new Label("Metodo de pago:"), metodo);
        grid.addRow(3, new Label("Estado:"), estado);
        grid.addRow(4, new Label("Fecha de pago:"), fechaPago);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (reserva.getValue() == null) {
                    mostrarAviso("Debes seleccionar una reserva.");
                    return null;
                }
                try {
                    Pago p = itemExistente == null ? new Pago() : itemExistente;
                    p.setReserva(reserva.getValue());
                    p.setMonto(new BigDecimal(monto.getText().trim().replace(",", ".")));
                    p.setMetodoPago(metodo.getValue());
                    p.setEstado(estado.getValue());
                    p.setFechaPago(fechaPago.getValue());
                    return p;
                } catch (NumberFormatException nfe) {
                    mostrarAviso("El monto ingresado no es un numero valido.");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
