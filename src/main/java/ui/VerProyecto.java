package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerProyecto extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;

    public VerProyecto(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Detalles del Proyecto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        List<Actividad> actividades = plan.actividades();

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

        String[] columnas = {"Descripción", "Finalizado", "Acciones"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(50);
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
        add(scroll, BorderLayout.CENTER);

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
            modeloTabla.addRow(new Object[]{
                    a.getDescripcion(),
                    a.finalizado() ? "Sí" : "No",
                    a.requiereInforme() ? "Cargar Informe" : "Esta actividad no requiere informe"
            });
        }

        tabla.getColumn("Acciones").setCellRenderer(new ButtonRenderer(actividades));
        tabla.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), actividades, gestorDeProyectos));

        setVisible(true);
    }

    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private List<Actividad> actividades;

        public ButtonRenderer(List<Actividad> actividades) {
            this.actividades = actividades;
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Actividad act = actividades.get(row);
            if (act.requiereInforme()) {
                setText("Cargar Informe");
                return this;
            } else {
                return new JLabel("Esta actividad no requiere informe");
            }
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private List<Actividad> actividades;
        private int currentRow;
        private GestorDeProyectos gestorDeProyectos;

        public ButtonEditor(JCheckBox checkBox, List<Actividad> actividades, GestorDeProyectos gestorDeProyectos) {
            super(checkBox);
            this.actividades = actividades;
            this.gestorDeProyectos = gestorDeProyectos;
            this.button = new JButton("Cargar Informe");
            this.button.addActionListener(e -> {
                Actividad act = actividades.get(currentRow);
                if (act.requiereInforme()) {
                    Proyectos proyectos = new Proyectos(gestorDeProyectos);
                    VentanaCargarInforme ventana = new VentanaCargarInforme(proyectos, act);
                    ventana.setVisible(true);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.currentRow = row;
            Actividad act = actividades.get(row);
            return act.requiereInforme() ? button : new JLabel("Esta actividad no requiere informe");
        }

        @Override
        public Object getCellEditorValue() {
            return "Cargar Informe";
        }
    }
}
