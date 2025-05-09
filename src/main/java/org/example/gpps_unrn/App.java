package org.example.gpps_unrn;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/gpps_unrn/seleccionar_proyecto.fxml"));
        Scene scene = new Scene(loader.load(), 400, 300);

        stage.setTitle("Seleccionar Proyecto PPS");
        stage.setScene(scene);
        stage.show();
    }
}
