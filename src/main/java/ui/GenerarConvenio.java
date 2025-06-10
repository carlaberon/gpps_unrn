package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import model.*;

public class GenerarConvenio extends JFrame {

    private JComboBox<EntidadColaboradora> comboEntidades;
    private JComboBox<Proyecto> comboProyectos;
    private JTextArea txtDescripcion;
    private JSpinner spinnerFechaInicio;
    private JSpinner spinnerFechaFin;
    private JButton btnGenerar;

    private final Administrador admin;
    private final GestorDeConvenios gestorDeConvenios;

    public GenerarConvenio(Administrador admin, GestorDeConvenios gestorDeConvenios) {
        this.admin = admin;
        this.gestorDeConvenios = gestorDeConvenios;

        setTitle("Generar Convenio");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        comboEntidades = new JComboBox<>();
        comboProyectos = new JComboBox<>();
        txtDescripcion = new JTextArea(4, 20);
        SpinnerDateModel modelInicio = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerFechaInicio = new JSpinner(modelInicio);
        spinnerFechaInicio.setEditor(new JSpinner.DateEditor(spinnerFechaInicio, "dd/MM/yyyy"));

        SpinnerDateModel modelFin = new SpinnerDateModel(new Date(), null, null, java.util.Calendar.DAY_OF_MONTH);
        spinnerFechaFin = new JSpinner(modelFin);
        spinnerFechaFin.setEditor(new JSpinner.DateEditor(spinnerFechaFin, "dd/MM/yyyy"));

        btnGenerar = new JButton("Generar Convenio");

        cargarEntidades();
        cargarProyectos();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        panel.add(new JLabel("Entidad colaboradora:"));
        panel.add(comboEntidades);

        panel.add(new JLabel("Proyecto:"));
        panel.add(comboProyectos);

        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(txtDescripcion));

        panel.add(new JLabel("Fecha de inicio:"));
        panel.add(spinnerFechaInicio);

        panel.add(new JLabel("Fecha de fin:"));
        panel.add(spinnerFechaFin);

        panel.add(new JLabel());
        panel.add(btnGenerar);

        add(panel);

        btnGenerar.addActionListener(this::generarConvenio);
        
    }

    private void cargarEntidades() {
        try {
            List<EntidadColaboradora> entidades = gestorDeConvenios.obtenerTodas();
            for (EntidadColaboradora e : entidades) {
                comboEntidades.addItem(e);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar entidades: " + e.getMessage());
        }
    }

    private void cargarProyectos() {
        try {
            List<Proyecto> proyectos = gestorDeConvenios.obtenerProyectosConEstudiante();
            for (Proyecto p : proyectos) {
                comboProyectos.addItem(p);
            }
        } catch (SQLException e) {
            mostrarError("Error al cargar proyectos: " + e.getMessage());
        }
    }
    
    private void generarConvenio(ActionEvent event) {
        try {
            EntidadColaboradora entidad = (EntidadColaboradora) comboEntidades.getSelectedItem();
            Proyecto proyecto = (Proyecto) comboProyectos.getSelectedItem();
            String descripcion = txtDescripcion.getText().trim();

            LocalDate fechaInicio = convertirFecha((Date) spinnerFechaInicio.getValue());
            LocalDate fechaFin = convertirFecha((Date) spinnerFechaFin.getValue());

            admin.generarConvenio(entidad.getId(), proyecto.getId(), descripcion, fechaInicio, fechaFin, gestorDeConvenios);
            JOptionPane.showMessageDialog(this, "Convenio generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (Exception ex) {
            mostrarError("Error al generar convenio: " + ex.getMessage());
        }
    }

    private LocalDate convertirFecha(Date date) {
        return (date != null) ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}


