package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class VentanaLogin extends JFrame {

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JCheckBox chkVerContrasena;

    private GestorDeUsuarios gestorDeUsuarios;
    private GestorDeProyectos gestorDeProyectos;
    private GestorDeConvenios gestorDeConvenios;

    public VentanaLogin(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos, GestorDeConvenios gestorDeConvenios) {
        this.gestorDeUsuarios = gestorDeUsuarios;
        this.gestorDeProyectos = gestorDeProyectos;
        this.gestorDeConvenios = gestorDeConvenios;

        setTitle("Iniciar Sesión");
        setSize(400, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        chkVerContrasena = new JCheckBox("Ver contraseña");
        btnIniciarSesion = new JButton("Iniciar Sesión");
        chkVerContrasena.addActionListener(e -> {
            txtContrasena.setEchoChar(chkVerContrasena.isSelected() ? (char) 0 : '\u2022');
        });
        getRootPane().setDefaultButton(btnIniciarSesion);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblGPPS = new JLabel("GPPS");
        lblGPPS.setFont(new Font("SansSerif", Font.BOLD, 36));
        lblGPPS.setForeground(Color.RED);

        JLabel lblDescripcion = new JLabel("Gestor de Práctica Profesional Supervisada");
        lblDescripcion.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblDescripcion.setForeground(Color.BLACK);

        panelTitulo.add(lblGPPS);
        panelTitulo.add(lblDescripcion);

        JPanel panelFormulario = new JPanel(new GridLayout(4, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panelFormulario.add(new JLabel("Usuario:"));
        panelFormulario.add(txtUsuario);
        panelFormulario.add(new JLabel("Contraseña:"));
        panelFormulario.add(txtContrasena);
        panelFormulario.add(new JLabel());
        panelFormulario.add(chkVerContrasena);
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnIniciarSesion);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelFormulario, BorderLayout.CENTER);
    }

    private void setupListeners() {
        btnIniciarSesion.addActionListener((ActionEvent e) -> {
            String usuario = txtUsuario.getText().trim();
            String contrasena = new String(txtContrasena.getPassword()).trim();

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor complete todos los campos.", "Campos Vacíos", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                Usuario user = gestorDeUsuarios.buscarUsuario(usuario, contrasena);
                this.dispose();

                if (user instanceof Administrador admin) {
                    new VentanaPrincipal(gestorDeUsuarios, gestorDeProyectos, admin, gestorDeConvenios).setVisible(true);

                } else if (user instanceof Estudiante estudiante) {
                    Integer idProyecto = gestorDeUsuarios.obtenerIdProyectoEstudiante(estudiante.getId());

                    if (idProyecto == null) {
                        new SeleccionarProyecto(gestorDeProyectos, estudiante.getId()).setVisible(true);
                    } else {
                        new VerProyecto(gestorDeProyectos, idProyecto).setVisible(true);
                    }

                } else if (user instanceof Director) {
                    new AprobacionDeProyectos(gestorDeProyectos).setVisible(true);

                } else if (user instanceof Tutor tutor) {
                    new ProyectosACargo(gestorDeProyectos, tutor.getId()).setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "Tipo de usuario desconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                String mensaje = ex.getMessage().toLowerCase();
                if (mensaje.contains("usuario")) {
                    JOptionPane.showMessageDialog(this, "El nombre de usuario es incorrecto.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                } else if (mensaje.contains("contraseña") || mensaje.contains("contrasena")) {
                    JOptionPane.showMessageDialog(this, "La contraseña es incorrecta.", "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}