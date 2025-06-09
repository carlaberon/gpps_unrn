package ui;

import model.Actividad;
import model.GestorDeProyectos;
import model.PlanDeTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CrearPlanTrabajo extends JFrame {
    private GestorDeProyectos gestorDeProyectos;
    private int idProyecto;
    private JTable tablaActividades;
    private DefaultTableModel modeloTabla;
    private JTextField campoDescripcion;
    private JTextArea campoRecursos;
    private JCheckBox checkRequiereInforme;
    private JSpinner campoFechaInicio;
    private JSpinner campoHoras;
    private JSpinner campoFechaInicioPlan;
    private JSpinner campoFechaFinPlan;

    public CrearPlanTrabajo(GestorDeProyectos gestor, int idProyecto) {
        this.gestorDeProyectos = gestor;
        this.idProyecto = idProyecto;

        setTitle("Crear Plan de Trabajo");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Colores
        Color fondo = Color.decode("#A5E6BA");
        Color panelColor = Color.decode("#394032");
        Color textoColor = Color.decode("#000000");

        // Panel de fechas del plan
        JPanel panelFechasPlan = new JPanel(new GridLayout(1, 4, 5, 5));
        panelFechasPlan.setBorder(BorderFactory.createTitledBorder("Fechas del Plan"));
        panelFechasPlan.setBackground(panelColor);

        campoFechaInicioPlan = new JSpinner(new SpinnerDateModel());
        campoFechaInicioPlan.setEditor(new JSpinner.DateEditor(campoFechaInicioPlan, "yyyy-MM-dd"));

        campoFechaFinPlan = new JSpinner(new SpinnerDateModel());
        campoFechaFinPlan.setEditor(new JSpinner.DateEditor(campoFechaFinPlan, "yyyy-MM-dd"));

        panelFechasPlan.add(new JLabel("Inicio del Plan:"));
        panelFechasPlan.add(campoFechaInicioPlan);
        panelFechasPlan.add(new JLabel("Fin del Plan:"));
        panelFechasPlan.add(campoFechaFinPlan);

        // Panel de entrada de actividad
        JPanel panelActividad = new JPanel(new GridLayout(7, 2, 5, 5));
        panelActividad.setBorder(BorderFactory.createTitledBorder("Nueva Actividad"));
        panelActividad.setBackground(panelColor);

        campoDescripcion = new JTextField();
        campoFechaInicio = new JSpinner(new SpinnerDateModel());
        campoFechaInicio.setEditor(new JSpinner.DateEditor(campoFechaInicio, "yyyy-MM-dd"));
        campoHoras = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        checkRequiereInforme = new JCheckBox("¿Requiere informe?");
        checkRequiereInforme.setBackground(panelColor);
        checkRequiereInforme.setForeground(textoColor);

        panelActividad.add(new JLabel("Descripción:"));
        panelActividad.add(campoDescripcion);
        panelActividad.add(new JLabel("Fecha de Inicio:"));
        panelActividad.add(campoFechaInicio);
        panelActividad.add(new JLabel("Horas estimadas:"));
        panelActividad.add(campoHoras);
        panelActividad.add(new JLabel(""));
        panelActividad.add(checkRequiereInforme);

        JButton btnAgregarActividad = new JButton("Agregar Actividad");
        btnAgregarActividad.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 3));

        panelActividad.add(btnAgregarActividad);

        // Tabla de actividades
        modeloTabla = new DefaultTableModel(new String[]{"Descripción", "Fecha", "Horas", "Informe"}, 0);
        tablaActividades = new JTable(modeloTabla);

// Estilo para encabezado
        JTableHeader header = tablaActividades.getTableHeader();
        tablaActividades.setRowHeight(30); // por ejemplo, 30px
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK)); // grosor de línea derecha
                label.setBackground(Color.decode("#FFE3E3"));
                label.setForeground(Color.decode("#000000"));
                label.setFont(label.getFont().deriveFont(Font.BOLD));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setOpaque(true);
                return label;
            }
        });

// Estilo para las celdas para simular líneas verticales más gruesas
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.BLACK)); // grosor de línea derecha
                return label;
            }
        };

// Aplicar a todas las columnas
        for (int i = 0; i < tablaActividades.getColumnCount(); i++) {
            tablaActividades.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }


        JScrollPane scrollTabla = new JScrollPane(tablaActividades);

        // Botón guardar
        JButton btnGuardar = new JButton("Postular plan de trabajo");
        btnGuardar.setBorder(BorderFactory.createLineBorder(Color.decode("#000000"), 3));


        btnAgregarActividad.addActionListener(e -> agregarActividad());
        btnGuardar.addActionListener(e -> guardarPlan());

        JPanel panelSuperior = new JPanel(new BorderLayout());

        JPanel panelRecursos = new JPanel(new BorderLayout());
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos necesarios"));
        panelRecursos.setBackground(panelColor);
        campoRecursos = new JTextArea(3, 20);
        campoRecursos.setLineWrap(true);
        campoRecursos.setWrapStyleWord(true);
        panelRecursos.add(new JScrollPane(campoRecursos), BorderLayout.CENTER);

        JPanel panelDatosPlan = new JPanel(new BorderLayout());
        panelDatosPlan.add(panelFechasPlan, BorderLayout.NORTH);
        panelDatosPlan.add(panelRecursos, BorderLayout.CENTER);

        panelSuperior.add(panelDatosPlan, BorderLayout.NORTH);
        panelSuperior.add(panelActividad, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        // Aplicar estilo visual
        applyTheme(this.getContentPane(), fondo, textoColor);
        setVisible(true);
    }

    private void agregarActividad() {
        String descripcion = campoDescripcion.getText();
        java.util.Date fecha = (java.util.Date) campoFechaInicio.getValue();
        int horas = (int) campoHoras.getValue();
        boolean requiereInforme = checkRequiereInforme.isSelected();

        if (descripcion.isBlank()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        modeloTabla.addRow(new Object[]{
                descripcion, fechaLocal.format(formatter), horas, requiereInforme ? "Sí" : "No"
        });

        campoDescripcion.setText("");
        campoHoras.setValue(1);
        checkRequiereInforme.setSelected(false);
    }

    private void guardarPlan() {
        List<Actividad> actividades = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String descripcion = (String) modeloTabla.getValueAt(i, 0);
            LocalDate fecha = LocalDate.parse(modeloTabla.getValueAt(i, 1).toString());
            int horas = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());
            boolean requiereInforme = modeloTabla.getValueAt(i, 3).equals("Sí");

            actividades.add(new Actividad(descripcion, fecha, horas, false, requiereInforme));
        }

        java.util.Date fechaInicioUtil = (java.util.Date) campoFechaInicioPlan.getValue();
        java.util.Date fechaFinUtil = (java.util.Date) campoFechaFinPlan.getValue();
        LocalDate fechaInicio = fechaInicioUtil.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFin = fechaFinUtil.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        String recursos = campoRecursos.getText();
        try {
            PlanDeTrabajo plan = new PlanDeTrabajo(idProyecto, fechaInicio, fechaFin, actividades, recursos);
            gestorDeProyectos.cargarPlanDeTrabajo(plan, idProyecto);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        JOptionPane.showMessageDialog(this, "Plan de trabajo postulado correctamente.");
        dispose();
    }

    private void applyTheme(Container container, Color fondo, Color texto) {
        for (Component comp : container.getComponents()) {
            comp.setBackground(fondo);
            comp.setForeground(texto);
            styleComponent(comp, texto);
            if (comp instanceof Container) {
                applyTheme((Container) comp, fondo, texto);
            }
        }
    }

    private void styleComponent(Component c, Color textoColor) {
        if (c instanceof JLabel label) {
            label.setForeground(textoColor);
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        } else if (c instanceof JTextArea ta) {
            ta.setForeground(textoColor);
            ta.setBackground(Color.decode("#FFE3E3"));
            ta.setFont(ta.getFont().deriveFont(Font.BOLD));
        } else if (c instanceof JTextField tf) {
            tf.setForeground(textoColor);
            tf.setBackground(Color.decode("#FFE3E3"));
            tf.setFont(tf.getFont().deriveFont(Font.BOLD));
        } else if (c instanceof JSpinner sp) {
            Component editor = sp.getEditor();
            if (editor instanceof JSpinner.DefaultEditor defEditor) {
                defEditor.getTextField().setForeground(textoColor);
                defEditor.getTextField().setBackground(Color.decode("#587B7F"));
                defEditor.getTextField().setFont(defEditor.getTextField().getFont().deriveFont(Font.BOLD));
            }
        } else if (c instanceof JCheckBox cb) {
            cb.setFont(cb.getFont().deriveFont(Font.BOLD));
        }
    }
}
