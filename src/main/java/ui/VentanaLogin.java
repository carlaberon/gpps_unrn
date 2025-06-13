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
        setSize(400, 350);
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

        Dimension campoPequeno = new Dimension(150, 25);
        txtUsuario.setPreferredSize(campoPequeno);
        txtContrasena.setPreferredSize(campoPequeno);
        btnIniciarSesion.setPreferredSize(new Dimension(120, 25));

        chkVerContrasena.addActionListener(e -> {
            txtContrasena.setEchoChar(chkVerContrasena.isSelected() ? (char) 0 : '\u2022');
        });

        getRootPane().setDefaultButton(btnIniciarSesion);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // ---------- Panel superior con fondo gris ----------
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(Color.decode("#BFBFBF"));
        panelTitulo.setLayout(new BoxLayout(panelTitulo, BoxLayout.Y_AXIS));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Logo centrado
        JLabel lblLogo = new JLabel();
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        ImagenPanel panelImagen = new ImagenPanel("imagenes/UNRN-color.png");
        panelTitulo.add(panelImagen);


        // Título centrado
        JLabel lblDescripcion = new JLabel("Gestor de Práctica Profesional Supervisada");
        lblDescripcion.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblDescripcion.setForeground(Color.WHITE);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelTitulo.add(lblLogo);
        panelTitulo.add(Box.createVerticalStrut(20));
        panelTitulo.add(lblDescripcion);

        // ---------- Panel formulario ----------
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.decode("#BFBFBF"));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panelFormulario.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panelFormulario.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        panelFormulario.add(txtContrasena, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        chkVerContrasena.setOpaque(true);
        chkVerContrasena.setBackground(Color.decode("#BFBFBF"));
        panelFormulario.add(chkVerContrasena, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panelFormulario.add(btnIniciarSesion, gbc);

        // ---------- Ensamblar ventana ----------
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

                    if (idProyecto == null)
                        new MenuEstudiante(gestorDeUsuarios, gestorDeProyectos, estudiante.getId()).setVisible(true);
                    else if (!gestorDeProyectos.existeConvenio(estudiante.getId(), idProyecto)) {
                        throw new RuntimeException("Ya has postulado o seleccionado un proyecto. Espere la generacion del convenio!");
                    } else
                        new VerProyecto(gestorDeProyectos, idProyecto).setVisible(true);
                } else if (user instanceof Director) {
                    new MenuPrincipalDirector(gestorDeProyectos).setVisible(true);
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
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
                dispose();
            }
        });
    }
}

