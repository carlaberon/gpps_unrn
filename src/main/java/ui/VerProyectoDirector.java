package ui;

import model.Actividad;
import model.GestorDeProyectos;
import model.PlanDeTrabajo;
import model.Proyecto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerProyectoDirector extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;

    public VerProyectoDirector(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Obtener datos del proyecto
        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        List<Actividad> actividades = plan.actividades();

        // Panel de información del proyecto
        JPanel panelProyecto = new JPanel(new GridLayout(5, 2, 5, 5));
        panelProyecto.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));

        panelProyecto.add(new JLabel("Nombre:"));
        panelProyecto.add(new JLabel(proyecto.getNombre()));

        panelProyecto.add(new JLabel("Descripción:"));
        panelProyecto.add(new JLabel(proyecto.getDescripcion()));

        panelProyecto.add(new JLabel("Área de Interés:"));
        panelProyecto.add(new JLabel(proyecto.getAreaDeInteres()));

        panelProyecto.add(new JLabel("Ubicación:"));
        panelProyecto.add(new JLabel(proyecto.getUbicacion()));

        panelProyecto.add(new JLabel("Aprobado:"));
        panelProyecto.add(new JLabel(proyecto.getEstado() ? "Sí" : "No"));

        add(panelProyecto, BorderLayout.NORTH);

        // Tabla de actividades (sin botón de acciones)
        String[] columnas = {"Descripción", "Finalizado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tabla);

        // Panel para Detalles del Plan
        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.setBorder(BorderFactory.createTitledBorder("Detalles del Plan"));

        int totalHoras = plan.cantHoras();

        panelDetallesPlan.add(new JLabel("Fecha de Inicio:"));
        panelDetallesPlan.add(new JLabel(plan.fechaInicio().toString()));

        panelDetallesPlan.add(new JLabel("Fecha de Fin:"));
        panelDetallesPlan.add(new JLabel(plan.fechaFin().toString()));

        panelDetallesPlan.add(new JLabel("Recursos:"));
        panelDetallesPlan.add(new JLabel(plan.recursos()));

        panelDetallesPlan.add(new JLabel("Total de Horas:"));
        panelDetallesPlan.add(new JLabel(String.valueOf(totalHoras)));

        // Contenedor vertical para Detalles del Plan y la tabla
        JPanel contenedorCentro = new JPanel();
        contenedorCentro.setLayout(new BoxLayout(contenedorCentro, BoxLayout.Y_AXIS));

        // Panel superior para título de actividades y progreso
        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);


        int porcentaje = plan.porcentajeDeFinalizado();

        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(200, 20));
        panelActividadesTop.add(barraProgreso, BorderLayout.EAST);

        // Agregar al contenedor existente
        contenedorCentro.add(panelDetallesPlan);
        contenedorCentro.add(panelActividadesTop);
        contenedorCentro.add(scroll);

        add(contenedorCentro, BorderLayout.CENTER);

        // Cargar actividades
        for (Actividad a : actividades) {
            modeloTabla.addRow(new Object[]{
                    a.getDescripcion(),
                    a.finalizado() ? "Sí" : "No"
            });
        }

        setVisible(true);
    }
}
