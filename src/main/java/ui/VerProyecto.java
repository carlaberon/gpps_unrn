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
        tabla.setRowHeight(90);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(420);
        tabla.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        // Render multi-línea para descripción
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                                                           boolean sel, boolean foc, int r, int c) {
                JTextArea ta = new JTextArea(val.toString());
                ta.setLineWrap(true);
                ta.setWrapStyleWord(true);
                ta.setRows(3);
                ta.setBackground(sel ? t.getSelectionBackground() : t.getBackground());
                ta.setForeground(sel ? t.getSelectionForeground() : t.getForeground());
                return ta;
            }
        });

        /* --- Cargar filas según reglas --- */
        for (Actividad a : actividades) {
            String accion;
            if (a.finalizado()) accion = "—";
            else if (!a.requiereInforme()) accion = "Finalizar";
            else accion = "Cargar Informe";

            model.addRow(new Object[]{a.getDescripcion(),
                    a.finalizado() ? "Sí" : "No",
                    accion});
        }

        JScrollPane scroll = new JScrollPane(tabla);

        /* --- Panel Plan + progreso --- */
        JPanel pPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        pPlan.setBorder(BorderFactory.createTitledBorder("Detalles del Plan"));
        pPlan.add(new JLabel("Fecha de Inicio:"));
        pPlan.add(new JLabel(plan.fechaInicio().toString()));
        pPlan.add(new JLabel("Fecha de Fin:"));
        pPlan.add(new JLabel(plan.fechaFin().toString()));
        pPlan.add(new JLabel("Recursos:"));
        pPlan.add(new JLabel(plan.recursos()));
        pPlan.add(new JLabel("Total de Horas:"));
        pPlan.add(new JLabel(String.valueOf(plan.cantHoras())));

        JPanel topAct = new JPanel(new BorderLayout());
        topAct.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        JLabel lblA = new JLabel("Actividades:");
        lblA.setFont(lblA.getFont().deriveFont(Font.BOLD, 14f));
        topAct.add(lblA, BorderLayout.WEST);
        JProgressBar barra = new JProgressBar(0, 100);
        barra.setValue(plan.porcentajeDeFinalizado());
        barra.setStringPainted(true);
        barra.setPreferredSize(new Dimension(180, 20));
        topAct.add(barra, BorderLayout.EAST);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(pPlan);
        centro.add(topAct);
        centro.add(scroll);
        add(centro, BorderLayout.CENTER);

        /* --- Render y editor de botón --- */
        tabla.getColumn("Acciones").setCellRenderer(new BotonRenderer());
        tabla.getColumn("Acciones").setCellEditor(
                new BotonEditor(new JCheckBox(), actividades, gestorDeProyectos,
                        model, barra, plan, tabla));

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

            if ("—".equals(value)) {
                if (isSelected) {
                    dash.setOpaque(true);
                    dash.setBackground(table.getSelectionBackground());
                    dash.setForeground(table.getSelectionForeground());
                } else {
                    dash.setOpaque(false);
                }
                return dash;
            }
            button.setText(value.toString());
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
        private int currentRow;

        BotonEditor(JCheckBox cb, List<Actividad> acts, GestorDeProyectos gestor,
                    DefaultTableModel model, JProgressBar barra,
                    PlanDeTrabajo plan, JTable tabla) {
            super(cb);
            this.actividades = acts;
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

            if ("Finalizar".equals(accionActual)) {
                int ok = JOptionPane.showConfirmDialog(
                        SwingUtilities.getWindowAncestor(button),
                        "¿Seguro que desea marcar la actividad como finalizada?",
                        "Confirmar finalización",
                        JOptionPane.YES_NO_OPTION);

                if (ok == JOptionPane.YES_OPTION) {
                    gestor.finalizarActividad(act.getIdActividad()); // actualiza BD

                    model.setValueAt("Sí", currentRow, 1);   // Finalizado = Sí

                    /* --- parte crítica --- */
                    button.setText("—");                     // ahora el editor devolverá "—"

                    fireEditingStopped();                    // termina la edición
                }
                return;
            }

            if ("Cargar Informe".equals(accionActual)) {
                new VentanaCargarInforme(new Proyectos(gestor), act).setVisible(true);
            }
        }

        @Override
        public Component getTableCellEditorComponent(JTable t, Object v,
                                                     boolean s, int r, int c) {
            currentRow = r;
            button.setText(v.toString());
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();   // devolverá "—" tras el cambio
        }
    }
}




