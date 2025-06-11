package ui;

import model.Informe;
import model.Proyectos;

import javax.swing.*;
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
        setLayout(new BorderLayout());

        // Panel de información del informe
        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 5, 5)); // Eliminamos fila del ID
        panelInfo.setBorder(BorderFactory.createTitledBorder("Información del Informe"));

        panelInfo.add(new JLabel("Descripción:"));
        panelInfo.add(new JLabel(informe.descripcion()));

        panelInfo.add(new JLabel("Fecha de Entrega:"));
        panelInfo.add(new JLabel(informe.fechaEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

        panelInfo.add(new JLabel("Tipo:"));
        panelInfo.add(new JLabel(informe.tipo()));

        panelInfo.add(new JLabel("Valoración:"));
        panelInfo.add(new JLabel(informe.valoracionInforme() == -1 ? "No valorado" : String.valueOf(informe.valoracionInforme())));

        add(panelInfo, BorderLayout.NORTH);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnVerArchivo = new JButton("Ver Archivo");
        JButton btnDescargarArchivo = new JButton("Descargar Archivo");

        btnVerArchivo.addActionListener(e -> verArchivo());
        btnDescargarArchivo.addActionListener(e -> descargarArchivo());

        panelBotones.add(btnVerArchivo);
        panelBotones.add(btnDescargarArchivo);

        add(panelBotones, BorderLayout.CENTER);

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
        // Se elimina el ID del nombre del archivo
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
