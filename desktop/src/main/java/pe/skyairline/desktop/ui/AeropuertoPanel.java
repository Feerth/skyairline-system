package pe.skyairline.desktop.ui;

import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pe.skyairline.desktop.api.ApiClient;
import pe.skyairline.desktop.model.Aeropuerto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AeropuertoPanel extends CrudPanel<Aeropuerto> {

    public AeropuertoPanel() {
        super("Aeropuertos");
    }

    @Override
    protected List<TableColumn<Aeropuerto, ?>> buildColumns() {
        List<TableColumn<Aeropuerto, ?>> cols = new ArrayList<>();

        TableColumn<Aeropuerto, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colId.setMaxWidth(60);

        TableColumn<Aeropuerto, String> colCodigo = new TableColumn<>("IATA");
        colCodigo.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCodigoIata()));

        TableColumn<Aeropuerto, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));

        TableColumn<Aeropuerto, String> colCiudad = new TableColumn<>("Ciudad");
        colCiudad.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCiudad()));

        TableColumn<Aeropuerto, String> colPais = new TableColumn<>("Pais");
        colPais.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getPais()));

        cols.add(colId);
        cols.add(colCodigo);
        cols.add(colNombre);
        cols.add(colCiudad);
        cols.add(colPais);
        return cols;
    }

    @Override
    protected List<Aeropuerto> fetchAll() throws Exception {
        CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Aeropuerto.class);
        return ApiClient.getList("/aeropuertos", type);
    }

    @Override
    protected void crear(Aeropuerto item) throws Exception {
        ApiClient.post("/aeropuertos", item, Aeropuerto.class);
    }

    @Override
    protected void actualizar(Aeropuerto item) throws Exception {
        ApiClient.put("/aeropuertos/" + item.getId(), item, Aeropuerto.class);
    }

    @Override
    protected void eliminar(Aeropuerto item) throws Exception {
        ApiClient.delete("/aeropuertos/" + item.getId());
    }

    @Override
    protected Optional<Aeropuerto> mostrarFormulario(Aeropuerto itemExistente) {
        Dialog<Aeropuerto> dialog = new Dialog<>();
        dialog.setTitle(itemExistente == null ? "Nuevo aeropuerto" : "Editar aeropuerto");

        TextField codigo = new TextField();
        codigo.setPromptText("Ej: LIM");
        codigo.setTextFormatter(new javafx.scene.control.TextFormatter<String>(change -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));
        TextField nombre = new TextField();
        TextField ciudad = new TextField();
        TextField pais = new TextField("Peru");

        if (itemExistente != null) {
            codigo.setText(itemExistente.getCodigoIata());
            nombre.setText(itemExistente.getNombre());
            ciudad.setText(itemExistente.getCiudad());
            pais.setText(itemExistente.getPais());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("Codigo IATA:"), codigo);
        grid.addRow(1, new Label("Nombre:"), nombre);
        grid.addRow(2, new Label("Ciudad:"), ciudad);
        grid.addRow(3, new Label("Pais:"), pais);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                Aeropuerto a = itemExistente == null ? new Aeropuerto() : itemExistente;
                a.setCodigoIata(codigo.getText().trim().toUpperCase());
                a.setNombre(nombre.getText());
                a.setCiudad(ciudad.getText());
                a.setPais(pais.getText());
                return a;
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
