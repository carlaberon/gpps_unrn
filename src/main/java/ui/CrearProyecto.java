package ui;

import model.*;

import javax.swing.*;
import java.awt.*;

public class CrearProyecto extends JFrame {
    /* ---------- DAOs / servicios ---------- */
    private final GestorDeProyectos gestorProyectos;
    private final GestorDeUsuarios gestorUsuarios;

    /* ---------- campos de la UI ---------- */
    private JTextField nombreField;
    private JTextArea descripcionField;
    private JTextField areaField;
    private JTextField ubicacionField;
    private JComboBox<Tutor> tutorCombo;
    private JComboBox<Tutor> supervisorCombo;
    private JButton btnCrearPlan;
    private JButton guardarButton;
    private JButton limpiarButton;
    private JLabel lblPlanCreado;
    private Proyectos proyectoDAO;
    private Usuarios tutorDAO;

    /* ---------- datos en memoria ---------- */
    private PlanDeTrabajo planCreado;

    public CrearProyecto(GestorDeUsuarios gUsuarios, GestorDeProyectos gProyectos) {
        this.gestorProyectos = gProyectos;
        this.gestorUsuarios = gUsuarios;
        initComponents();
        setupLayout();
        loadData();
        setupListeners();
    }

    /* ---------------- init ---------------- */
    private void initComponents() {
        setTitle("Formulario de Proyecto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 520);
        setLocationRelativeTo(null);

        nombreField = new JTextField(20);
        descripcionField = new JTextArea(4, 20);
        descripcionField.setLineWrap(true);
        areaField = new JTextField(20);
        ubicacionField = new JTextField(20);
        tutorCombo = new JComboBox<>();
        supervisorCombo = new JComboBox<>();
        btnCrearPlan = new JButton("Crear Plan");
        guardarButton = new JButton("Guardar");
        limpiarButton = new JButton("Limpiar");
        lblPlanCreado = new JLabel("(sin plan)");

        try {
            proyectoDAO = new Proyectos(gestorProyectos);
            tutorDAO = new Usuarios(gestorUsuarios);
        } catch (Exception e) {
            mostrarAlerta("Error de conexión", e.getMessage());
        }
    }

    /* ---------------- UI ---------------- */
    private void setupLayout() {
        // Fondo general
        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(Color.decode("#BFBFBF")); // ← Fondo gris claro
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;
        addLine(main, gbc, y++, "Nombre:", nombreField);
        addLine(main, gbc, y++, "Descripción:", new JScrollPane(descripcionField));
        addLine(main, gbc, y++, "Área:", areaField);
        addLine(main, gbc, y++, "Ubicación:", ubicacionField);
        addLine(main, gbc, y++, "Tutor Interno:", tutorCombo);
        addLine(main, gbc, y++, "Tutor Externo:", supervisorCombo);

        // Panel para plan de trabajo
        JPanel planPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        planPanel.setBackground(Color.decode("#BFBFBF"));
        planPanel.add(lblPlanCreado);
        planPanel.add(Box.createHorizontalStrut(10));
        planPanel.add(btnCrearPlan);
        addLine(main, gbc, y++, "Plan:", planPanel);

        // Panel de botones inferior
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.decode("#BFBFBF"));
        btnPanel.add(guardarButton);
        btnPanel.add(limpiarButton);
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        main.add(btnPanel, gbc);

        // Título personalizado
        JLabel titulo = new JLabel("Crear Proyecto", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 24)); // más grande
        titulo.setForeground(new Color(3, 1, 1));
        titulo.setOpaque(true);
        titulo.setBackground(Color.decode("#BFBFBF"));
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // más separado del borde

        // Layout principal
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.decode("#BFBFBF"));
        getContentPane().add(titulo, BorderLayout.NORTH);
        getContentPane().add(main, BorderLayout.CENTER);
    }

    private void addLine(JPanel p, GridBagConstraints gbc, int y, String label, Component c) {
        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        p.add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        p.add(c, gbc);
        gbc.weightx = 0;
    }

    /* ---------------- data ---------------- */
    private void loadData() {
        cargarTutores(tutorCombo, "interno");
        cargarTutores(supervisorCombo, "externo");
    }

    private void cargarTutores(JComboBox<Tutor> combo, String tipo) {
        DefaultComboBoxModel<Tutor> model = new DefaultComboBoxModel<>();
        for (Tutor t : tutorDAO.obtenerTutores()) {
            if (tipo.equalsIgnoreCase(t.getTipo().trim())) model.addElement(t);
        }
        combo.setModel(model);
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object v, int i, boolean s, boolean f) {
                super.getListCellRendererComponent(list, v, i, s, f);
                if (v instanceof Tutor t) setText(t.nombre() + " (" + t.getTipo() + ")");
                return this;
            }
        });
    }

    /* ---------------- listeners ---------------- */
    private void setupListeners() {
        btnCrearPlan.addActionListener(e -> abrirDialogoPlan());
        guardarButton.addActionListener(e -> guardarProyecto());
        limpiarButton.addActionListener(e -> limpiarCampos());
    }

    private void abrirDialogoPlan() {
        CrearPlanTrabajoDialog dlg = new CrearPlanTrabajoDialog(this, gestorProyectos);
        dlg.setVisible(true);
        planCreado = dlg.getPlanCreado();

        if (planCreado != null) {
            lblPlanCreado.setText("Plan creado ✔");
        } else {
            lblPlanCreado.setText("(sin plan)");
        }
    }

    /* ---------------- guardar ---------------- */
    private void guardarProyecto() {
        if (!validarCampos()) return;
        if (planCreado == null) {
            mostrarAlerta("Falta el Plan", "Debe crear el Plan de Trabajo antes de guardar.");
            return;
        }

        try {
            Proyecto proyecto = new Proyecto(
                    0,
                    nombreField.getText(),
                    descripcionField.getText(),
                    false,
                    areaField.getText(),
                    (Tutor) supervisorCombo.getSelectedItem(),
                    (Tutor) tutorCombo.getSelectedItem(),
                    ubicacionField.getText()
            );

            if (!proyecto.esValido()) {
                mostrarAlerta("Error", "Los datos del proyecto no son válidos.");
                return;
            }

            int idGenerado = proyectoDAO.guardarProyectoSinEstudiante(proyecto, planCreado);
            mostrarAlerta("Éxito", "Proyecto creado con éxito.");
            dispose();

        } catch (Exception ex) {
            mostrarAlerta("Error", "Error al guardar: " + ex.getMessage());
        }
    }

    /* ---------------- utilidades ---------------- */
    private boolean validarCampos() {
        StringBuilder err = new StringBuilder();
        if (nombreField.getText().isBlank()) err.append("- Falta Nombre\n");
        if (descripcionField.getText().isBlank()) err.append("- Falta Descripción\n");
        if (areaField.getText().isBlank()) err.append("- Falta Área\n");
        if (ubicacionField.getText().isBlank()) err.append("- Falta Ubicación\n");
        if (tutorCombo.getSelectedItem() == null) err.append("- Seleccione Tutor Interno\n");
        if (supervisorCombo.getSelectedItem() == null) err.append("- Seleccione Tutor Externo\n");

        if (err.length() > 0) {
            mostrarAlerta("Errores de validación", err.toString());
            return false;
        }
        return true;
    }

    private void limpiarCampos() {
        nombreField.setText("");
        descripcionField.setText("");
        areaField.setText("");
        ubicacionField.setText("");
        tutorCombo.setSelectedItem(null);
        supervisorCombo.setSelectedItem(null);
        planCreado = null;
        lblPlanCreado.setText("(sin plan)");
    }

    private void mostrarAlerta(String t, String m) {
        JOptionPane.showMessageDialog(this, m, t, JOptionPane.INFORMATION_MESSAGE);
    }
}
