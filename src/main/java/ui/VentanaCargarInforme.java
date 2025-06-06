package ui;

import database.ServicioDePersistenciaGestionProyectos;
import model.Actividad;
import model.Informe;
import model.Proyectos;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

public class VentanaCargarInforme extends JFrame {

    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private JTextField txtDescripcion;
    private JTextField txtTipo;
    private JLabel lblArchivo;
    private File archivoSeleccionado;
    private Proyectos proyectos;
    private Actividad actividad;

    public VentanaCargarInforme(Proyectos proyectos, Actividad actividad) {
        this.proyectos = proyectos;
        this.actividad = actividad;
        setTitle("Cargar Informe Parcial");
        setSize(450, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicioDePersistenciaGestionProyectos servicio = new ServicioDePersistenciaGestionProyectos();
            Proyectos proyectos = new Proyectos(servicio);
            new VentanaCargarInforme(proyectos, null).setVisible(true);
        });
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        txtDescripcion = new JTextField();
        panel.add(txtDescripcion, gbc);

        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        txtTipo = new JTextField();
        panel.add(txtTipo, gbc);

        // Archivo
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Archivo PDF:"), gbc);

        gbc.gridx = 1;
        JButton btnSeleccionar = new JButton("Seleccionar archivo");
        panel.add(btnSeleccionar, gbc);

        // Nombre del archivo
        gbc.gridx = 1;
        gbc.gridy = 3;
        lblArchivo = new JLabel("Ningún archivo seleccionado");
        panel.add(lblArchivo, gbc);

        // Botón Cargar
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnCargar = new JButton("Cargar Informe");
        panel.add(btnCargar, gbc);

        // Eventos
        btnSeleccionar.addActionListener(e -> seleccionarArchivo());
        btnCargar.addActionListener(e -> cargarInforme());

        add(panel);
    }

    private void seleccionarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            archivoSeleccionado = fileChooser.getSelectedFile();
            lblArchivo.setText(archivoSeleccionado.getName());
        }
    }

    private void cargarInforme() {
        try {
            String descripcion = txtDescripcion.getText();
            String tipo = txtTipo.getText();
            int idInforme = idCounter.getAndIncrement();

            byte[] archivoBytes = archivoSeleccionado != null ? Files.readAllBytes(archivoSeleccionado.toPath()) : null;

            // Crear el informe con el ID de la actividad
            Informe informe = new Informe(actividad.getIdActividad(), descripcion, tipo, archivoBytes);
            proyectos.cargarInforme(informe);

            JOptionPane.showMessageDialog(this, "Informe cargado correctamente.");
            dispose(); // Cerrar la ventana después de cargar exitosamente

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el informe: " + ex.getMessage());
        }
    }
}
