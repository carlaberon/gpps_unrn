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

        // Tabla de actividades (sin columna "Nombre")
        String[] columnas = {"Descripción", "Finalizado", "Acciones"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Solo el botón es editable
            }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(100); // Aumentamos la altura de las filas para mostrar más texto
        tabla.getColumnModel().getColumn(0).setPreferredWidth(400); // Ancho para la descripción

        // Configurar el renderizador para la columna de descripción
        tabla.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JTextArea textArea = new JTextArea(value.toString());
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setRows(3);
                textArea.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                textArea.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
                return textArea;
            }
        });

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setPreferredSize(new Dimension(700, 300)); // Ajustamos el tamaño del scroll pane
        add(scroll, BorderLayout.CENTER);
        // Panel para Detalles del Plan
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

// Contenedor vertical para Detalles del Plan y la tabla
        JPanel contenedorCentro = new JPanel();
        contenedorCentro.setLayout(new BoxLayout(contenedorCentro, BoxLayout.Y_AXIS));

// Panel superior para título de actividades y progreso
        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

// Título "Actividades:"
        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

// Calcular porcentaje de actividades finalizadas
        int totalActividades = actividades.size();
        long finalizadas = actividades.stream().filter(Actividad::finalizado).count();
        int porcentaje = totalActividades == 0 ? 0 : (int) ((finalizadas * 100.0) / totalActividades);

// Barra de progreso
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
                    a.finalizado() ? "Sí" : "No",
                    "Cargar Informe"
            });
        }

        // Agregar renderizador y editor de botón
        tabla.getColumn("Acciones").setCellRenderer(new ButtonRenderer(actividades));
        tabla.getColumn("Acciones").setCellEditor(new ButtonEditor(new JCheckBox(), actividades, gestorDeProyectos));

        setVisible(true);
    }

    // Renderizador para el botón
    static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private List<Actividad> actividades;

        public ButtonRenderer(List<Actividad> actividades) {
            this.actividades = actividades;
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Actividad actividad = actividades.get(row);

            if (actividad.getIdInforme() > 0) {
                setText("Ver Informe");
            } else {
                setText("Cargar Informe");
            }

            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(table.getBackground());
            }

            return this;
        }
    }

    // Editor del botón
    static class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private List<Actividad> actividades;
        private int currentRow;
        private GestorDeProyectos gestorDeProyectos;

        public ButtonEditor(JCheckBox checkBox, List<Actividad> actividades, GestorDeProyectos gestorDeProyectos) {
            super(checkBox);
            this.actividades = actividades;
            this.gestorDeProyectos = gestorDeProyectos;
            this.button = new JButton();
            this.button.setOpaque(true);
            this.button.addActionListener(e -> {
                Actividad act = actividades.get(currentRow);
                Proyectos proyectos = new Proyectos(gestorDeProyectos);

                if (act.getIdInforme() > 0) {
                    // Si ya tiene informe, mostrar el informe
                    Informe informe = gestorDeProyectos.obtenerInforme(act.getIdInforme());
                    if (informe != null) {
                        new VerInformeEstudiante(proyectos, informe).setVisible(true);
                    }
                } else {
                    // Si no tiene informe, mostrar la ventana para cargar
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

            if (act.getIdInforme() > 0) {
                button.setText("Ver Informe");
            } else {
                button.setText("Cargar Informe");
            }

            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }
}
