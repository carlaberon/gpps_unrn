package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

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
        getContentPane().setBackground(new Color(0xBFBFBF));
        setLayout(new BorderLayout());

        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        Estudiante estudiante = gestorDeProyectos.obtenerEstudianteAsignado(idProyecto);

        JPanel panelProyecto = new JPanel(new GridLayout(5, 2, 5, 5));
        panelProyecto.setBackground(new Color(0xBFBFBF));
        panelProyecto.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));

        panelProyecto.add(new JLabel("Nombre:"));
        panelProyecto.add(new JLabel(proyecto.getNombre()));

        panelProyecto.add(new JLabel("Descripción:"));
        panelProyecto.add(new JLabel(proyecto.getDescripcion()));

        panelProyecto.add(new JLabel("Área de Interés:"));
        panelProyecto.add(new JLabel(proyecto.getAreaDeInteres()));

        panelProyecto.add(new JLabel("Ubicación:"));
        panelProyecto.add(new JLabel(proyecto.getUbicacion()));

        panelProyecto.add(new JLabel("Estado del Proyecto:"));
        panelProyecto.add(new JLabel(proyecto.estadoProyecto() != null ? proyecto.estadoProyecto() : ""));

        add(panelProyecto, BorderLayout.NORTH);

        JPanel panelTutores = new JPanel(new GridBagLayout());
        panelTutores.setBackground(new Color(0xBFBFBF));
        panelTutores.setBorder(BorderFactory.createTitledBorder("Información de Tutores"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        int filaActual = 0;
        try {
            List<Tutor> tutores = gestorDeProyectos.obtenerTutoresPorProyecto(idProyecto);
            for (Tutor tutor : tutores) {
                if ("interno".equalsIgnoreCase(tutor.getTipo())) {
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);
                    filaActual++;

                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Email Tutor Interno:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.getEmail()), gbc);
                    filaActual++;
                } else if ("externo".equalsIgnoreCase(tutor.getTipo())) {
                    gbc.gridx = 0;
                    gbc.gridy = filaActual;
                    panelTutores.add(new JLabel("Tutor Externo:"), gbc);
                    gbc.gridx = 1;
                    panelTutores.add(new JLabel(tutor.nombre()), gbc);
                    filaActual++;

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

        JPanel panelEstudiante = null;
        if (estudiante != null) {
            panelEstudiante = new JPanel(new GridLayout(3, 2, 5, 5));
            panelEstudiante.setBackground(new Color(0xBFBFBF));
            panelEstudiante.setBorder(BorderFactory.createTitledBorder("Estudiante asignado"));

            panelEstudiante.add(new JLabel("Nombre:"));
            panelEstudiante.add(new JLabel(estudiante.nombre()));
            panelEstudiante.add(new JLabel("Email:"));
            panelEstudiante.add(new JLabel(estudiante.getEmail()));
            panelEstudiante.add(new JLabel("Legajo:"));
            panelEstudiante.add(new JLabel(estudiante.getLegajo()));
        }

        JPanel contenedorIzquierdo = new JPanel();
        contenedorIzquierdo.setBackground(new Color(0xBFBFBF));
        contenedorIzquierdo.setLayout(new BoxLayout(contenedorIzquierdo, BoxLayout.Y_AXIS));
        contenedorIzquierdo.add(panelTutores);
        if (panelEstudiante != null) {
            contenedorIzquierdo.add(Box.createVerticalStrut(10));
            contenedorIzquierdo.add(panelEstudiante);
        }

        JPanel panelPlanTrabajo = construirPanelPlanTrabajo(proyecto, idProyecto);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2, 10, 0));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelCentro.setBackground(new Color(0xBFBFBF));
        panelCentro.add(contenedorIzquierdo);
        panelCentro.add(panelPlanTrabajo);
        add(panelCentro, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(0xBFBFBF));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(btnCerrar);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel construirPanelPlanTrabajo(Proyecto proyecto, int idProyecto) {
        JPanel panelPlanTrabajo = new JPanel();
        panelPlanTrabajo.setLayout(new BoxLayout(panelPlanTrabajo, BoxLayout.Y_AXIS));
        panelPlanTrabajo.setBackground(new Color(0xBFBFBF));
        panelPlanTrabajo.setBorder(BorderFactory.createTitledBorder("Plan de Trabajo"));

        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        if (plan == null) {
            JOptionPane.showMessageDialog(this,
                    "Este proyecto aún no tiene un plan de trabajo creado.",
                    "Información", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            return panelPlanTrabajo;
        }

        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.setBackground(new Color(0xBFBFBF));
        panelDetallesPlan.add(new JLabel("Fecha de Inicio:"));
        panelDetallesPlan.add(new JLabel(plan.fechaInicio().toString()));
        panelDetallesPlan.add(new JLabel("Fecha de Fin:"));
        panelDetallesPlan.add(new JLabel(plan.fechaFin().toString()));
        panelDetallesPlan.add(new JLabel("Recursos:"));
        panelDetallesPlan.add(new JLabel(plan.recursos()));
        panelDetallesPlan.add(new JLabel("Total de Horas:"));
        panelDetallesPlan.add(new JLabel(String.valueOf(plan.cantHoras())));
        panelPlanTrabajo.add(panelDetallesPlan);

        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBackground(new Color(0xBFBFBF));
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(plan.porcentajeDeFinalizado());
        barra.setStringPainted(true);
        barra.setPreferredSize(new Dimension(200, 14));
        panelActividadesTop.add(barra, BorderLayout.EAST);
        panelPlanTrabajo.add(panelActividadesTop);

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
        tabla.setRowHeight(50);
        tabla.setBackground(new Color(0xBFBFBF));
        tabla.setGridColor(Color.DARK_GRAY);

        // Encabezado personalizado
        JTableHeader header = tabla.getTableHeader();
        header.setReorderingAllowed(false);
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(getFont().deriveFont(Font.BOLD, 13f));
                setBackground(new Color(0x4A4A4A));
                setForeground(Color.WHITE);
                setOpaque(true);
            }

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                ((JComponent) c).setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
                return c;
            }
        });

        // Renderizado para la descripción multilínea
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                                                           boolean sel, boolean foc, int r, int c) {
                JTextArea ta = new JTextArea(v.toString());
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setRows(3);
                ta.setFont(t.getFont());
                ta.setBackground(sel ? t.getSelectionBackground() : t.getBackground());
                ta.setForeground(sel ? t.getSelectionForeground() : t.getForeground());
                return ta;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(350, 200));
        scroll.getViewport().setBackground(new Color(0xBFBFBF));
        panelPlanTrabajo.add(scroll);

        return panelPlanTrabajo;
    }
}
