package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerProyecto extends JFrame {
    private final GestorDeProyectos gestorDeProyectos;
    private final int idProyecto;

    public VerProyecto(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        /* ──────────  Dominio  ────────── */
        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        List<Actividad> actividades = plan.actividades();

        /* ──────────  UI principal  ────────── */
        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        /* --- Panel Proyecto --- */
        JPanel pDatos = new JPanel(new GridLayout(5, 2, 5, 5));
        pDatos.setBorder(BorderFactory.createTitledBorder("Información del Proyecto"));
        pDatos.add(new JLabel("Nombre:"));
        pDatos.add(new JLabel(proyecto.getNombre()));
        pDatos.add(new JLabel("Descripción:"));
        pDatos.add(new JLabel(proyecto.getDescripcion()));
        pDatos.add(new JLabel("Área de Interés:"));
        pDatos.add(new JLabel(proyecto.getAreaDeInteres()));
        pDatos.add(new JLabel("Ubicación:"));
        pDatos.add(new JLabel(proyecto.getUbicacion()));
        pDatos.add(new JLabel("Aprobado:"));
        pDatos.add(new JLabel(proyecto.getEstado() ? "Sí" : "No"));
        add(pDatos, BorderLayout.NORTH);

        /* --- Tabla de actividades --- */
        String[] cols = {"Descripción", "Finalizado", "Acciones"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 2;
            }
        };

        JTable tabla = new JTable(model);
        tabla.setRowHeight(100);
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

        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.setBorder(BorderFactory.createTitledBorder("Detalles del Plan"));

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

        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

        int totalActividades = actividades.size();
        long finalizadas = actividades.stream().filter(Actividad::finalizado).count();
        int porcentaje = totalActividades == 0 ? 0 : (int) ((finalizadas * 100.0) / totalActividades);

        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(200, 20));
        panelActividadesTop.add(barraProgreso, BorderLayout.EAST);

        contenedorCentro.add(panelDetallesPlan);
        contenedorCentro.add(panelActividadesTop);
        contenedorCentro.add(scroll);

        add(contenedorCentro, BorderLayout.CENTER);

        for (Actividad a : actividades) {
            String accion;
            if (a.finalizado()) accion = "—";
            else if (!a.requiereInforme()) accion = "Finalizar";
            else accion = a.getIdInforme() > 0 ? "Ver Informe" : "Cargar Informe";

            model.addRow(new Object[]{
                a.getDescripcion(),
                a.finalizado() ? "Sí" : "No",
                accion
            });
        }

        tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
        tabla.getColumn("Acciones").setCellEditor(new BotonEditor(new JCheckBox(), actividades, gestorDeProyectos, model, barraProgreso, plan, tabla));

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
            if (value.toString().equals("—")) {
                dash.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                dash.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return dash;
            }

            button.setText(value.toString());
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
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
        private int currentRow;

        public BotonEditor(JCheckBox checkBox, List<Actividad> actividades, GestorDeProyectos gestor,
                          DefaultTableModel model, JProgressBar barra, PlanDeTrabajo plan, JTable tabla) {
            super(checkBox);
            this.actividades = actividades;
            this.gestor = gestor;
            this.model = model;
            this.barra = barra;
            this.plan = plan;
            this.tabla = tabla;
            this.button = new JButton();
            button.addActionListener(e -> accion());
        }

        private void accion() {
            Actividad act = actividades.get(currentRow);
            String accionActual = model.getValueAt(currentRow, 2).toString();

            if (accionActual.equals("Finalizar")) {
                int confirmacion = JOptionPane.showConfirmDialog(
                    button,
                    "¿Está seguro que desea finalizar esta actividad?",
                    "Confirmar finalización",
                    JOptionPane.YES_NO_OPTION
                );
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    try {
                        gestor.finalizarActividad(act.getIdActividad());
                        model.setValueAt("—", currentRow, 2);
                        model.setValueAt("Sí", currentRow, 1);
                        
                        // Actualizar barra de progreso
                        long finalizadas = actividades.stream().filter(Actividad::finalizado).count();
                        int porcentaje = (int) ((finalizadas * 100.0) / actividades.size());
                        barra.setValue(porcentaje);
                        
                        JOptionPane.showMessageDialog(button, "Actividad finalizada correctamente.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(button, "Error al finalizar la actividad: " + ex.getMessage());
                    }
                }
            } else if (accionActual.equals("Cargar Informe")) {
                Proyectos proyectos = new Proyectos(gestor);
                new VentanaCargarInforme(proyectos, act, v -> {
                    // Actualizar la tabla después de cargar el informe
                    model.setValueAt("Ver Informe", currentRow, 2);
                }).setVisible(true);
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

            if (act.finalizado()) {
                return dash;
            }

            button.setText(value.toString());
            button.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            button.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}




