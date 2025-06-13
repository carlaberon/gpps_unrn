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
    private InformeValoradoListener listener;

    public VerInformeTutor(Proyectos proyectos, Informe informe, InformeValoradoListener listener) {
        this.proyectos = proyectos;
        this.informe = informe;
        this.listener = listener;

        setTitle("Detalles del Informe");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color fondo = Color.decode("#BFBFBF");
        Font fuenteNegrita = new Font("SansSerif", Font.BOLD, 12);
        Font fuenteTitulo = new Font("SansSerif", Font.BOLD, 16);

        // Panel de información del informe
        JPanel panelInfo = new JPanel(new GridLayout(4, 2, 5, 5));
        panelInfo.setBackground(fondo);

        JPanel panelInfoConTitulo = new JPanel(new BorderLayout());
        panelInfoConTitulo.setBackground(fondo);
        panelInfoConTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Información del Informe");
        lblTitulo.setFont(fuenteTitulo);
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        panelInfoConTitulo.add(lblTitulo, BorderLayout.NORTH);
        panelInfoConTitulo.add(panelInfo, BorderLayout.CENTER);

        panelInfo.add(crearLabel("Descripción:", fuenteNegrita));
        panelInfo.add(crearLabel(informe.descripcion(), fuenteNegrita));

        panelInfo.add(crearLabel("Fecha de Entrega:", fuenteNegrita));
        panelInfo.add(crearLabel(informe.fechaEntrega().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), fuenteNegrita));

        panelInfo.add(crearLabel("Tipo:", fuenteNegrita));
        panelInfo.add(crearLabel(informe.tipo(), fuenteNegrita));

        panelInfo.add(crearLabel("Valoración:", fuenteNegrita));
        panelInfo.add(crearLabel(informe.valoracionInforme() == -1 ? "No valorado" : String.valueOf(informe.valoracionInforme()), fuenteNegrita));

        add(panelInfoConTitulo, BorderLayout.NORTH);

        // Panel de valoración
        if (informe.valoracionInforme() == -1) {
            JPanel panelValoracion = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panelValoracion.setBackground(fondo);
            panelValoracion.setBorder(BorderFactory.createTitledBorder("Valorar Informe"));

            JLabel lblValoracion = new JLabel("Ingrese valoración (0-10):");
            lblValoracion.setFont(fuenteNegrita);
            lblValoracion.setForeground(Color.BLACK);

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
        panelBotones.setBackground(fondo);
        JButton btnVerArchivo = new JButton("Ver Archivo");
        JButton btnDescargarArchivo = new JButton("Descargar Archivo");

        btnVerArchivo.addActionListener(e -> verArchivo());
        btnDescargarArchivo.addActionListener(e -> descargarArchivo());

        panelBotones.add(btnVerArchivo);
        panelBotones.add(btnDescargarArchivo);

        add(panelBotones, BorderLayout.SOUTH);

        getContentPane().setBackground(fondo);
        setVisible(true);
    }

    private JLabel crearLabel(String texto, Font fuente) {
        JLabel label = new JLabel(texto);
        label.setFont(fuente);
        label.setForeground(Color.BLACK);
        return label;
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
                if (listener != null) {
                    listener.informeValorado();
                }
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
            if (!(archivo[0] == '%' && archivo[1] == 'P' && archivo[2] == 'D' && archivo[3] == 'F')) {
                JOptionPane.showMessageDialog(this, "El archivo no parece ser un PDF válido.");
                return;
            }

            File tempFile = File.createTempFile("informe_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(archivo);
            }

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
