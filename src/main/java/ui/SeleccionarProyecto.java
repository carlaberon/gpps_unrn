package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class SeleccionarProyecto extends JFrame {

    private final GestorDeProyectos proyectoDAO;
    private final int idEstudiante;
    private JTable tableProyectos;
    private JButton btnVerDetalles;
    private JButton btnSeleccionar;
    private Proyecto proyectoSeleccionado;

    public SeleccionarProyecto(GestorDeProyectos proyectoDAO, int idEstudiante) {
        this.proyectoDAO = proyectoDAO;
        this.idEstudiante = idEstudiante;

        setTitle("Seleccionar Proyecto");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        cargarProyectosDisponibles();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        tableProyectos = new JTable(new DefaultTableModel(
                new Object[]{"Nombre", "Descripción"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tableProyectos);
        add(scrollPane, BorderLayout.CENTER);

        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.addActionListener(e -> verDetallesProyecto());

        btnSeleccionar = new JButton("Seleccionar Proyecto");
        btnSeleccionar.addActionListener(e -> seleccionarProyecto());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnVerDetalles);
        panelBoton.add(btnSeleccionar);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarProyectosDisponibles() {
        DefaultTableModel model = (DefaultTableModel) tableProyectos.getModel();
        model.setRowCount(0);

        try {
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectosAprobados();
            for (Proyecto p : proyectos) {
                model.addRow(new Object[]{p.getNombre(), p.getDescripcion()});
            }
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudieron cargar los proyectos.\n" + e.getMessage());
        }
    }

    private void verDetallesProyecto() {
        int selectedRow = tableProyectos.getSelectedRow();
        if (selectedRow == -1) {
            mostrarMensaje("Atención", "Debe seleccionar un proyecto.");
            return;
        }

        try {
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectosAprobados();
            Proyecto p = proyectos.get(selectedRow);
            new DetalleProyecto(proyectoDAO, p.getId());
        } catch (Exception e) {
            mostrarMensaje("Error", "Error al cargar los detalles del proyecto.\n" + e.getMessage());
        }
    }

    private void seleccionarProyecto() {
        int selectedRow = tableProyectos.getSelectedRow();
        if (selectedRow == -1) {
            mostrarMensaje("Atención", "Debe seleccionar un proyecto.");
            return;
        }

        try {
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectosAprobados();
            proyectoSeleccionado = proyectos.get(selectedRow);

            // Llamar al método del gestor para asignar el estudiante al proyecto
            boolean asignacionExitosa = proyectoDAO.asignarEstudianteAProyecto(idEstudiante, proyectoSeleccionado.getId());

            if (asignacionExitosa) {
                mostrarMensaje("Éxito", "Estudiante asignado correctamente al proyecto: " + proyectoSeleccionado.getNombre());
                dispose(); // Cierra la ventana después de seleccionar
            } else {
                mostrarMensaje("Error", "No se pudo asignar el estudiante al proyecto.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("ya tiene un proyecto asignado")) {
                mostrarMensaje("Error", "No se puede seleccionar un nuevo proyecto porque ya tienes uno asignado.");
            } else {
                mostrarMensaje("Error", "Error al seleccionar el proyecto.\n" + e.getMessage());
            }
        } catch (Exception e) {
            mostrarMensaje("Error", "Error al seleccionar el proyecto.\n" + e.getMessage());
        }
    }

    public Proyecto getProyectoSeleccionado() {
        return proyectoSeleccionado;
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
}
