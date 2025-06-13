package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class VerProyecto extends JFrame {
    private final GestorDeProyectos gestorDeProyectos;
    private final int idProyecto;

    public VerProyecto(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        List<Actividad> actividades = plan.actividades();

        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        getContentPane().setBackground(new Color(0xBFBFBF));

        JPanel pDatos = new JPanel(new GridLayout(5, 2, 5, 5));
        pDatos.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));
        pDatos.setBackground(new Color(0xBFBFBF));
        pDatos.add(new JLabel("Nombre:"));
        pDatos.add(new JLabel(proyecto.getNombre()));
        pDatos.add(new JLabel("Descripción:"));
        pDatos.add(new JLabel(proyecto.getDescripcion()));
        pDatos.add(new JLabel("Área de Interés:"));
        pDatos.add(new JLabel(proyecto.getAreaDeInteres()));
        pDatos.add(new JLabel("Ubicación:"));
        pDatos.add(new JLabel(proyecto.getUbicacion()));
        pDatos.add(new JLabel("Estado:"));
        pDatos.add(new JLabel(proyecto.estadoProyecto()));
        add(pDatos, BorderLayout.NORTH);

        String[] cols = {"Descripción", "Finalizado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2;
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(60);
        tabla.setBackground(new Color(0xE0E0E0));
        tabla.setGridColor(new Color(0x555555));
        tabla.setShowGrid(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 2));

        JTableHeader header = tabla.getTableHeader();
        header.setBackground(new Color(0x3A3A3A));
        header.setForeground(Color.WHITE);
        header.setFont(header.getFont().deriveFont(Font.BOLD));
        header.setBorder(new LineBorder(new Color(0x222222), 2));

        tabla.getColumnModel().getColumn(0).setPreferredWidth(400);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value.toString());
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setRows(2);
                textArea.setFont(table.getFont());
                textArea.setMargin(new Insets(2, 2, 2, 2));
                textArea.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                textArea.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return textArea;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(700, 300));
        scroll.getViewport().setBackground(new Color(0xBFBFBF));
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x333333), 2));

        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.setBorder(BorderFactory.createTitledBorder("Detalles del Plan"));
        panelDetallesPlan.setBackground(new Color(0xBFBFBF));

        int totalHoras = actividades.stream().mapToInt(Actividad::horas).sum();

        panelDetallesPlan.add(new JLabel("Fecha de Inicio:"));
        panelDetallesPlan.add(new JLabel(plan.fechaInicio().toString()));
        panelDetallesPlan.add(new JLabel("Fecha de Fin:"));
        panelDetallesPlan.add(new JLabel(plan.fechaFin().toString()));
        panelDetallesPlan.add(new JLabel("Recursos:"));
        panelDetallesPlan.add(new JLabel(plan.recursos()));
        panelDetallesPlan.add(new JLabel("Total de Horas:"));
        panelDetallesPlan.add(new JLabel(String.valueOf(totalHoras)));

        JPanel contenedorCentro = new JPanel();
        contenedorCentro.setLayout(new BoxLayout(contenedorCentro, BoxLayout.Y_AXIS));
        contenedorCentro.setBackground(new Color(0xBFBFBF));

        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        panelActividadesTop.setBackground(new Color(0xBFBFBF));

        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

        int porcentaje = plan.porcentajeDeFinalizado();
        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(200, 16));

        JPanel panelProgresoFinal = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelProgresoFinal.setBackground(new Color(0xBFBFBF));
        panelProgresoFinal.add(barraProgreso);

        if (porcentaje == 100) {
            if (!gestorDeProyectos.existeInformeFinal(idProyecto)) {
                JButton btnInformeFinal = new JButton("Cargar Informe Final");
                btnInformeFinal.addActionListener(e -> {
                    Component parent = btnInformeFinal.getParent();
                    while (parent != null && !(parent instanceof JFrame)) {
                        parent = parent.getParent();
                    }
                    JFrame parentFrame = null;
                    if (parent instanceof JFrame) {
                        parentFrame = (JFrame) parent;
                    }

                    Proyectos proyectos = new Proyectos(gestorDeProyectos);
                    JFrame finalParentFrame = parentFrame;
                    new VentanaCargarInforme(proyectos, null, v -> {
                        if (finalParentFrame != null) {
                            finalParentFrame.dispose();
                            new VerProyecto(gestorDeProyectos, idProyecto).setVisible(true);
                        }
                    }, "FINAL", idProyecto).setVisible(true);
                });
                panelProgresoFinal.add(btnInformeFinal);
            } else {
                JButton btnVerInformeFinal = new JButton("Ver Informe Final");
                btnVerInformeFinal.addActionListener(e -> {
                    Informe informe = gestorDeProyectos.obtenerInformeFinal(idProyecto);
                    Proyectos proyectos = new Proyectos(gestorDeProyectos);
                    new VerInformeEstudiante(proyectos, informe).setVisible(true);
                });
                panelProgresoFinal.add(btnVerInformeFinal);
            }
        }

        panelActividadesTop.add(panelProgresoFinal, BorderLayout.EAST);

        contenedorCentro.add(panelDetallesPlan);
        contenedorCentro.add(panelActividadesTop);
        contenedorCentro.add(scroll);

        add(contenedorCentro, BorderLayout.CENTER);

        for (Actividad a : actividades) {
            String accion;
            if (!a.requiereInforme()) {
                accion = a.finalizado() ? "—" : "Finalizar";
            } else if (a.getIdInforme() > 0) {
                accion = "Ver Informe";
            } else {
                accion = "Cargar Informe";
            }
            model.addRow(new Object[]{a.getDescripcion(), a.finalizado() ? "Sí" : "No", accion});
        }

        tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
        tabla.getColumn("Acciones").setCellEditor(new BotonEditor(new JCheckBox(), actividades, gestorDeProyectos, model, barraProgreso, plan, tabla, idProyecto));

        setVisible(true);
    }

    /* ========= RENDERER ========= */
    static class BotonRenderer implements javax.swing.table.TableCellRenderer {
        private final JButton button = new JButton();
        private final JLabel dash = new JLabel("—", SwingConstants.CENTER);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            String text = value.toString();
            if (text.equals("—")) {
                dash.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                dash.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return dash;
            }

            button.setText(text);
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            button.setOpaque(true);
            return button;
        }
    }

    /* ========= EDITOR ========= */
    static class BotonEditor extends DefaultCellEditor {
        private final JButton button;
        private final List<Actividad> actividades;
        private final GestorDeProyectos gestor;
        private final DefaultTableModel model;
        private final JProgressBar barra;
        private final PlanDeTrabajo plan;
        private final JTable tabla;
        private final JLabel dash = new JLabel("—", SwingConstants.CENTER);
        private final int idProyecto;
        private int currentRow;
        private JFrame parentFrame;
        private String currentAction;

        public BotonEditor(JCheckBox checkBox, List<Actividad> actividades, GestorDeProyectos gestor,
                           DefaultTableModel model, JProgressBar barra, PlanDeTrabajo plan, JTable tabla, int idProyecto) {
            super(checkBox);
            this.actividades = actividades;
            this.gestor = gestor;
            this.model = model;
            this.barra = barra;
            this.plan = plan;
            this.tabla = tabla;
            this.idProyecto = idProyecto;
            this.button = new JButton();
            button.addActionListener(e -> accion());
        }

        private void accion() {
            Actividad act = actividades.get(currentRow);
            String accionActual = model.getValueAt(currentRow, 2).toString();

            // Obtener la ventana padre
            Component parent = button.getParent();
            while (parent != null && !(parent instanceof JFrame)) {
                parent = parent.getParent();
            }
            if (parent instanceof JFrame) {
                parentFrame = (JFrame) parent;
            }

            if (accionActual.equals("Finalizar")) {
                int confirmacion = JOptionPane.showConfirmDialog(
                        parentFrame,
                        "¿Está seguro que desea finalizar esta actividad?",
                        "Confirmar finalización",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        gestor.finalizarActividad(act.getIdActividad());
                        JOptionPane.showMessageDialog(parentFrame, "Actividad finalizada correctamente.");

                        // Cerrar y reabrir la ventana principal
                        if (parentFrame != null) {
                            parentFrame.dispose();
                            new VerProyecto(gestor, idProyecto).setVisible(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame, "Error al finalizar la actividad: " + ex.getMessage());
                    }
                }
            } else if (accionActual.equals("Cargar Informe")) {
                Proyectos proyectos = new Proyectos(gestor);
                new VentanaCargarInforme(proyectos, act, v -> {
                    // Cerrar y reabrir la ventana principal
                    if (parentFrame != null) {
                        parentFrame.dispose();
                        new VerProyecto(gestor, idProyecto).setVisible(true);
                    }
                }, "Parcial", idProyecto).setVisible(true);
            } else if (accionActual.equals("Ver Informe")) {
                Proyectos proyectos = new Proyectos(gestor);
                Informe informe = gestor.obtenerInforme(act.getIdInforme());
                if (informe != null) {
                    new VerInformeEstudiante(proyectos, informe).setVisible(true);
                }
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.currentRow = row;
            Actividad act = actividades.get(row);
            this.currentAction = value.toString();

            if (act.finalizado() && !act.requiereInforme()) {
                return dash;
            }

            button.setText(currentAction);
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            button.setOpaque(true);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return currentAction;
        }
    }
}

