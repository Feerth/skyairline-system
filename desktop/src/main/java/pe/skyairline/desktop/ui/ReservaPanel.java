package pe.skyairline.desktop.ui;

import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pe.skyairline.desktop.api.ApiClient;
import pe.skyairline.desktop.model.EstadoReserva;
import pe.skyairline.desktop.model.Pasajero;
import pe.skyairline.desktop.model.Reserva;
import pe.skyairline.desktop.model.Vuelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservaPanel extends CrudPanel<Reserva> {

    public ReservaPanel() {
        super("Reservas");
    }

    @Override
    protected List<TableColumn<Reserva, ?>> buildColumns() {
        List<TableColumn<Reserva, ?>> cols = new ArrayList<>();

        TableColumn<Reserva, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colId.setMaxWidth(60);

        TableColumn<Reserva, String> colPasajero = new TableColumn<>("Pasajero");
        colPasajero.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getPasajero() == null ? "" : d.getValue().getPasajero().toString()));

        TableColumn<Reserva, String> colVuelo = new TableColumn<>("Vuelo");
        colVuelo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getVuelo() == null ? "" : d.getValue().getVuelo().toString()));

        TableColumn<Reserva, Number> colPasajes = new TableColumn<>("N. pasajes");
        colPasajes.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getNumPasajes()));

        TableColumn<Reserva, String> colTotal = new TableColumn<>("Total (S/)");
        colTotal.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getTotal() == null ? "" : d.getValue().getTotal().toString()));

        TableColumn<Reserva, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getEstado() == null ? "" : d.getValue().getEstado().name()));

        TableColumn<Reserva, String> colFecha = new TableColumn<>("Fecha reserva");
        colFecha.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getFechaReserva() == null ? "" : d.getValue().getFechaReserva().toString()));

        cols.add(colId);
        cols.add(colPasajero);
        cols.add(colVuelo);
        cols.add(colPasajes);
        cols.add(colTotal);
        cols.add(colEstado);
        cols.add(colFecha);
        return cols;
    }

    @Override
    protected List<Reserva> fetchAll() throws Exception {
        CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Reserva.class);
        return ApiClient.getList("/reservas", type);
    }

    @Override
    protected void crear(Reserva item) throws Exception {
        ApiClient.post("/reservas", item, Reserva.class);
    }

    @Override
    protected void actualizar(Reserva item) throws Exception {
        ApiClient.put("/reservas/" + item.getId(), item, Reserva.class);
    }

    @Override
    protected void eliminar(Reserva item) throws Exception {
        ApiClient.delete("/reservas/" + item.getId());
    }

    @Override
    protected Optional<Reserva> mostrarFormulario(Reserva itemExistente) {
        Dialog<Reserva> dialog = new Dialog<>();
        dialog.setTitle(itemExistente == null ? "Nueva reserva" : "Editar reserva");

        ComboBox<Pasajero> pasajero = new ComboBox<>();
        ComboBox<Vuelo> vuelo = new ComboBox<>();
        Spinner<Integer> numPasajes = new Spinner<>(1, 20, 1);
        numPasajes.setEditable(true);
        ComboBox<EstadoReserva> estado = new ComboBox<>();
        estado.getItems().addAll(EstadoReserva.values());
        DatePicker fechaReserva = new DatePicker(LocalDate.now());

        try {
            CollectionType pasajeroType = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Pasajero.class);
            pasajero.getItems().addAll(ApiClient.<Pasajero>getList("/pasajeros", pasajeroType));

            CollectionType vueloType = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Vuelo.class);
            vuelo.getItems().addAll(ApiClient.<Vuelo>getList("/vuelos", vueloType));
        } catch (Exception ex) {
            mostrarError(ex);
        }

        if (itemExistente != null) {
            pasajero.setValue(itemExistente.getPasajero());
            vuelo.setValue(itemExistente.getVuelo());
            numPasajes.getValueFactory().setValue(itemExistente.getNumPasajes() <= 0 ? 1 : itemExistente.getNumPasajes());
            estado.setValue(itemExistente.getEstado());
            if (itemExistente.getFechaReserva() != null) fechaReserva.setValue(itemExistente.getFechaReserva());
        } else {
            estado.setValue(EstadoReserva.PENDIENTE);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("Pasajero:"), pasajero);
        grid.addRow(1, new Label("Vuelo:"), vuelo);
        grid.addRow(2, new Label("N. de pasajes:"), numPasajes);
        grid.addRow(3, new Label("Estado:"), estado);
        grid.addRow(4, new Label("Fecha reserva:"), fechaReserva);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (pasajero.getValue() == null || vuelo.getValue() == null) {
                    mostrarAviso("Debes seleccionar un pasajero y un vuelo.");
                    return null;
                }
                Reserva r = itemExistente == null ? new Reserva() : itemExistente;
                r.setPasajero(pasajero.getValue());
                r.setVuelo(vuelo.getValue());
                r.setNumPasajes(numPasajes.getValue());
                r.setEstado(estado.getValue());
                r.setFechaReserva(fechaReserva.getValue());
                return r;
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
