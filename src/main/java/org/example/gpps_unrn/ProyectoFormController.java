package org.example.gpps_unrn;

import database.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import java.sql.Connection;
import model.*;

public class ProyectoFormController {
    @FXML private TextField nombreField;
    @FXML private TextArea descripcionField;
    @FXML private TextField areaField;
    @FXML private TextField ubicacionField;
    @FXML private ComboBox<Estudiante> estudianteCombo;
    @FXML private ComboBox<Director> directorCombo;
    @FXML private ComboBox<Tutor> supervisorCombo;
    @FXML private CheckBox estadoCheck;

    private ProyectoDAO proyectoDAO;
    private EstudianteDAO estudianteDAO;
    private DirectorDAO directorDAO;
    private TutorDAO tutorDAO;

    @FXML
    public void initialize() {
        try {
            Connection conn = Conn.getConnection();
            proyectoDAO = new ProyectoDAOJDBC(conn);
            estudianteDAO = new EstudianteDAOJDBC(conn);
            directorDAO = new DirectorDAOJDBC(conn);
            tutorDAO = new TutorDAOJDBC(conn);

            // Cargar los ComboBox
            cargarEstudiantes();
            cargarDirectores();
            cargarTutores();
        } catch (Exception e) {
            mostrarAlerta("Error de conexión", e.getMessage());
        }
    }

    private void cargarEstudiantes() {
        ObservableList<Estudiante> estudiantes = FXCollections.observableArrayList(estudianteDAO.obtenerTodos());
        estudianteCombo.setItems(estudiantes);
        estudianteCombo.setConverter(new StringConverter<Estudiante>() {
            @Override
            public String toString(Estudiante estudiante) {
                return estudiante != null ? estudiante.getNombre() + " (" + estudiante.getLegajo() + ")" : "";
            }

            @Override
            public Estudiante fromString(String string) {
                return null; // No se necesita
            }
        });
    }

    private void cargarDirectores() {
        ObservableList<Director> directores = FXCollections.observableArrayList(directorDAO.obtenerTodos());
        directorCombo.setItems(directores);
        directorCombo.setConverter(new StringConverter<Director>() {
            @Override
            public String toString(Director director) {
                return director != null ? director.getNombre() : "";
            }

            @Override
            public Director fromString(String string) {
                return null; // No se necesita
            }
        });
    }

    private void cargarTutores() {
        ObservableList<Tutor> tutores = FXCollections.observableArrayList(tutorDAO.obtenerTodos());
        supervisorCombo.setItems(tutores);
        supervisorCombo.setConverter(new StringConverter<Tutor>() {
            @Override
            public String toString(Tutor tutor) {
                return tutor != null ? tutor.getNombre() + " (" + tutor.getTipo() + ")" : "";
            }

            @Override
            public Tutor fromString(String string) {
                return null; // No se necesita
            }
        });
    }

    @FXML
    public void guardarProyecto() {
        if (!validarCampos()) {
            return;
        }

        try {
            Proyecto proyecto = new Proyecto(
                0,
                nombreField.getText(),
                descripcionField.getText(),
                estadoCheck.isSelected(),
                areaField.getText(),
                estudianteCombo.getValue(),
                directorCombo.getValue(),
                supervisorCombo.getValue()
            );

            if (proyecto.esValido()) {
                proyectoDAO.guardar(proyecto);
                mostrarAlerta("Éxito", "Proyecto guardado correctamente");
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "Los datos del proyecto no son válidos");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar el proyecto: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();
        
        if (nombreField.getText().isEmpty()) {
            errores.append("- El nombre es obligatorio\n");
        }
        if (descripcionField.getText().isEmpty()) {
            errores.append("- La descripción es obligatoria\n");
        }
        if (areaField.getText().isEmpty()) {
            errores.append("- El área de interés es obligatoria\n");
        }
        if (estudianteCombo.getValue() == null) {
            errores.append("- Debe seleccionar un estudiante\n");
        }
        if (directorCombo.getValue() == null) {
            errores.append("- Debe seleccionar un director\n");
        }
        if (supervisorCombo.getValue() == null) {
            errores.append("- Debe seleccionar un supervisor\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta("Error de validación", errores.toString());
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void limpiarCampos() {
        nombreField.clear();
        descripcionField.clear();
        areaField.clear();
        ubicacionField.clear();
        estudianteCombo.setValue(null);
        directorCombo.setValue(null);
        supervisorCombo.setValue(null);
        estadoCheck.setSelected(false);
    }
}
