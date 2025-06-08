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
        tabla.setRowHeight(25);

        // Ocultar columna de ID
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        // Configurar el renderer para la columna "Ver Informe"
        tabla.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), actividades, gestorDeProyectos));

        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        setVisible(true);
    }

    // Clase para renderizar el botón en la tabla
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

    // Clase para manejar el evento del botón
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
