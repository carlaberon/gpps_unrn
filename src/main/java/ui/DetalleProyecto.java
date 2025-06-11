package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class DetalleProyecto extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;

    public DetalleProyecto(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Obtener datos del proyecto
        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);

        // Panel de información del proyecto
        JPanel panelProyecto = new JPanel(new GridLayout(6, 2, 5, 5));
        panelProyecto.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));

        panelProyecto.add(new JLabel("ID:"));
        panelProyecto.add(new JLabel(String.valueOf(proyecto.getId())));

        panelProyecto.add(new JLabel("Nombre:"));
        panelProyecto.add(new JLabel(proyecto.getNombre()));

        panelProyecto.add(new JLabel("Descripción:"));
        panelProyecto.add(new JLabel(proyecto.getDescripcion()));

        panelProyecto.add(new JLabel("Área de Interés:"));
        panelProyecto.add(new JLabel(proyecto.getAreaDeInteres()));

        panelProyecto.add(new JLabel("Ubicación:"));
        panelProyecto.add(new JLabel(proyecto.getUbicacion()));

        panelProyecto.add(new JLabel("Estado:"));
        panelProyecto.add(new JLabel(proyecto.getEstado() ? "Aprobado" : "Pendiente"));

        add(panelProyecto, BorderLayout.NORTH);

        // Panel de información de tutores (desde base de datos)
        JPanel panelTutores = new JPanel(new GridBagLayout());
        panelTutores.setBorder(BorderFactory.createTitledBorder("Información de Tutores"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); // Espaciado entre componentes
        gbc.anchor = GridBagConstraints.WEST; // Alineación a la izquierda
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridx = 0;
        gbc.gridy = 0;

        try {
            List<Tutor> tutores = gestorDeProyectos.obtenerTutoresPorProyecto(idProyecto);
            for (Tutor tutor : tutores) {
                if (tutor.getTipo().equalsIgnoreCase("interno")) {
                    panelTutores.add(new JLabel("Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panelTutores.add(new JLabel("Email Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.getEmail()), gbc);

                    gbc.gridy++;
                } else if (tutor.getTipo().equalsIgnoreCase("externo")) {
                    gbc.gridx = 0;
                    panelTutores.add(new JLabel("Tutor Externo:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);

                    gbc.gridx = 0;
                    gbc.gridy++;
                    panelTutores.add(new JLabel("Email Tutor Externo:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.getEmail()), gbc);

                    gbc.gridy++;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener tutores desde la base de datos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Envolver el panel de tutores en un contenedor alineado a la izquierda
        JPanel contenedorTutores = new JPanel(new FlowLayout(FlowLayout.LEFT));
        contenedorTutores.add(panelTutores);

        // Agregar al centro de la ventana
        add(contenedorTutores, BorderLayout.CENTER);

        // Panel de detalles del plan y actividades
        JPanel panelPlanTrabajo = new JPanel();
        panelPlanTrabajo.setLayout(new BoxLayout(panelPlanTrabajo, BoxLayout.Y_AXIS));
        panelPlanTrabajo.setBorder(BorderFactory.createTitledBorder("Plan de Trabajo"));

// Panel superior: detalles del plan
        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));

        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);

        if (plan == null) {
            JOptionPane.showMessageDialog(this,
                    "Este proyecto aún no tiene un plan de trabajo creado.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return;
        }

        List<Actividad> actividades = plan.actividades();

        int totalHoras = plan.cantHoras();

        panelDetallesPlan.add(new JLabel("Fecha de Inicio:"));
        panelDetallesPlan.add(new JLabel(plan.fechaInicio().toString()));

        panelDetallesPlan.add(new JLabel("Fecha de Fin:"));
        panelDetallesPlan.add(new JLabel(plan.fechaFin().toString()));

        panelDetallesPlan.add(new JLabel("Recursos:"));
        panelDetallesPlan.add(new JLabel(plan.recursos()));

        panelDetallesPlan.add(new JLabel("Total de Horas:"));
        panelDetallesPlan.add(new JLabel(String.valueOf(totalHoras)));

        panelPlanTrabajo.add(panelDetallesPlan);

        // Actividades y progreso
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

        panelPlanTrabajo.add(panelActividadesTop);

        // Tabla de actividades
        String[] columnas = {"Descripción", "Finalizado", "Acciones"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo la columna de acciones
            }
        };

        for (var actividad : actividades) {
            modeloTabla.addRow(new Object[]{
                    actividad.descripcion(),
                    actividad.finalizado() ? "Sí" : "No",
                    "Ver" // Botón o texto temporal
            });
        }

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(30);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(350, 200));
        panelPlanTrabajo.add(scroll);

        // Panel central con tutores + plan de trabajo
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 0)); // separación entre paneles
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(contenedorTutores);
        panelCentro.add(panelPlanTrabajo);

// Reemplazar el BorderLayout.CENTER con el nuevo panel combinado
        add(panelCentro, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }
}
