package ui;

import model.Informe;
import model.Proyectos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class VerInformeEstudiante extends JFrame {
    private final Informe informe;
    private final Proyectos proyectos;

    public VerInformeEstudiante(Proyectos proyectos, Informe informe) {
        this.proyectos = proyectos;
        this.informe = informe;

        setTitle("Detalles del Informe");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color fondo = Color.decode("#BFBFBF");
        getContentPane().setBackground(fondo);
        setLayout(new BorderLayout());

        // Panel para el título separado
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(fondo);
        panelTitulo.setBorder(new EmptyBorder(20, 10, 10, 10)); // margen superior y algo abajo

        JLabel lblTitulo = new JLabel("Información del Informe");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(Color.BLACK);

        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel con las características (labels)
        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 8, 8));
        panelInfo.setBackground(fondo);
        panelInfo.setBorder(new EmptyBorder(10, 30, 20, 30)); // margen interno para separar de los bordes
        panelInfo.setPreferredSize(new Dimension(500, 150));

        Font labelTituloFont = new Font("Segoe UI", Font.BOLD, 13);
        Font labelValorFont = new Font("Segoe UI", Font.PLAIN, 13);

        JLabel lblDescripcion = new JLabel("Descripción:");
        lblDescripcion.setFont(labelTituloFont);
        lblDescripcion.setForeground(Color.BLACK);
        panelInfo.add(lblDescripcion);

        JLabel valDescripcion = new JLabel(informe.descripcion());
        valDescripcion.setFont(labelValorFont);
        valDescripcion.setForeground(Color.DARK_GRAY);
        panelInfo.add(valDescripcion);

        JLabel lblFecha = new JLabel("Fecha de Entrega:");
        lblFecha.setFont(labelTituloFont);
        lblFecha.setForeground(Color.BLACK);
        panelInfo.add(lblFecha);

        JLabel valFecha = new JLabel(informe.fechaEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        valFecha.setFont(labelValorFont);
        valFecha.setForeground(Color.DARK_GRAY);
        panelInfo.add(valFecha);

        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(labelTituloFont);
        lblTipo.setForeground(Color.BLACK);
        panelInfo.add(lblTipo);

        JLabel valTipo = new JLabel(informe.tipo());
        valTipo.setFont(labelValorFont);
        valTipo.setForeground(Color.DARK_GRAY);
        panelInfo.add(valTipo);

        JLabel lblValoracion = new JLabel("Valoración:");
        lblValoracion.setFont(labelTituloFont);
        lblValoracion.setForeground(Color.BLACK);
        panelInfo.add(lblValoracion);

        JLabel valValoracion = new JLabel(informe.valoracionInforme() == -1 ? "No valorado" : String.valueOf(informe.valoracionInforme()));
        valValoracion.setFont(labelValorFont);
        valValoracion.setForeground(Color.DARK_GRAY);
        panelInfo.add(valValoracion);

        // Contenedor para centrar panelInfo
        JPanel contenedorInfo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contenedorInfo.setBackground(fondo);
        contenedorInfo.add(panelInfo);

        add(contenedorInfo, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(fondo);

        JButton btnVerArchivo = new JButton("Ver Archivo");
        JButton btnDescargarArchivo = new JButton("Descargar Archivo");

        btnVerArchivo.addActionListener(e -> verArchivo());
        btnDescargarArchivo.addActionListener(e -> descargarArchivo());

        panelBotones.add(btnVerArchivo);
        panelBotones.add(btnDescargarArchivo);

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void verArchivo() {
        if (informe.archivoEntregable() == null) {
            JOptionPane.showMessageDialog(this, "No hay archivo adjunto.");
            return;
        }

        try {
            File tempFile = File.createTempFile("informe_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(informe.archivoEntregable());
            }

            Desktop.getDesktop().open(tempFile);
            tempFile.deleteOnExit();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al abrir el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void descargarArchivo() {
        if (informe.archivoEntregable() == null) {
            JOptionPane.showMessageDialog(this, "No hay archivo adjunto.");
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        String nombreBase = "informe_" + informe.fechaEntrega().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf";
        fileChooser.setSelectedFile(new File(nombreBase));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(informe.archivoEntregable());
                JOptionPane.showMessageDialog(this, "Archivo descargado correctamente.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al descargar el archivo: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}