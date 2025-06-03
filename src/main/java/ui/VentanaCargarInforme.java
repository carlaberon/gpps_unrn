package ui;
import database.ServicioDePersistenciaGestionProyectos;
import model.Proyectos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;

public class VentanaCargarInforme extends JFrame {

    private JTextField txtIdInforme;
    private JTextField txtDescripcion;
    private JTextField txtTipo;
    private JLabel lblArchivo;
    private File archivoSeleccionado;

    private Proyectos proyectos;

    public VentanaCargarInforme(Proyectos proyectos) {
        this.proyectos = proyectos;
        setTitle("Cargar Informe Parcial");
        setSize(450, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID Informe:"), gbc);

        gbc.gridx = 1;
        txtIdInforme = new JTextField();
        panel.add(txtIdInforme, gbc);

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        txtDescripcion = new JTextField();
        panel.add(txtDescripcion, gbc);

        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        txtTipo = new JTextField();
        panel.add(txtTipo, gbc);

        // Archivo
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Archivo PDF:"), gbc);

        gbc.gridx = 1;
        JButton btnSeleccionar = new JButton("Seleccionar archivo");
        panel.add(btnSeleccionar, gbc);

        // Nombre del archivo
        gbc.gridx = 1;
        gbc.gridy = 4;
        lblArchivo = new JLabel("Ningún archivo seleccionado");
        panel.add(lblArchivo, gbc);

        // Botón Cargar
        gbc.gridx = 1;
        gbc.gridy = 5;
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
            int id = Integer.parseInt(txtIdInforme.getText());
            String descripcion = txtDescripcion.getText();
            String tipo = txtTipo.getText();

            byte[] archivoBytes = archivoSeleccionado != null ? Files.readAllBytes(archivoSeleccionado.toPath()) : null;

            proyectos.cargarInforme(id, descripcion, tipo, archivoBytes);

            JOptionPane.showMessageDialog(this, "Informe cargado correctamente.");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número entero.");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el informe: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicioDePersistenciaGestionProyectos servicio = new ServicioDePersistenciaGestionProyectos();
            Proyectos proyectos = new Proyectos(servicio); // Reemplaza por tu instancia real
            new VentanaCargarInforme(proyectos).setVisible(true);
        });
    }
}
