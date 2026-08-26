package pe.skyairline.desktop.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;

/**
 * Panel CRUD generico y reutilizable (patron Template Method).
 * Cada modulo (Aeropuertos, Vuelos, Pasajeros, Reservas, Pagos) extiende esta clase
 * y solo define: las columnas de la tabla, el formulario de alta/edicion,
 * y las llamadas a la API para listar/crear/actualizar/eliminar.
 */
public abstract class CrudPanel<T> extends BorderPane {

    protected final TableView<T> tabla = new TableView<>();
    protected final ObservableList<T> datos = FXCollections.observableArrayList();
    protected final Label estadoLabel = new Label();

    public CrudPanel(String titulo) {
        setPadding(new Insets(16));

        Label heading = new Label(titulo);
        heading.getStyleClass().add("panel-heading");

        Button btnNuevo = new Button("+ Nuevo");
        Button btnEditar = new Button("Editar");
        Button btnEliminar = new Button("Eliminar");
        Button btnRefrescar = new Button("Refrescar");
        btnNuevo.getStyleClass().add("btn-primary");

        btnNuevo.setOnAction(e -> onNuevo());
        btnEditar.setOnAction(e -> onEditar());
        btnEliminar.setOnAction(e -> onEliminar());
        btnRefrescar.setOnAction(e -> cargarDatos());

        HBox toolbar = new HBox(10, btnNuevo, btnEditar, btnEliminar, btnRefrescar);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setPadding(new Insets(12, 0, 12, 0));

        VBox topBox = new VBox(4, heading, toolbar);
        setTop(topBox);

        tabla.setItems(datos);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.getColumns().addAll(buildColumns());
        setCenter(tabla);

        estadoLabel.getStyleClass().add("status-label");
        setBottom(estadoLabel);
        BorderPane.setMargin(estadoLabel, new Insets(8, 0, 0, 0));

        cargarDatos();
    }

    protected abstract List<TableColumn<T, ?>> buildColumns();

    protected abstract List<T> fetchAll() throws Exception;

    protected abstract void crear(T item) throws Exception;

    protected abstract void actualizar(T item) throws Exception;

    protected abstract void eliminar(T item) throws Exception;

    /** Debe mostrar un dialogo modal y devolver el objeto editado, o Optional.empty() si se cancela. */
    protected abstract Optional<T> mostrarFormulario(T itemExistente);

    protected void cargarDatos() {
        estadoLabel.setText("Cargando...");
        new Thread(() -> {
            try {
                List<T> lista = fetchAll();
                Platform.runLater(() -> {
                    datos.setAll(lista);
                    estadoLabel.setText(lista.size() + " registro(s)");
                });
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    estadoLabel.setText("Error al cargar datos");
                    mostrarError(ex);
                });
            }
        }).start();
    }

    private void onNuevo() {
        Optional<T> resultado = mostrarFormulario(null);
        resultado.ifPresent(item -> ejecutarEnSegundoPlano(() -> crear(item)));
    }

    private void onEditar() {
        T seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAviso("Selecciona un registro de la tabla para editar.");
            return;
        }
        Optional<T> resultado = mostrarFormulario(seleccionado);
        resultado.ifPresent(item -> ejecutarEnSegundoPlano(() -> actualizar(item)));
    }

    private void onEliminar() {
        T seleccionado = tabla.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAviso("Selecciona un registro de la tabla para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Esta seguro que desea eliminar el registro seleccionado?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        confirm.showAndWait().ifPresent(bt -> {
            if (bt == ButtonType.YES) {
                ejecutarEnSegundoPlano(() -> eliminar(seleccionado));
            }
        });
    }

    private interface Accion {
        void ejecutar() throws Exception;
    }

    private void ejecutarEnSegundoPlano(Accion accion) {
        estadoLabel.setText("Procesando...");
        new Thread(() -> {
            try {
                accion.ejecutar();
                Platform.runLater(this::cargarDatos);
            } catch (Exception ex) {
                Platform.runLater(() -> {
                    estadoLabel.setText("Error");
                    mostrarError(ex);
                });
            }
        }).start();
    }

    protected void mostrarAviso(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, mensaje, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    protected void mostrarError(Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage(), ButtonType.OK);
        alert.setHeaderText("Ocurrio un error");
        alert.showAndWait();
    }
}
