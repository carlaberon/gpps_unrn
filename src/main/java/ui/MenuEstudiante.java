package ui;

import model.GestorDeProyectos;
import model.GestorDeUsuarios;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MenuEstudiante extends JFrame {

    private final GestorDeUsuarios gestorDeUsuarios;
    private final GestorDeProyectos gestorDeProyectos;
    private final int idEstudiante;

    public MenuEstudiante(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos, int idEstudiante) {
        this.gestorDeUsuarios = gestorDeUsuarios;
        this.gestorDeProyectos = gestorDeProyectos;
        this.idEstudiante = idEstudiante;


        initUI();
    }

    private void initUI() {
        setTitle("Menú Estudiante");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.decode("#BFBFBF"));
            }
        };
        panel.setLayout(new GridBagLayout());

        JButton btnSeleccionar = new JButton("Seleccionar Proyecto");
        JButton btnPostular = new JButton("Postular Proyecto");

        btnSeleccionar.setPreferredSize(new Dimension(200, 40));
        btnPostular.setPreferredSize(new Dimension(200, 40));

        // Acción: Seleccionar Proyecto
        btnSeleccionar.addActionListener((ActionEvent e) -> {
            new SeleccionarProyecto(gestorDeProyectos, idEstudiante).setVisible(true);
        });

        // Acción: Postular Proyecto
        btnPostular.addActionListener((ActionEvent e) -> {
            Integer idProyecto = null;
            idProyecto = gestorDeUsuarios.obtenerIdProyectoEstudiante(idEstudiante);

            if (idProyecto == null)
                new CrearProyecto(gestorDeUsuarios, gestorDeProyectos, true, idEstudiante).setVisible(true);
            else {
                JOptionPane.showMessageDialog(this, "Ya has elegido o postulado un proyecto!", "Aviso", JOptionPane.WARNING_MESSAGE);
                dispose();
            }

        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        gbc.gridy = 0;
        panel.add(btnSeleccionar, gbc);

        gbc.gridy = 1;
        panel.add(btnPostular, gbc);

        setContentPane(panel);
    }
}

