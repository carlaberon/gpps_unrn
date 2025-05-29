package front;

import javax.swing.*;

import database.ServicioDePersistenciaGestionProyectos;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import front.VentanaPrincipal;
import model.GestorDeUsuarios;
import ui.CrearPlanTrabajo;
import ui.ProyectoFormSwing;
import model.Administrador;
import model.GestorDeProyectos;
import model.ConvenioDAO;


public class VentanaPrincipal extends JFrame {

    private JButton btnCrearProyecto;
    private JButton btnCrearConvenio;
    private JButton btnCrearPlan;

    private final GestorDeUsuarios gestorDeUsuarios;
    private final GestorDeProyectos gestorDeProyectos;
    private final Administrador administrador;
    private final ConvenioDAO convenioDAO;

    public VentanaPrincipal(GestorDeUsuarios gestorDeUsuarios, GestorDeProyectos gestorDeProyectos,
                            Administrador administrador, ConvenioDAO convenioDAO) {
        this.gestorDeUsuarios = gestorDeUsuarios;
        this.gestorDeProyectos = gestorDeProyectos;
        this.administrador = administrador;
        this.convenioDAO = convenioDAO;

        setTitle("Gestión de Proyectos PPS");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        initComponents();
        setupLayout();
        setupListeners();
    }

    private void initComponents() {
        btnCrearProyecto = new JButton("Crear Proyecto");
        btnCrearConvenio = new JButton("Crear Convenio");
        btnCrearPlan = new JButton("Crear Plan de Trabajo");
    }

    private void setupLayout() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10)); 

        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnCrearProyecto);
        panel.add(btnCrearConvenio);
        panel.add(btnCrearPlan);

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
                GenerarConvenio convenioForm = new GenerarConvenio(administrador, convenioDAO);
                convenioForm.setVisible(true);
            }
        });

        btnCrearPlan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProyecto = 1; 
                CrearPlanTrabajo planForm = new CrearPlanTrabajo(
                    new ServicioDePersistenciaGestionProyectos(), 
                    idProyecto
                );
                planForm.setVisible(true);
            }
        });
    }
}
