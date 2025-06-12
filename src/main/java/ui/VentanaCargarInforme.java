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
import java.util.function.Consumer;

public class VentanaCargarInforme extends JFrame {

    private static final AtomicInteger idCounter = new AtomicInteger(1);
    private final String tipoInforme;
    private JTextField txtDescripcion;
    private JLabel lblArchivo;
    private File archivoSeleccionado;
    private Proyectos proyectos;
    private Actividad actividad;
    private Consumer<Void> onInformeCargado;

    public VentanaCargarInforme(Proyectos proyectos, Actividad actividad, Consumer<Void> onInformeCargado, String tipoInforme) {
        this.proyectos = proyectos;
        this.actividad = actividad;
        this.onInformeCargado = onInformeCargado;
        this.tipoInforme = tipoInforme;

        setTitle("Cargar Informe " + tipoInforme);
        setSize(450, 250);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServicioDePersistenciaGestionProyectos servicio = new ServicioDePersistenciaGestionProyectos();
            Proyectos proyectos = new Proyectos(servicio);
            new VentanaCargarInforme(proyectos, null, null, "PARCIAL").setVisible(true);
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

        // Archivo
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Archivo PDF:"), gbc);

        gbc.gridx = 1;
        JButton btnSeleccionar = new JButton("Seleccionar archivo");
        panel.add(btnSeleccionar, gbc);

        // Nombre del archivo
        gbc.gridx = 1;
        gbc.gridy = 2;
        lblArchivo = new JLabel("Ningún archivo seleccionado");
        panel.add(lblArchivo, gbc);

        // Botón Cargar
        gbc.gridx = 1;
        gbc.gridy = 3;
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
            int idInforme = idCounter.getAndIncrement();

            byte[] archivoBytes = archivoSeleccionado != null ? Files.readAllBytes(archivoSeleccionado.toPath()) : null;

            // Crear el informe con el ID de la actividad y tipo
            Informe informe = new Informe(
                    actividad != null ? actividad.getIdActividad() : -1,
                    descripcion,
                    tipoInforme,
                    archivoBytes
            );
            proyectos.cargarInforme(informe);

            JOptionPane.showMessageDialog(this, "Informe cargado correctamente.");

            // Notify parent window that informe was loaded
            if (onInformeCargado != null) {
                onInformeCargado.accept(null);
            }

            dispose(); // Cerrar la ventana después de cargar exitosamente

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al leer el archivo.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar el informe: " + ex.getMessage());
        }
    }
}
