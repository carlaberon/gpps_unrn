package ui;

import model.Actividad;
import model.GestorDeProyectos;
import model.PlanDeTrabajo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        // Panel de fechas del plan
        JPanel panelFechasPlan = new JPanel(new GridLayout(1, 4, 5, 5));
        panelFechasPlan.setBorder(BorderFactory.createTitledBorder("Fechas del Plan"));

        campoFechaInicioPlan = new JSpinner(new SpinnerDateModel());
        campoFechaInicioPlan.setEditor(new JSpinner.DateEditor(campoFechaInicioPlan, "yyyy-MM-dd"));

        campoFechaFinPlan = new JSpinner(new SpinnerDateModel());
        campoFechaFinPlan.setEditor(new JSpinner.DateEditor(campoFechaFinPlan, "yyyy-MM-dd"));

        panelFechasPlan.add(new JLabel("Inicio del Plan:"));
        panelFechasPlan.add(campoFechaInicioPlan);
        panelFechasPlan.add(new JLabel("Fin del Plan:"));
        panelFechasPlan.add(campoFechaFinPlan);

        // Panel de entrada de actividad
        JPanel panelActividad = new JPanel(new GridLayout(6, 2, 5, 5));
        panelActividad.setBorder(BorderFactory.createTitledBorder("Nueva Actividad"));

        campoDescripcion = new JTextField();
        campoFechaInicio = new JSpinner(new SpinnerDateModel());
        campoFechaInicio.setEditor(new JSpinner.DateEditor(campoFechaInicio, "yyyy-MM-dd"));
        campoHoras = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1)); // más pequeño

        panelActividad.add(new JLabel("Descripción:"));
        panelActividad.add(campoDescripcion);
        panelActividad.add(new JLabel("Fecha de Inicio:"));
        panelActividad.add(campoFechaInicio);
        panelActividad.add(new JLabel("Horas estimadas:"));
        panelActividad.add(campoHoras);

        JButton btnAgregarActividad = new JButton("Agregar Actividad");
        panelActividad.add(btnAgregarActividad);

        // Tabla de actividades
        modeloTabla = new DefaultTableModel(new String[]{"Descripción", "Fecha", "Horas"}, 0);
        tablaActividades = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaActividades);

        // Botón guardar
        JButton btnGuardar = new JButton("Postular plan de trabajo");

        btnAgregarActividad.addActionListener(e -> agregarActividad());
        btnGuardar.addActionListener(e -> guardarPlan());

        JPanel panelSuperior = new JPanel(new BorderLayout());
        // Panel para recursos
        JPanel panelRecursos = new JPanel(new BorderLayout());
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos necesarios"));
        campoRecursos = new JTextArea(3, 20);
        campoRecursos.setLineWrap(true);
        campoRecursos.setWrapStyleWord(true);
        panelRecursos.add(new JScrollPane(campoRecursos), BorderLayout.CENTER);

// Panel combinado fechas + recursos
        JPanel panelDatosPlan = new JPanel(new BorderLayout());
        panelDatosPlan.add(panelFechasPlan, BorderLayout.NORTH);
        panelDatosPlan.add(panelRecursos, BorderLayout.CENTER);

// Agregar al panel superior
        panelSuperior.add(panelDatosPlan, BorderLayout.NORTH);
        panelSuperior.add(panelActividad, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);
    }

    private void agregarActividad() {
        String descripcion = campoDescripcion.getText();
        java.util.Date fecha = (java.util.Date) campoFechaInicio.getValue();
        int horas = (int) campoHoras.getValue();

        if (descripcion.isBlank()) {
            JOptionPane.showMessageDialog(this, "La descripción no puede estar vacía.");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        modeloTabla.addRow(new Object[]{
                descripcion, fechaLocal.format(formatter), horas
        });

        // Limpiar campos
        campoDescripcion.setText("");
        campoHoras.setValue(1);
    }

    private void guardarPlan() {
        List<Actividad> actividades = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String descripcion = (String) modeloTabla.getValueAt(i, 0);
            LocalDate fecha = LocalDate.parse(modeloTabla.getValueAt(i, 1).toString());
            int horas = Integer.parseInt(modeloTabla.getValueAt(i, 2).toString());

            actividades.add(new Actividad(descripcion, fecha, horas, false));
        }

        // Obtener fechas del plan desde los campos de fecha
        java.util.Date fechaInicioUtil = (java.util.Date) campoFechaInicioPlan.getValue();
        java.util.Date fechaFinUtil = (java.util.Date) campoFechaFinPlan.getValue();
        LocalDate fechaInicio = fechaInicioUtil.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaFin = fechaFinUtil.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

        // Crear plan con fechas del plan
        String recursos = campoRecursos.getText();
        PlanDeTrabajo plan = new PlanDeTrabajo(idProyecto, fechaInicio, fechaFin, actividades, recursos);
        gestorDeProyectos.cargarPlanDeTrabajo(plan, idProyecto);

        JOptionPane.showMessageDialog(this, "Plan de trabajo postulado correctamente.");
        dispose();
    }
}



