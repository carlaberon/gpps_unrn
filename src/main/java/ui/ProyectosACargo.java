package ui;

import model.GestorDeProyectos;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProyectosACargo extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idUsuario;

    public ProyectosACargo(GestorDeProyectos gestor, int idUsuario) {
        this.gestorDeProyectos = gestor;
        this.idUsuario = idUsuario;

        setTitle("Proyectos a cargo");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Obtener proyectos resumidos
        List<Proyecto> proyectos = gestorDeProyectos.listarProyectosRelacionados(idUsuario);

        // Modelo de tabla
        String[] columnas = {"ID", "Nombre", "Descripción"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // deshabilita edición
            }
        };

        for (Proyecto p : proyectos) {
            modelo.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getDescripcion()
            });
        }

        // Crear tabla
        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(25);

        // Ocultar columna de ID
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scroll = new JScrollPane(tabla);

        // Botón Ver Proyecto
        JButton btnVerProyecto = new JButton("Ver proyecto");
        btnVerProyecto.setEnabled(false);

        // Habilitar botón solo si hay selección
        tabla.getSelectionModel().addListSelectionListener(e -> {
            btnVerProyecto.setEnabled(tabla.getSelectedRow() != -1);
        });

        // Acción del botón
        btnVerProyecto.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila != -1) {
                int idProyecto = (int) modelo.getValueAt(fila, 0);
                new VerProyectoInformes(gestorDeProyectos, idProyecto);
            }
        });

        // Layout
        add(new JLabel("Seleccione un proyecto:"), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(btnVerProyecto, BorderLayout.SOUTH);

        setVisible(true);
    }
}
