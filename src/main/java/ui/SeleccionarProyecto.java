package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        Color fondo = Color.decode("#BFBFBF");
        getContentPane().setBackground(fondo);
        setLayout(new BorderLayout());

        // Tabla con estilo visual como en ProyectosACargo
        String[] columnas = {"Nombre", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableProyectos = new JTable(modelo);
        tableProyectos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableProyectos.setRowHeight(40);
        tableProyectos.setIntercellSpacing(new Dimension(0, 2));
        tableProyectos.setShowGrid(true);
        tableProyectos.setShowHorizontalLines(true);
        tableProyectos.setShowVerticalLines(false);
        tableProyectos.setGridColor(Color.BLACK); // línea negra entre filas
        tableProyectos.setBackground(fondo);
        tableProyectos.setSelectionBackground(new Color(184, 207, 229));
        tableProyectos.setFont(new Font("SansSerif", Font.PLAIN, 13));

        JTableHeader header = tableProyectos.getTableHeader();
        header.setBackground(new Color(64, 64, 64));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("SansSerif", Font.BOLD, 13));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setHorizontalAlignment(SwingConstants.LEFT);

        JScrollPane scrollPane = new JScrollPane(tableProyectos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(fondo);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        btnVerDetalles = new JButton("Ver Detalles");
        btnVerDetalles.addActionListener(e -> verDetallesProyecto());

        btnSeleccionar = new JButton("Seleccionar Proyecto");
        btnSeleccionar.addActionListener(e -> seleccionarProyecto());

        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(fondo);
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

            boolean asignacionExitosa = proyectoDAO.asignarEstudianteAProyecto(idEstudiante, proyectoSeleccionado.getId());

            if (asignacionExitosa) {
                mostrarMensaje("Éxito", "Estudiante asignado correctamente al proyecto: " + proyectoSeleccionado.getNombre());
                dispose();
            } else {
                mostrarMensaje("Error", "No se pudo asignar el estudiante al proyecto.");
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("ya tiene un proyecto asignado")) {
                JOptionPane.showMessageDialog(this, "Ya has seleccionado un proyecto. Debes esperar que se genere el convenio correspondiente",
                        "Advertencia", JOptionPane.WARNING_MESSAGE);
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
