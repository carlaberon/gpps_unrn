package front;

import model.Proyecto;
import model.ProyectoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SeleccionarProyecto extends JFrame {

    private JTable tableProyectos;
    private JButton btnSeleccionar;
    private final ProyectoDAO proyectoDAO;

    public SeleccionarProyecto(ProyectoDAO proyectoDAO) {
        this.proyectoDAO = proyectoDAO;

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

        btnSeleccionar = new JButton("Seleccionar");
        btnSeleccionar.addActionListener(e -> seleccionarProyecto());

        JPanel panelBoton = new JPanel();
        panelBoton.add(btnSeleccionar);
        add(panelBoton, BorderLayout.SOUTH);
    }

    private void cargarProyectosDisponibles() {
        DefaultTableModel model = (DefaultTableModel) tableProyectos.getModel();
        model.setRowCount(0);

        try {
            List<Proyecto> proyectos = proyectoDAO.obtenerProyectos();
            for (Proyecto p : proyectos) {
                model.addRow(new Object[]{p.getNombre(), p.getDescripcion()});
            }
        } catch (Exception e) {
            mostrarMensaje("Error", "No se pudieron cargar los proyectos.\n" + e.getMessage());
        }
    }

    private void seleccionarProyecto() {
        int selectedRow = tableProyectos.getSelectedRow();
        if (selectedRow == -1) {
            mostrarMensaje("Atención", "Debe seleccionar un proyecto.");
            return;
        }

        String nombreProyecto = tableProyectos.getValueAt(selectedRow, 0).toString();
        mostrarMensaje("Proyecto seleccionado", nombreProyecto);
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }



}
