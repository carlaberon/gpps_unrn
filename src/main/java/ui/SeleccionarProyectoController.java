package ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Proyecto;
import database.*;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.util.List;

public class SeleccionarProyectoController {

    @FXML private TableView<Proyecto> tableProyectos;
    @FXML private TableColumn<Proyecto, String> colNombre;
    @FXML private TableColumn<Proyecto, String> colDescripcion;
    @FXML private Button btnSeleccionar;

    // Quitamos la necesidad de un estudiante para esta prueba
    // private Estudiante estudiante;

    // El método setEstudiante lo puedes comentar por ahora
    /*
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        cargarProyectosDisponibles();
    }
    */

    public void initialize() {

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        cargarProyectosDisponibles();
    }

    private void cargarProyectosDisponibles() {
        try (Connection conn = Conn.getConnection()) {
            ProyectoDAOJDBC dao = new ProyectoDAOJDBC(conn);
            List<Proyecto> proyectos = dao.obtenerProyectos();
            tableProyectos.getItems().setAll(proyectos);
        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudieron cargar los proyectos.\n" + e.getMessage());
        }
    }

    @FXML
    public void seleccionarProyecto(ActionEvent event) {
        Proyecto seleccionado = tableProyectos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Atención", "Debe seleccionar un proyecto.");
            return;
        }

        // Por ahora, solo mostramos el nombre del proyecto
        mostrarAlerta("Proyecto seleccionado", seleccionado.getNombre());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
