package ui;

import com.toedter.calendar.JDateChooser;
import model.Administrador;
import model.EntidadColaboradora;
import model.GestorDeConvenios;
import model.Proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class GenerarConvenio extends JFrame {

    private final Administrador admin;
    private final GestorDeConvenios gestorDeConvenios;
    private JComboBox<EntidadColaboradora> comboEntidades;
    private JComboBox<Proyecto> comboProyectos;
    private JTextArea txtDescripcion;
    private JDateChooser fechaInicioChooser;
    private JDateChooser fechaFinChooser;
    private JButton btnGenerar;

    public GenerarConvenio(Administrador admin, GestorDeConvenios gestorDeConvenios) {
        this.admin = admin;
        this.gestorDeConvenios = gestorDeConvenios;

        setTitle("Generar Convenio");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Color fondo = Color.decode("#BFBFBF");
        getContentPane().setBackground(fondo);

        comboEntidades = new JComboBox<>();
        comboProyectos = new JComboBox<>();

        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtDescripcion.setBackground(Color.WHITE);

        fechaInicioChooser = new JDateChooser();
        fechaInicioChooser.setDateFormatString("dd/MM/yyyy");

        fechaFinChooser = new JDateChooser();
        fechaFinChooser.setDateFormatString("dd/MM/yyyy");

        btnGenerar = new JButton("Generar Convenio");

        cargarEntidades();
        cargarProyectos();

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.setBackground(fondo);

        JLabel lblEntidades = new JLabel("Entidad colaboradora:");
        JLabel lblProyectos = new JLabel("Proyecto:");
        JLabel lblDescripcion = new JLabel("Descripción:");
        JLabel lblInicio = new JLabel("Fecha de inicio:");
        JLabel lblFin = new JLabel("Fecha de fin:");

        JScrollPane scrollDescripcion = new JScrollPane(txtDescripcion);
        scrollDescripcion.getViewport().setBackground(Color.WHITE);
        scrollDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        panel.add(lblEntidades);
        panel.add(comboEntidades);

        panel.add(lblProyectos);
        panel.add(comboProyectos);

        panel.add(lblDescripcion);
        panel.add(scrollDescripcion);

        panel.add(lblInicio);
        panel.add(fechaInicioChooser);

        panel.add(lblFin);
        panel.add(fechaFinChooser);

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

            Date fechaInicioDate = fechaInicioChooser.getDate();
            Date fechaFinDate = fechaFinChooser.getDate();

            if (fechaInicioDate == null || fechaFinDate == null) {
                mostrarError("Debe seleccionar ambas fechas.");
                return;
            }

            LocalDate fechaInicio = convertirFecha(fechaInicioDate);
            LocalDate fechaFin = convertirFecha(fechaFinDate);

            admin.generarConvenio(entidad.getId(), proyecto.getId(), descripcion, fechaInicio, fechaFin, gestorDeConvenios);
            JOptionPane.showMessageDialog(this, "Convenio generado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
        } catch (Exception ex) {
            mostrarError("Error al generar convenio: " + ex.getMessage());
        }
    }

    private LocalDate convertirFecha(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
