package front;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.ServicioDePersistenciaGestionProyectos;
import front.VentanaPrincipal;
import model.*;
import ui.ProyectoFormSwing;

public class VentanaPrincipal extends JFrame {

    private JButton btnCrearProyecto;
    private JButton btnCrearConvenio;

    private final GestorDeUsuarios gestorDeUsuarios;
    private final GestorDeProyectos gestorDeProyectos;
    private final Administrador administrador;
    private final GestorDeConvenios gestorDeConvenios;

    public VentanaPrincipal(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos,
                            Administrador administrador, GestorDeConvenios gestorDeConvenios) {
        this.gestorDeUsuarios = gestorDeUsuarios;
        this.gestorDeProyectos = gestorDeProyectos;
        this.administrador = administrador;
        this.gestorDeConvenios = gestorDeConvenios;


        setTitle("Gestión de Proyectos PPS");
        setSize(400, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        btnCrearProyecto = new JButton("Crear Proyecto");
        btnCrearConvenio = new JButton("Crear Convenio");
    }

    private void setupLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnCrearProyecto);
        panel.add(btnCrearConvenio);

        add(panel);
    }

    private void setupListeners() {
        btnCrearProyecto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ProyectoFormSwing proyectoForm = new ProyectoFormSwing(gestorDeUsuarios, gestorDeProyectos);
                proyectoForm.setVisible(true);
            }
        });

        btnCrearConvenio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GenerarConvenio convenioForm = new GenerarConvenio(administrador, gestorDeConvenios);
                convenioForm.setVisible(true);
            }
        });
    }
}
