package ui;

import model.Informe;
import model.Proyectos;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class VerInformeTutor extends JFrame {
    private final Informe informe;
    private final Proyectos proyectos;
    private JTextField txtValoracion;

    public VerInformeTutor(Proyectos proyectos, Informe informe) {
        this.proyectos = proyectos;
        this.informe = informe;

        setTitle("Detalles del Informe");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de información del informe
        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 5, 5)); // Eliminamos la fila del ID
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

        // Panel de valoración - solo se muestra si el informe no tiene valoración
        if (informe.valoracionInforme() == -1) {
            JPanel panelValoracion = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelValoracion.setBorder(BorderFactory.createTitledBorder("Valorar Informe"));

            JLabel lblValoracion = new JLabel("Ingrese valoración (0-10):");
            txtValoracion = new JTextField(5);
            JButton btnGuardarValoracion = new JButton("Guardar Valoración");

            btnGuardarValoracion.addActionListener(e -> guardarValoracion());

            panelValoracion.add(lblValoracion);
            panelValoracion.add(txtValoracion);
            panelValoracion.add(btnGuardarValoracion);

            add(panelValoracion, BorderLayout.CENTER);
        }

        // Panel de botones
        JPanel panelBotones = new JPanel();
        JButton btnVerArchivo = new JButton("Ver Archivo");
        JButton btnDescargarArchivo = new JButton("Descargar Archivo");

        btnVerArchivo.addActionListener(e -> verArchivo());
        btnDescargarArchivo.addActionListener(e -> descargarArchivo());

        panelBotones.add(btnVerArchivo);
        panelBotones.add(btnDescargarArchivo);

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void guardarValoracion() {
        try {
            int valoracion = Integer.parseInt(txtValoracion.getText().trim());
            if (valoracion < 0 || valoracion > 10) {
                JOptionPane.showMessageDialog(this, "La valoración debe estar entre 0 y 10",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                proyectos.valorarInforme(informe.id(), valoracion);
                JOptionPane.showMessageDialog(this, "Valoración guardada correctamente");
                dispose(); // Cierra la ventana después de guardar
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la valoración: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor ingrese un número válido",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verArchivo() {
        byte[] archivo = informe.archivoEntregable();

        if (archivo == null || archivo.length == 0) {
            JOptionPane.showMessageDialog(this, "No hay archivo adjunto.");
            return;
        }

        try {
            // Validar que el archivo parece ser un PDF (opcional pero útil)
            if (!(archivo[0] == '%' && archivo[1] == 'P' && archivo[2] == 'D' && archivo[3] == 'F')) {
                JOptionPane.showMessageDialog(this, "El archivo no parece ser un PDF válido.");
                return;
            }

            // Crear archivo temporal
            File tempFile = File.createTempFile("informe_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(archivo);
            }

            // Asegurar que el archivo tenga extensión .pdf y sea ejecutable
            if (!Desktop.isDesktopSupported()) {
                JOptionPane.showMessageDialog(this, "El visor de escritorio no es compatible.");
                return;
            }

            Desktop desktop = Desktop.getDesktop();
            if (tempFile.exists()) {
                desktop.open(tempFile);
            } else {
                JOptionPane.showMessageDialog(this, "El archivo no se pudo crear correctamente.");
            }

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
        // En vez de usar el ID, generamos un nombre genérico o con la fecha
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
