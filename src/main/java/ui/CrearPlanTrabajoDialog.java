package ui;

import com.toedter.calendar.JDateChooser;
import model.Actividad;
import model.GestorDeProyectos;
import model.PlanDeTrabajo;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CrearPlanTrabajoDialog extends JDialog {
    private final GestorDeProyectos gestor;
    private PlanDeTrabajo planCreado;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtDescripcion;
    private JTextArea txtRecursos;
    private JCheckBox chkInforme;
    private JDateChooser dcFechaActividad;
    private JSpinner spHoras;
    private JDateChooser dcInicioPlan;
    private JDateChooser dcFinPlan;

    public CrearPlanTrabajoDialog(Frame owner, GestorDeProyectos gestor) {
        super(owner, "Crear Plan de Trabajo", true);
        this.gestor = gestor;
        init();
    }

    private void init() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout());

        Color fondo = new Color(0xBFBFBF);
        getContentPane().setBackground(fondo);

        /* ---------- Panel de Fechas del Plan ---------- */
        JPanel panelFechasPlan = new JPanel(new GridLayout(1, 4, 5, 5));
        TitledBorder bordePlan = BorderFactory.createTitledBorder("Fechas del Plan");
        bordePlan.setTitleColor(new Color(3, 1, 1));
        panelFechasPlan.setBorder(bordePlan);
        panelFechasPlan.setBackground(fondo);

        dcInicioPlan = new JDateChooser();
        dcInicioPlan.setDateFormatString("yyyy-MM-dd");
        dcFinPlan = new JDateChooser();
        dcFinPlan.setDateFormatString("yyyy-MM-dd");

        panelFechasPlan.add(new JLabel("Inicio del Plan:"));
        panelFechasPlan.add(dcInicioPlan);
        panelFechasPlan.add(new JLabel("Fin del Plan:"));
        panelFechasPlan.add(dcFinPlan);

        /* ---------- Recursos ---------- */
        txtRecursos = new JTextArea(3, 20);
        txtRecursos.setLineWrap(true);
        txtRecursos.setWrapStyleWord(true);
        JScrollPane scrollRecursos = new JScrollPane(txtRecursos);
        scrollRecursos.getViewport().setBackground(Color.WHITE);
        JPanel panelRecursos = new JPanel(new BorderLayout());
        panelRecursos.setBorder(BorderFactory.createTitledBorder("Recursos necesarios"));
        panelRecursos.setBackground(fondo);
        panelRecursos.add(scrollRecursos, BorderLayout.CENTER);

        /* ---------- Datos de Actividad ---------- */
        JPanel panelActividad = new JPanel(new GridLayout(5, 2, 5, 5));
        TitledBorder bordeActividad = BorderFactory.createTitledBorder("Nueva Actividad");
        bordeActividad.setTitleColor(new Color(3, 1, 1));
        panelActividad.setBorder(bordeActividad);
        panelActividad.setBackground(fondo);

        txtDescripcion = new JTextField();
        dcFechaActividad = new JDateChooser();
        dcFechaActividad.setDateFormatString("yyyy-MM-dd");
        spHoras = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        chkInforme = new JCheckBox("¿Requiere informe?");
        chkInforme.setBackground(fondo);

        panelActividad.add(new JLabel("Descripción:"));
        panelActividad.add(txtDescripcion);
        panelActividad.add(new JLabel("Fecha de Inicio:"));
        panelActividad.add(dcFechaActividad);
        panelActividad.add(new JLabel("Horas estimadas:"));
        panelActividad.add(spHoras);
        panelActividad.add(new JLabel(""));
        panelActividad.add(chkInforme);

        JButton btnAgregarAct = new JButton("Agregar Actividad");
        panelActividad.add(btnAgregarAct);

        /* ---------- Tabla de Actividades ---------- */
        modelo = new DefaultTableModel(new String[]{"Descripción", "Fecha", "Horas", "Informe"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(fondo);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(0x333333), 2));

        /* ---------- Botones inferiores ---------- */
        JButton btnGuardar = new JButton("Aceptar Plan");
        JButton btnCancelar = new JButton("Cancelar");

        btnAgregarAct.addActionListener(e -> agregarActividad());
        btnGuardar.addActionListener(e -> aceptar());
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(fondo);
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        /* ---------- Panel superior ---------- */
        JPanel sup = new JPanel(new BorderLayout());
        sup.setBackground(fondo);
        sup.add(panelFechasPlan, BorderLayout.NORTH);
        sup.add(panelRecursos, BorderLayout.CENTER);
        sup.add(panelActividad, BorderLayout.SOUTH);

        add(sup, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void agregarActividad() {
        String d = txtDescripcion.getText().trim();
        java.util.Date fecha = dcFechaActividad.getDate();
        int horas = (int) spHoras.getValue();
        boolean reqInf = chkInforme.isSelected();

        if (d.isBlank() || fecha == null) {
            JOptionPane.showMessageDialog(this, "Complete descripción y fecha.");
            return;
        }

        LocalDate f = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        modelo.addRow(new Object[]{d, f.format(DateTimeFormatter.ISO_DATE), horas, reqInf ? "Sí" : "No"});
        txtDescripcion.setText("");
        dcFechaActividad.setDate(null);
        spHoras.setValue(1);
        chkInforme.setSelected(false);
    }

    private void aceptar() {
        try {
            List<Actividad> acts = new ArrayList<>();
            for (int i = 0; i < modelo.getRowCount(); i++) {
                String desc = (String) modelo.getValueAt(i, 0);
                LocalDate fecha = LocalDate.parse((String) modelo.getValueAt(i, 1));
                int horas = Integer.parseInt(modelo.getValueAt(i, 2).toString());
                boolean reqInf = modelo.getValueAt(i, 3).equals("Sí");
                acts.add(new Actividad(desc, fecha, horas, false, reqInf));
            }

            LocalDate iniPlan = dcInicioPlan.getDate() == null ? null :
                    dcInicioPlan.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate finPlan = dcFinPlan.getDate() == null ? null :
                    dcFinPlan.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            planCreado = new PlanDeTrabajo(0, iniPlan, finPlan, acts, txtRecursos.getText());
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al crear el plan: " + ex.getMessage());
        }
    }

    public PlanDeTrabajo getPlanCreado() {
        return planCreado;
    }
}
