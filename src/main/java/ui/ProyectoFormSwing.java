package ui;

//import database.*;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProyectoFormSwing extends JFrame {
    private JTextField nombreField;
    private JTextArea descripcionField;
    private JTextField areaField;
    private JTextField ubicacionField;
    private JComboBox<Tutor> tutorCombo;
    private JComboBox<Tutor> supervisorCombo;
    private JCheckBox estadoCheck;
    private JButton guardarButton;
    private JButton limpiarButton;

    private GestorDeProyectos proyectoDAOPersistencia;
    private GestorDeUsuarios tutorDAOPersistencia;

    private Proyectos proyectoDAO;
    private Usuarios tutorDAO;

    public ProyectoFormSwing(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos) {
        this.proyectoDAOPersistencia = gestorDeProyectos;
        this.tutorDAOPersistencia = gestorDeUsuarios;
        initializeComponents();
        setupLayout();
        loadData();
        setupListeners();
    }

    private void initializeComponents() {
        setTitle("Formulario de Proyecto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);

        nombreField = new JTextField(20);
        descripcionField = new JTextArea(4, 20);
        descripcionField.setLineWrap(true);
        areaField = new JTextField(20);
        ubicacionField = new JTextField(20);
        tutorCombo = new JComboBox<>();
        supervisorCombo = new JComboBox<>();
        estadoCheck = new JCheckBox("Activo");
        guardarButton = new JButton("Guardar");
        limpiarButton = new JButton("Limpiar");

        try {
            proyectoDAO = new Proyectos(proyectoDAOPersistencia);
            tutorDAO = new Usuarios(tutorDAOPersistencia);
        } catch (Exception e) {
            mostrarAlerta("Error de conexión", e.getMessage());
        }
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(nombreField, gbc);

        // Descripción
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(new JScrollPane(descripcionField), gbc);

        // Área
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Área:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(areaField, gbc);

        //Ubicación
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Úbicación:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(ubicacionField, gbc);

        // Tutor Interno
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Tutor Interno:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(tutorCombo, gbc);

        // Tutor Externo
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Tutor Externo:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(supervisorCombo, gbc);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(estadoCheck, gbc);

        // Botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(guardarButton);
        buttonPanel.add(limpiarButton);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void loadData() {
        cargarTutoresInternos();
        cargarTutoresExternos();
    }

    private void cargarTutoresInternos() {
        List<Tutor> tutores = tutorDAO.obtenerTutores();
        DefaultComboBoxModel<Tutor> model = new DefaultComboBoxModel<>();

        for (Tutor t : tutores) {
            if ("Interno".equalsIgnoreCase(t.getTipo().trim())) {
                model.addElement(t);
            }
        }

        tutorCombo.setModel(model);
        tutorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tutor) {
                    Tutor t = (Tutor) value;
                    setText(t.getNombre() + " (" + t.getTipo() + ")");
                }
                return this;
            }
        });
    }

    private void cargarTutoresExternos() {
        List<Tutor> tutores = tutorDAO.obtenerTutores();
        DefaultComboBoxModel<Tutor> model = new DefaultComboBoxModel<>();

        for (Tutor t : tutores) {
            if ("Externo".equalsIgnoreCase(t.getTipo())) {
                model.addElement(t);
            }
        }

        supervisorCombo.setModel(model);
        supervisorCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tutor) {
                    Tutor t = (Tutor) value;
                    setText(t.getNombre() + " (" + t.getTipo() + ")");
                }
                return this;
            }
        });
    }

    private void setupListeners() {
        guardarButton.addActionListener(e -> guardarProyecto());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    private void guardarProyecto() {
        if (!validarCampos()) {
            return;
        }

        try {
            Proyecto proyecto = new Proyecto(
                    0,
                    nombreField.getText(),
                    descripcionField.getText(),
                    estadoCheck.isSelected(),
                    areaField.getText(),
                    (Tutor) tutorCombo.getSelectedItem(),
                    (Tutor) supervisorCombo.getSelectedItem(),
                    ubicacionField.getText()
            );

            if (proyecto.esValido()) {
                proyectoDAO.guardarProyectoSinEstudiante(proyecto);
                mostrarAlerta("Éxito", "Proyecto guardado correctamente");
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "Los datos del proyecto no son válidos");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al guardar el proyecto: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        StringBuilder errores = new StringBuilder();

        if (nombreField.getText().isEmpty()) {
            errores.append("- El nombre es obligatorio\n");
        }
        if (descripcionField.getText().isEmpty()) {
            errores.append("- La descripción es obligatoria\n");
        }
        if (areaField.getText().isEmpty()) {
            errores.append("- El área de interés es obligatoria\n");
        }
        if (ubicacionField.getText().isEmpty()) {
            errores.append("- La ubicación es obligatoria\n");
        }
        if (tutorCombo.getSelectedItem() == null) {
            errores.append("- Debe seleccionar un tutor interno\n");
        }
        if (supervisorCombo.getSelectedItem() == null) {
            errores.append("- Debe seleccionar un tutor externo\n");
        }

        if (errores.length() > 0) {
            mostrarAlerta("Error de validación", errores.toString());
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void limpiarCampos() {
        nombreField.setText("");
        descripcionField.setText("");
        areaField.setText("");
        ubicacionField.setText("");
        tutorCombo.setSelectedItem(null);
        supervisorCombo.setSelectedItem(null);
        estadoCheck.setSelected(false);
    }
} 