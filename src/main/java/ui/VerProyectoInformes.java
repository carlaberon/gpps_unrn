package ui;

import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerProyectoInformes extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;

    public VerProyectoInformes(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Informes del Proyecto");
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

        // Detalles del plan
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

        // Subtítulo "Actividades:" y barra de progreso
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

        // Tabla de actividades con columna para ver informes
        String[] columnas = {"ID", "Descripción", "Estado", "Ver Informe"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 && "Ver Informe".equals(getValueAt(row, column));
            }
        };

        for (Actividad actividad : actividades) {
            boolean requiere = actividad.requiereInforme();
            modelo.addRow(new Object[]{
                    actividad.getIdActividad(),
                    actividad.descripcion(),
                    actividad.finalizado() ? "Finalizada" : "En progreso",
                    requiere ? "Ver Informe" : "Esta actividad no requiere informe"
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setRowHeight(60);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        tabla.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), actividades, gestorDeProyectos));

        JScrollPane scroll = new JScrollPane(tabla);

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.add(panelDetallesPlan);
        centro.add(panelActividadesTop);
        centro.add(scroll);

        add(centro, BorderLayout.CENTER);

        setVisible(true);
    }

    class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            if ("Ver Informe".equals(value)) {
                setText("Ver Informe");
                return this;
            } else {
                JLabel label = new JLabel(value.toString());
                label.setOpaque(true);
                return label;
            }
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private List<Actividad> actividades;
        private int currentRow;
        private GestorDeProyectos gestorDeProyectos;

        public ButtonEditor(JCheckBox checkBox, List<Actividad> actividades, GestorDeProyectos gestorDeProyectos) {
            super(checkBox);
            this.actividades = actividades;
            this.gestorDeProyectos = gestorDeProyectos;
            this.button = new JButton("Ver Informe");
            this.button.addActionListener(e -> {
                Actividad act = actividades.get(currentRow);
                if (act.getIdInforme() > 0) {
                    Informe informe = gestorDeProyectos.obtenerInforme(act.getIdInforme());
                    if (informe != null) {
                        Proyectos proyectos = new Proyectos(gestorDeProyectos);
                        new VerInforme(proyectos, informe).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró el informe asociado.");
                    }
                } else {
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
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Ver Informe";
        }
    }
}

