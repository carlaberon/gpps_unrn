package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Ventana de detalle de un proyecto; muestra:
 * • Datos básicos del proyecto
 * • Tutores asignados
 * • (Opcional) Estudiante asignado
 * • Plan de trabajo y sus actividades
 */
public class DetalleProyecto extends JFrame {
    private final GestorDeProyectos gestorDeProyectos;
    private final int idProyecto;

    public DetalleProyecto(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        /* ────────── Datos de dominio ────────── */
        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        Estudiante estudiante = gestorDeProyectos.obtenerEstudianteAsignado(idProyecto); // ← NUEVO

        /* ────────── Panel “Información del Proyecto” ────────── */
        JPanel panelProyecto = new JPanel(new GridLayout(4, 2, 5, 5));
        panelProyecto.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));

        panelProyecto.add(new JLabel("Nombre:"));
        panelProyecto.add(new JLabel(proyecto.getNombre()));

        panelProyecto.add(new JLabel("Descripción:"));
        panelProyecto.add(new JLabel(proyecto.getDescripcion()));

        panelProyecto.add(new JLabel("Área de Interés:"));
        panelProyecto.add(new JLabel(proyecto.getAreaDeInteres()));

        panelProyecto.add(new JLabel("Ubicación:"));
        panelProyecto.add(new JLabel(proyecto.getUbicacion()));

        add(panelProyecto, BorderLayout.NORTH);

        /* ────────── Panel “Información de Tutores” ────────── */
        JPanel panelTutores = new JPanel(new GridBagLayout());
        panelTutores.setBorder(BorderFactory.createTitledBorder("Información de Tutores"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int filaActual = 0;

        try {
            List<Tutor> tutores = gestorDeProyectos.obtenerTutoresPorProyecto(idProyecto);
            for (Tutor tutor : tutores) {
                if ("interno".equalsIgnoreCase(tutor.getTipo())) {
                    // Nombre tutor interno
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);
                    filaActual++;

                    // Email tutor interno
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Email Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.getEmail()), gbc);
                    filaActual++;
                } else if ("externo".equalsIgnoreCase(tutor.getTipo())) {
                    // Nombre tutor externo
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Tutor Externo:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);
                    filaActual++;

                    // Email tutor externo
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Email Tutor Externo:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.getEmail()), gbc);
                    filaActual++;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener tutores desde la base de datos:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        /* ────────── (Opcional) Panel “Estudiante asignado” ────────── */
        JPanel panelEstudiante = null;
        if (estudiante != null) {
            panelEstudiante = new JPanel(new GridLayout(3, 2, 5, 5));
            panelEstudiante.setBorder(BorderFactory.createTitledBorder("Estudiante asignado"));

            panelEstudiante.add(new JLabel("Nombre:"));
            panelEstudiante.add(new JLabel(estudiante.nombre()));

            panelEstudiante.add(new JLabel("Email:"));
            panelEstudiante.add(new JLabel(estudiante.getEmail()));

            panelEstudiante.add(new JLabel("Legajo:"));
            panelEstudiante.add(new JLabel(estudiante.getLegajo()));

        }

        /* Contenedor vertical para tutores (y estudiante si existe) */
        JPanel contenedorIzquierdo = new JPanel();
        contenedorIzquierdo.setLayout(new BoxLayout(contenedorIzquierdo, BoxLayout.Y_AXIS));
        contenedorIzquierdo.add(panelTutores);
        if (panelEstudiante != null) contenedorIzquierdo.add(Box.createVerticalStrut(10)); // separación
        if (panelEstudiante != null) contenedorIzquierdo.add(panelEstudiante);

        /* ────────── Panel “Plan de Trabajo” ────────── */
        JPanel panelPlanTrabajo = construirPanelPlanTrabajo(proyecto, idProyecto);

        /* Panel central: columna izquierda (tutores/estudiante) + columna derecha (plan) */
        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.add(contenedorIzquierdo);
        panelCentro.add(panelPlanTrabajo);
        add(panelCentro, BorderLayout.CENTER);

        /* ────────── Botón Cerrar ────────── */
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    /* ======= helpers ==================================================== */

    private static void insertarTuplaTutores(JPanel panel, GridBagConstraints gbc,
                                             String etiqueta1, String valor1,
                                             String etiqueta2, String valor2) {
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta1), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(valor1), gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JLabel(etiqueta2), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(valor2), gbc);

        gbc.gridy++;
    }

    /**
     * Crea el panel de plan de trabajo + actividades
     */
    private JPanel construirPanelPlanTrabajo(Proyecto proyecto, int idProyecto) {
        JPanel panelPlanTrabajo = new JPanel();
        panelPlanTrabajo.setLayout(new BoxLayout(panelPlanTrabajo, BoxLayout.Y_AXIS));
        panelPlanTrabajo.setBorder(BorderFactory.createTitledBorder("Plan de Trabajo"));

        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        if (plan == null) {
            JOptionPane.showMessageDialog(this,
                    "Este proyecto aún no tiene un plan de trabajo creado.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return panelPlanTrabajo;
        }

        /* 1) Datos del plan */
        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.add(new JLabel("Fecha de Inicio:"));
        panelDetallesPlan.add(new JLabel(plan.fechaInicio().toString()));
        panelDetallesPlan.add(new JLabel("Fecha de Fin:"));
        panelDetallesPlan.add(new JLabel(plan.fechaFin().toString()));
        panelDetallesPlan.add(new JLabel("Recursos:"));
        panelDetallesPlan.add(new JLabel(plan.recursos()));
        panelDetallesPlan.add(new JLabel("Total de Horas:"));
        panelDetallesPlan.add(new JLabel(String.valueOf(plan.cantHoras())));
        panelPlanTrabajo.add(panelDetallesPlan);

        /* 2) Barra de progreso */
        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(plan.porcentajeDeFinalizado());
        barra.setStringPainted(true);
        barra.setPreferredSize(new Dimension(200, 20));
        panelActividadesTop.add(barra, BorderLayout.EAST);
        panelPlanTrabajo.add(panelActividadesTop);

        /* 3) Tabla de actividades */
        String[] columnas = {"Descripción", "Horas"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        for (Actividad a : plan.actividades()) {
            modelo.addRow(new Object[]{a.descripcion(), a.horas()});
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(60);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(300);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(60);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int r, int c) {
                JTextArea ta = new JTextArea(v.toString());
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setRows(3);
                ta.setBackground(sel ? t.getSelectionBackground() : t.getBackground());
                ta.setForeground(sel ? t.getSelectionForeground() : t.getForeground());
                return ta;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(350, 200));
        panelPlanTrabajo.add(scroll);

        return panelPlanTrabajo;
    }
}
