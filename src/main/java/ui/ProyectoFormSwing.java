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
    private JComboBox<Estudiante> estudianteCombo;
    private JComboBox<Tutor> tutorCombo;
    private JComboBox<Tutor> supervisorCombo;
    private JCheckBox estadoCheck;
    private JButton guardarButton;
    private JButton limpiarButton;

    private GestorDeProyectos proyectoDAOPersistencia;
    private GestorDeUsuarios estudianteDAOPersistencia;
    private GestorDeUsuarios tutorDAOPersistencia;

    private Proyectos proyectoDAO;
    private Usuarios estudianteDAO;
    private Usuarios tutorDAO;

    public ProyectoFormSwing(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos) {
        this.proyectoDAOPersistencia = gestorDeProyectos;
        this.estudianteDAOPersistencia = gestorDeUsuarios;
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
        estudianteCombo = new JComboBox<>();
        tutorCombo = new JComboBox<>();
        supervisorCombo = new JComboBox<>();
        estadoCheck = new JCheckBox("Activo");
        guardarButton = new JButton("Guardar");
        limpiarButton = new JButton("Limpiar");

        try {
            proyectoDAO = new Proyectos(proyectoDAOPersistencia);
            estudianteDAO = new Usuarios(estudianteDAOPersistencia);
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

        // Estudiante
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(new JLabel("Estudiante:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(estudianteCombo, gbc);

        // Director
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(new JLabel("Tutor Interno:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(tutorCombo, gbc);

        // Supervisor
        gbc.gridx = 0;
        gbc.gridy = 6;
        mainPanel.add(new JLabel("Supervisor:"), gbc);
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
        cargarEstudiantes();
        cargarTutoresInternos();
        cargarTutoresExternos();
    }

    private void cargarEstudiantes() {
        List<Estudiante> estudiantes = estudianteDAO.obtenerEstudiantes();
        DefaultComboBoxModel<Estudiante> model = new DefaultComboBoxModel<>();
        for (Estudiante e : estudiantes) {
            model.addElement(e);
        }
        estudianteCombo.setModel(model);
        estudianteCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Estudiante) {
                    Estudiante e = (Estudiante) value;
                    setText(e.getNombre() + " (" + e.getLegajo() + ")");
                }
                return this;
            }
        });
    }

    private void cargarTutoresInternos() {
        List<Tutor> tutores = tutorDAO.obtenerTutores();
        DefaultComboBoxModel<Tutor> model = new DefaultComboBoxModel<>();

        for (Tutor t : tutores) {
            if ("Tutor Interno".equalsIgnoreCase(t.getTipo().trim())) {
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
            if ("Tutor Externo".equalsIgnoreCase(t.getTipo())) {
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
                    (Estudiante) estudianteCombo.getSelectedItem(),
                    (Tutor) tutorCombo.getSelectedItem(),
                    (Tutor) supervisorCombo.getSelectedItem()
            );

            if (proyecto.esValido()) {
                //proyectoDAO.guardar(proyecto);
                proyectoDAO.guardarProyecto(proyecto);
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
//        if (estudianteCombo.getSelectedItem() == null) {
//            errores.append("- Debe seleccionar un estudiante\n");
//        }
//        if (tutorCombo.getSelectedItem() == null) {
//            errores.append("- Debe seleccionar un tutor interno\n");
//        }
//        if (supervisorCombo.getSelectedItem() == null) {
//            errores.append("- Debe seleccionar un supervisor\n");
//        }

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
        estudianteCombo.setSelectedItem(null);
        tutorCombo.setSelectedItem(null);
        supervisorCombo.setSelectedItem(null);
        estadoCheck.setSelected(false);
    }
} 