package pe.skyairline.desktop;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pe.skyairline.desktop.ui.AeropuertoPanel;
import pe.skyairline.desktop.ui.PagoPanel;
import pe.skyairline.desktop.ui.PasajeroPanel;
import pe.skyairline.desktop.ui.ReservaPanel;
import pe.skyairline.desktop.ui.VueloPanel;

/**
 * Sistema de escritorio de Sky Airline Peru (uso interno / administrativo).
 * Gestiona aeropuertos, vuelos, pasajeros, reservas y pagos consumiendo
 * la misma API REST que utiliza la web publica.
 *
 * Requiere que el modulo "backend" este corriendo en http://localhost:8080
 */
public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");

        HBox header = new HBox();
        header.getStyleClass().add("app-header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));
        Label title = new Label("Sky Airline Peru · Sistema administrativo");
        title.getStyleClass().add("app-title");
        header.getChildren().add(title);
        root.setTop(header);

        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabs.getTabs().addAll(
                new Tab("Aeropuertos", new AeropuertoPanel()),
                new Tab("Vuelos", new VueloPanel()),
                new Tab("Pasajeros", new PasajeroPanel()),
                new Tab("Reservas", new ReservaPanel()),
                new Tab("Pagos", new PagoPanel())
        );
        root.setCenter(tabs);

        Scene scene = new Scene(root, 1180, 720);
        scene.getStylesheets().add(getClass().getResource("/pe/skyairline/desktop/styles.css").toExternalForm());

        stage.setTitle("Sky Airline Peru - Sistema de escritorio");
        stage.setScene(scene);
        stage.setMinWidth(980);
        stage.setMinHeight(620);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
