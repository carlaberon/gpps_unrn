package ui;

import model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VerProyectoInformes extends JFrame implements InformeValoradoListener {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;

    public VerProyectoInformes(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Informes del Proyecto");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color fondo = Color.decode("#BFBFBF");
        getContentPane().setBackground(fondo);
        setLayout(new BorderLayout());

        Proyecto proyecto = gestorDeProyectos.obtenerProyecto(idProyecto);
        PlanDeTrabajo plan = gestorDeProyectos.obtenerPlan(idProyecto);
        List<Actividad> actividades = plan.actividades();

        // Panel proyecto
        JPanel panelProyecto = new JPanel(new GridLayout(5, 2, 5, 5));
        panelProyecto.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Información del Proyecto", TitledBorder.LEFT, TitledBorder.TOP));
        panelProyecto.setBackground(fondo);
        addLabelConEstilo(panelProyecto, "Nombre:", proyecto.getNombre());
        addLabelConEstilo(panelProyecto, "Descripción:", proyecto.getDescripcion());
        addLabelConEstilo(panelProyecto, "Área de Interés:", proyecto.getAreaDeInteres());
        addLabelConEstilo(panelProyecto, "Ubicación:", proyecto.getUbicacion());
        addLabelConEstilo(panelProyecto, "Estado:", proyecto.estadoProyecto());

        add(panelProyecto, BorderLayout.NORTH);

        // Panel detalles plan
        JPanel panelDetallesPlan = new JPanel(new GridLayout(4, 2, 5, 5));
        panelDetallesPlan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Detalles del Plan", TitledBorder.LEFT, TitledBorder.TOP));
        panelDetallesPlan.setBackground(fondo);
        int totalHoras = plan.cantHoras();
        addLabelConEstilo(panelDetallesPlan, "Fecha de Inicio:", plan.fechaInicio().toString());
        addLabelConEstilo(panelDetallesPlan, "Fecha de Fin:", plan.fechaFin().toString());
        addLabelConEstilo(panelDetallesPlan, "Recursos:", plan.recursos());
        addLabelConEstilo(panelDetallesPlan, "Total de Horas:", String.valueOf(totalHoras));

        // Panel actividades top
        JPanel panelActividadesTop = new JPanel(new BorderLayout());
        panelActividadesTop.setBackground(fondo);
        panelActividadesTop.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblActividades = new JLabel("Actividades:");
        lblActividades.setFont(lblActividades.getFont().deriveFont(Font.BOLD, 14f));
        panelActividadesTop.add(lblActividades, BorderLayout.WEST);

        int porcentaje = plan.porcentajeDeFinalizado();

        JProgressBar barraProgreso = new JProgressBar(0, 100);
        barraProgreso.setValue(porcentaje);
        barraProgreso.setStringPainted(true);
        barraProgreso.setPreferredSize(new Dimension(200, 14));

        JPanel panelProgresoFinal = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelProgresoFinal.setBackground(fondo);
        panelProgresoFinal.add(barraProgreso);

        if (porcentaje == 100 && gestorDeProyectos.existeInformeFinal(idProyecto)) {
            JButton btnVerInformeFinal = new JButton("Ver Informe Final");
            btnVerInformeFinal.addActionListener(e -> {
                Informe informe = gestorDeProyectos.obtenerInformeFinal(idProyecto);
                Proyectos proyectos = new Proyectos(gestorDeProyectos);
                new VerInformeTutor(proyectos, informe, VerProyectoInformes.this).setVisible(true);
            });
            panelProgresoFinal.add(btnVerInformeFinal);
        }

        panelActividadesTop.add(panelProgresoFinal, BorderLayout.EAST);

        // Tabla de actividades
        String[] columnas = {"ID", "Descripción", "Finalizado", "Ver Informe"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3 && "Ver Informe".equals(getValueAt(row, column));
            }
        };

        for (Actividad actividad : actividades) {
            String celdaInforme;
            if (!actividad.requiereInforme()) {
                celdaInforme = "Esta actividad no requiere informe";
            } else if (actividad.getIdInforme() > 0) {
                celdaInforme = "Ver Informe";
            } else {
                celdaInforme = "Aún no se cargó un informe";
            }

            modelo.addRow(new Object[]{
                    actividad.getIdActividad(),
                    actividad.descripcion(),
                    actividad.finalizado() ? "Sí" : "No",
                    celdaInforme
            });
        }

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(40);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(new Color(184, 207, 229));
        tabla.setBackground(new Color(0xE0E0E0));

        tabla.getTableHeader().setBackground(new Color(64, 64, 64));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        ((DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEFT);

        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);

        tabla.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabla.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), actividades, gestorDeProyectos));

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        // Centro
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBackground(fondo);
        centro.add(panelDetallesPlan);
        centro.add(panelActividadesTop);
        centro.add(scroll);

        add(centro, BorderLayout.CENTER);

        setVisible(true);
    }

    private void addLabelConEstilo(JPanel panel, String titulo, String valor) {
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD));
        panel.add(lblTitulo);

        JLabel lblValor = new JLabel(valor);
        panel.add(lblValor);
    }

    @Override
    public void informeValorado() {
        dispose(); // Cierra esta ventana
        new VerProyectoInformes(gestorDeProyectos, idProyecto); // La vuelve a abrir actualizada
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
                label.setHorizontalAlignment(SwingConstants.CENTER);
                return label;
            }
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private final JButton button;
        private final List<Actividad> actividades;
        private final GestorDeProyectos gestorDeProyectos;
        private int currentRow;

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
                        new VerInformeTutor(proyectos, informe, VerProyectoInformes.this).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(button, "No se encontró el informe asociado.");
                    }
                } else {
                    JOptionPane.showMessageDialog(button, "Esta actividad no tiene informe.");
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

