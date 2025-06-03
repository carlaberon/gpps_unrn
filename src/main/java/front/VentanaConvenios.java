package front;

import model.Convenio;


import javax.swing.*;

import database.DataBaseConnectionException; // TENGOESTO X AHORA CUANDO CREEMOS UN PAQUETE DONDE ESTE LAS EXCEPTION YA NO TENDRE NADA DE DATABASE
import model.GestorDeConvenios;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class VentanaConvenios extends JFrame {

    private final JLabel lblIdEntidad;
    private final JLabel lblIdProyecto;
    private final JLabel lblDescripcion;
    private final JLabel lblFechaInicio;
    private final JLabel lblFechaFin;
    private final JButton btnVerConvenioFirmado;

    private final GestorDeConvenios gestorDeConvenios;

    public VentanaConvenios(GestorDeConvenios gestorDeConvenios) {
        this.gestorDeConvenios = gestorDeConvenios;

        setTitle("Detalle del Convenio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 1, 10, 10));

        lblIdEntidad = new JLabel();
        lblIdProyecto = new JLabel();
        lblDescripcion = new JLabel();
        lblFechaInicio = new JLabel();
        lblFechaFin = new JLabel();
        btnVerConvenioFirmado = new JButton("Ver convenio firmado");

        add(lblIdEntidad);
        add(lblIdProyecto);
        add(lblDescripcion);
        add(lblFechaInicio);
        add(lblFechaFin);
        add(btnVerConvenioFirmado);

        inicializarEventos();
    }

    public void cargarDatosConvenio(Convenio convenio) {
        lblIdEntidad.setText("ID de Entidad: " + convenio.getIdEntidad());
        lblIdProyecto.setText("ID de Proyecto: " + convenio.getIdProyecto());
        lblDescripcion.setText("Descripción: " + convenio.getDescripcion());
        lblFechaInicio.setText("Fecha de Inicio: " + convenio.getFechaInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFechaFin.setText("Fecha de Fin: " + convenio.getFechaFin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        btnVerConvenioFirmado.putClientProperty("convenioId", convenio.getId());
    }

    private void inicializarEventos() {
        btnVerConvenioFirmado.addActionListener((ActionEvent e) -> {
            try {
                int idConvenio = (int) btnVerConvenioFirmado.getClientProperty("convenioId");
                verPdfConvenioFirmado(idConvenio);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "No se pudo abrir el convenio firmado.\n" + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void verPdfConvenioFirmado(int idConvenio) throws DataBaseConnectionException {
        try {
            byte[] archivoPdf = gestorDeConvenios.obtenerArchivoPdfPorId(idConvenio);

            if (archivoPdf == null || archivoPdf.length == 0) {
                throw new IOException("El archivo firmado está vacío o no se encuentra.");
            }

            File tempFile = File.createTempFile("convenio_firmado_", ".pdf");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(archivoPdf);
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                throw new UnsupportedOperationException("Tu sistema no soporta la apertura de archivos PDF.");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error al intentar abrir el archivo PDF: " + ex.getMessage(), ex);
        }
    }
}
