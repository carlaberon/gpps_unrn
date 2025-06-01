package front;

import model.*;
import ui.SeleccionarProyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class VentanaLogin extends JFrame {

    private final GestorDeUsuarios gestorDeUsuarios;
    private final GestorDeProyectos gestorDeProyectos;
    private final GestorDeConvenios gestorDeConvenios;

    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;

    public VentanaLogin(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos, GestorDeConvenios gestorDeConvenios) {
        this.gestorDeUsuarios = gestorDeUsuarios;
        this.gestorDeProyectos = gestorDeProyectos;
        this.gestorDeConvenios = gestorDeConvenios;


        setTitle("Iniciar Sesión");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        txtUsuario = new JTextField();
        txtContrasena = new JPasswordField();
        btnIniciarSesion = new JButton("Iniciar Sesión");
    }

    private void setupLayout() {
        setLayout(new GridLayout(3, 2, 10, 10));
        add(new JLabel("Usuario:"));
        add(txtUsuario);
        add(new JLabel("Contraseña:"));
        add(txtContrasena);
        add(new JLabel());
        add(btnIniciarSesion);
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
                    new VentanaPrincipal(gestorDeUsuarios, gestorDeProyectos, admin,gestorDeConvenios).setVisible(true);
                } else if (user instanceof Estudiante) {
                    new SeleccionarProyecto(gestorDeProyectos).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Tipo de usuario desconocido.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de inicio de sesión", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
