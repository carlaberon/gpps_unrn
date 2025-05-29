package front;
import model.Administrador;
import model.Convenio;
import model.ConvenioDAO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class CargarConvenio extends JFrame {

    private final ConvenioDAO convenioDAO;

    public CargarConvenio(ConvenioDAO convenioDAO) {
        this.convenioDAO = convenioDAO;
        initUI();
    }

    private void initUI() {
        setTitle("Carga de Convenio Firmado");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JButton botonSeleccionar = new JButton("Seleccionar y Cargar PDF");

        botonSeleccionar.addActionListener(e -> seleccionarYActualizarPDF());

        panel.add(botonSeleccionar);
        add(panel);
        setVisible(true);
    }

    private void seleccionarYActualizarPDF() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF", "pdf"));
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                byte[] pdfBytes = Files.readAllBytes(archivo.toPath());

                Convenio convenio = convenioDAO.buscarPorId(3);
                Administrador admin = new Administrador(10, "admin", "123", "Administrador", "admin@email.com", null);
                admin.cargarConvenioFirmado(convenio, pdfBytes, convenioDAO);

                JOptionPane.showMessageDialog(null, "Convenio actualizado correctamente.");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error al leer el archivo.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
            }
        }
    }
}