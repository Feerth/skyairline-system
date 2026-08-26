package pe.skyairline.desktop.ui;

import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pe.skyairline.desktop.api.ApiClient;
import pe.skyairline.desktop.model.Aeropuerto;
import pe.skyairline.desktop.model.EstadoVuelo;
import pe.skyairline.desktop.model.Vuelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VueloPanel extends CrudPanel<Vuelo> {

    public VueloPanel() {
        super("Vuelos");
    }

    @Override
    protected List<TableColumn<Vuelo, ?>> buildColumns() {
        List<TableColumn<Vuelo, ?>> cols = new ArrayList<>();

        TableColumn<Vuelo, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colId.setMaxWidth(50);

        TableColumn<Vuelo, String> colNumero = new TableColumn<>("N. Vuelo");
        colNumero.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNumeroVuelo()));

        TableColumn<Vuelo, String> colRuta = new TableColumn<>("Ruta");
        colRuta.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                (d.getValue().getOrigen() == null ? "?" : d.getValue().getOrigen().getCodigoIata()) + " -> " +
                        (d.getValue().getDestino() == null ? "?" : d.getValue().getDestino().getCodigoIata())));

        TableColumn<Vuelo, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getFechaSalida() == null ? "" : d.getValue().getFechaSalida().toString()));

        TableColumn<Vuelo, String> colHora = new TableColumn<>("Salida");
        colHora.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getHoraSalida() == null ? "" : d.getValue().getHoraSalida().toString()));

        TableColumn<Vuelo, String> colPrecio = new TableColumn<>("Precio (S/)");
        colPrecio.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getPrecio() == null ? "" : d.getValue().getPrecio().toString()));

        TableColumn<Vuelo, Number> colAsientos = new TableColumn<>("Asientos");
        colAsientos.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getAsientosDisponibles()));

        TableColumn<Vuelo, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(
                d.getValue().getEstado() == null ? "" : d.getValue().getEstado().name()));

        cols.add(colId);
        cols.add(colNumero);
        cols.add(colRuta);
        cols.add(colFecha);
        cols.add(colHora);
        cols.add(colPrecio);
        cols.add(colAsientos);
        cols.add(colEstado);
        return cols;
    }

    @Override
    protected List<Vuelo> fetchAll() throws Exception {
        CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Vuelo.class);
        return ApiClient.getList("/vuelos", type);
    }

    @Override
    protected void crear(Vuelo item) throws Exception {
        ApiClient.post("/vuelos", item, Vuelo.class);
    }

    @Override
    protected void actualizar(Vuelo item) throws Exception {
        ApiClient.put("/vuelos/" + item.getId(), item, Vuelo.class);
    }

    @Override
    protected void eliminar(Vuelo item) throws Exception {
        ApiClient.delete("/vuelos/" + item.getId());
    }

    @Override
    protected Optional<Vuelo> mostrarFormulario(Vuelo itemExistente) {
        Dialog<Vuelo> dialog = new Dialog<>();
        dialog.setTitle(itemExistente == null ? "Nuevo vuelo" : "Editar vuelo");

        TextField numeroVuelo = new TextField();
        numeroVuelo.setPromptText("Ej: SKY-501");
        ComboBox<Aeropuerto> origen = new ComboBox<>();
        ComboBox<Aeropuerto> destino = new ComboBox<>();
        DatePicker fechaSalida = new DatePicker(LocalDate.now().plusDays(7));
        TextField horaSalida = new TextField();
        horaSalida.setPromptText("HH:mm, ej: 06:15");
        TextField horaLlegada = new TextField();
        horaLlegada.setPromptText("HH:mm, ej: 07:35");
        TextField precio = new TextField();
        Spinner<Integer> asientos = new Spinner<>(1, 300, 168);
        asientos.setEditable(true);
        ComboBox<EstadoVuelo> estado = new ComboBox<>();
        estado.getItems().addAll(EstadoVuelo.values());

        try {
            CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Aeropuerto.class);
            List<Aeropuerto> aeropuertos = ApiClient.getList("/aeropuertos", type);
            origen.getItems().addAll(aeropuertos);
            destino.getItems().addAll(aeropuertos);
        } catch (Exception ex) {
            mostrarError(ex);
        }

        if (itemExistente != null) {
            numeroVuelo.setText(itemExistente.getNumeroVuelo());
            origen.setValue(itemExistente.getOrigen());
            destino.setValue(itemExistente.getDestino());
            if (itemExistente.getFechaSalida() != null) fechaSalida.setValue(itemExistente.getFechaSalida());
            if (itemExistente.getHoraSalida() != null) horaSalida.setText(itemExistente.getHoraSalida().toString());
            if (itemExistente.getHoraLlegada() != null) horaLlegada.setText(itemExistente.getHoraLlegada().toString());
            precio.setText(itemExistente.getPrecio() == null ? "" : itemExistente.getPrecio().toString());
            asientos.getValueFactory().setValue(itemExistente.getAsientosDisponibles() <= 0 ? 168 : itemExistente.getAsientosDisponibles());
            estado.setValue(itemExistente.getEstado());
        } else {
            estado.setValue(EstadoVuelo.PROGRAMADO);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("N. de vuelo:"), numeroVuelo);
        grid.addRow(1, new Label("Origen:"), origen);
        grid.addRow(2, new Label("Destino:"), destino);
        grid.addRow(3, new Label("Fecha salida:"), fechaSalida);
        grid.addRow(4, new Label("Hora salida:"), horaSalida);
        grid.addRow(5, new Label("Hora llegada:"), horaLlegada);
        grid.addRow(6, new Label("Precio (S/):"), precio);
        grid.addRow(7, new Label("Asientos disp.:"), asientos);
        grid.addRow(8, new Label("Estado:"), estado);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                if (origen.getValue() == null || destino.getValue() == null) {
                    mostrarAviso("Debes seleccionar origen y destino.");
                    return null;
                }
                if (origen.getValue().equals(destino.getValue())) {
                    mostrarAviso("El origen y el destino no pueden ser el mismo aeropuerto.");
                    return null;
                }
                try {
                    Vuelo v = itemExistente == null ? new Vuelo() : itemExistente;
                    v.setNumeroVuelo(numeroVuelo.getText());
                    v.setOrigen(origen.getValue());
                    v.setDestino(destino.getValue());
                    v.setFechaSalida(fechaSalida.getValue());
                    v.setHoraSalida(horaSalida.getText().isBlank() ? null : LocalTime.parse(horaSalida.getText().trim()));
                    v.setHoraLlegada(horaLlegada.getText().isBlank() ? null : LocalTime.parse(horaLlegada.getText().trim()));
                    v.setPrecio(new BigDecimal(precio.getText().trim().replace(",", ".")));
                    v.setAsientosDisponibles(asientos.getValue());
                    v.setEstado(estado.getValue());
                    return v;
                } catch (Exception ex) {
                    mostrarAviso("Revisa el formato de precio (numero) y horas (HH:mm).");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
