package pe.skyairline.desktop.ui;

import com.fasterxml.jackson.databind.type.CollectionType;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import pe.skyairline.desktop.api.ApiClient;
import pe.skyairline.desktop.model.Pasajero;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PasajeroPanel extends CrudPanel<Pasajero> {

    public PasajeroPanel() {
        super("Pasajeros");
    }

    @Override
    protected List<TableColumn<Pasajero, ?>> buildColumns() {
        List<TableColumn<Pasajero, ?>> cols = new ArrayList<>();

        TableColumn<Pasajero, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colId.setMaxWidth(60);

        TableColumn<Pasajero, String> colNombres = new TableColumn<>("Nombres");
        colNombres.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombres()));

        TableColumn<Pasajero, String> colApellidos = new TableColumn<>("Apellidos");
        colApellidos.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellidos()));

        TableColumn<Pasajero, String> colDoc = new TableColumn<>("Documento");
        colDoc.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDocumento()));

        TableColumn<Pasajero, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEmail()));

        TableColumn<Pasajero, String> colTelefono = new TableColumn<>("Telefono");
        colTelefono.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTelefono()));

        cols.add(colId);
        cols.add(colNombres);
        cols.add(colApellidos);
        cols.add(colDoc);
        cols.add(colEmail);
        cols.add(colTelefono);
        return cols;
    }

    @Override
    protected List<Pasajero> fetchAll() throws Exception {
        CollectionType type = ApiClient.mapper().getTypeFactory().constructCollectionType(List.class, Pasajero.class);
        return ApiClient.getList("/pasajeros", type);
    }

    @Override
    protected void crear(Pasajero item) throws Exception {
        ApiClient.post("/pasajeros", item, Pasajero.class);
    }

    @Override
    protected void actualizar(Pasajero item) throws Exception {
        ApiClient.put("/pasajeros/" + item.getId(), item, Pasajero.class);
    }

    @Override
    protected void eliminar(Pasajero item) throws Exception {
        ApiClient.delete("/pasajeros/" + item.getId());
    }

    @Override
    protected Optional<Pasajero> mostrarFormulario(Pasajero itemExistente) {
        Dialog<Pasajero> dialog = new Dialog<>();
        dialog.setTitle(itemExistente == null ? "Nuevo pasajero" : "Editar pasajero");

        TextField nombres = new TextField();
        TextField apellidos = new TextField();
        TextField documento = new TextField();
        documento.setPromptText("DNI / Pasaporte");
        TextField email = new TextField();
        TextField telefono = new TextField();

        if (itemExistente != null) {
            nombres.setText(itemExistente.getNombres());
            apellidos.setText(itemExistente.getApellidos());
            documento.setText(itemExistente.getDocumento());
            email.setText(itemExistente.getEmail());
            telefono.setText(itemExistente.getTelefono());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));
        grid.addRow(0, new Label("Nombres:"), nombres);
        grid.addRow(1, new Label("Apellidos:"), apellidos);
        grid.addRow(2, new Label("Documento:"), documento);
        grid.addRow(3, new Label("Email:"), email);
        grid.addRow(4, new Label("Telefono:"), telefono);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(bt -> {
            if (bt == ButtonType.OK) {
                Pasajero p = itemExistente == null ? new Pasajero() : itemExistente;
                p.setNombres(nombres.getText());
                p.setApellidos(apellidos.getText());
                p.setDocumento(documento.getText());
                p.setEmail(email.getText());
                p.setTelefono(telefono.getText());
                return p;
            }
            return null;
        });

        return dialog.showAndWait();
    }
}
